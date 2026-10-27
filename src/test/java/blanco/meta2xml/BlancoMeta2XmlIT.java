package blanco.meta2xml;

import static org.junit.Assert.*;

import java.io.DataInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathFactory;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.w3c.dom.Document;

/** Exercises the packaged JAR without target/classes or the bootstrap release. */
public class BlancoMeta2XmlIT {
    @Rule
    public TemporaryFolder temporary = new TemporaryFolder(new File("target"));

    private File jar;
    private String runtimeClasspath;
    private URL[] runtimeUrls;
    private URLClassLoader loader;
    private ClassLoader previousContext;

    @Before
    public void openPackagedRuntime() throws Exception {
        jar = new File(System.getProperty("blanco.jar")).getCanonicalFile();
        assertTrue("Packaged JAR is missing", jar.isFile());
        runtimeClasspath = new String(Files.readAllBytes(Paths.get(
                System.getProperty("blanco.runtime.classpath"))), StandardCharsets.UTF_8).trim();
        List<URL> urls = new ArrayList<URL>();
        urls.add(jar.toURI().toURL());
        for (String entry : runtimeClasspath.split(java.util.regex.Pattern.quote(File.pathSeparator))) {
            File dependency = new File(entry);
            assertFalse("Bootstrap JAR leaked into the runtime: " + entry,
                    dependency.getName().startsWith("blanco-meta2xml-"));
            urls.add(dependency.toURI().toURL());
        }
        runtimeUrls = urls.toArray(new URL[0]);
        loader = new URLClassLoader(runtimeUrls, ClassLoader.getSystemClassLoader().getParent());
        previousContext = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(loader);
    }

    @After
    public void closePackagedRuntime() throws Exception {
        Thread.currentThread().setContextClassLoader(previousContext);
        if (loader != null) {
            loader.close();
        }
    }

    @Test
    public void packagesXmlSourcesAndJava8Classes() throws Exception {
        try (JarFile binary = new JarFile(jar);
                JarFile sources = new JarFile(System.getProperty("blanco.sources.jar"))) {
            assertNotNull(binary.getJarEntry("blanco/meta2xml/BlancoMeta2XmlMeta2Xml.xml"));
            assertNotNull(sources.getJarEntry("blanco/meta2xml/BlancoMeta2XmlXml2JavaClass.java"));
            assertNotNull(sources.getJarEntry("blanco/meta2xml/task/BlancoMeta2XmlTask.java"));
            Properties coordinates = new Properties();
            try (InputStream input = binary.getInputStream(binary.getJarEntry(
                    "META-INF/maven/blanco.meta2xml/blanco-meta2xml/pom.properties"))) {
                coordinates.load(input);
            }
            assertEquals(coordinates.getProperty("version"), loader.loadClass(
                    "blanco.meta2xml.BlancoMeta2XmlConstants").getField("VERSION").get(null));
            int classes = 0;
            for (Enumeration<JarEntry> entries = binary.entries(); entries.hasMoreElements();) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    classes++;
                    try (DataInputStream input = new DataInputStream(binary.getInputStream(entry))) {
                        assertEquals(0xcafebabe, input.readInt());
                        input.readUnsignedShort();
                        assertEquals(entry.getName(), 52, input.readUnsignedShort());
                    }
                }
            }
            assertTrue("No project classes were packaged", classes > 0);
        }
        Class<?> converter = loader.loadClass("blanco.meta2xml.BlancoMeta2XmlMeta2Xml");
        assertEquals(jar.toURI().toURL(), converter.getProtectionDomain().getCodeSource().getLocation());
        URL resource = loader.getResource("blanco/meta2xml/BlancoMeta2XmlMeta2Xml.xml");
        assertNotNull(resource);
        assertTrue("XML must come from the packaged JAR", resource.toString().startsWith(
                "jar:" + jar.toURI().toURL().toString() + "!/"));
    }

    @Test
    public void generatesAndCompilesConverterFromXlsx() throws Exception {
        File xml = convert("blanco.meta2xml.BlancoMeta2XmlMeta2Xml",
                new File("meta/program/BlancoMeta2XmlMeta2Xml.xlsx"));
        assertEquals("BlancoMeta2XmlMeta2Xml", xpath(xml, "//blancometa2xml-process-common/name"));
        File generated = generate("blanco.meta2xml.BlancoMeta2XmlXml2JavaClass", xml);
        File classes = compile(generated);
        URL[] urls = new URL[runtimeUrls.length + 1];
        urls[0] = classes.toURI().toURL();
        System.arraycopy(runtimeUrls, 0, urls, 1, runtimeUrls.length);
        try (URLClassLoader generatedLoader = new URLClassLoader(urls,
                ClassLoader.getSystemClassLoader().getParent())) {
            Class<?> type = generatedLoader.loadClass("blanco.meta2xml.BlancoMeta2XmlMeta2Xml");
            assertEquals(classes.toURI().toURL(), type.getProtectionDomain().getCodeSource().getLocation());
            File roundtrip = temporary.newFile("roundtrip.xml");
            type.getMethod("process", File.class, File.class).invoke(type.getDeclaredConstructor().newInstance(),
                    new File("meta/program/BlancoMeta2XmlMeta2Xml.xlsx"), roundtrip);
            assertEquals("BlancoMeta2XmlMeta2Xml", xpath(roundtrip, "//blancometa2xml-process-common/name"));
        }
    }

    @Test
    public void readsLegacyXlsDefinition() throws Exception {
        File legacy = temporary.newFile("definition.xls");
        try (InputStream input = Files.newInputStream(Paths.get("meta/program/BlancoMeta2XmlMeta2Xml.xlsx"));
                Workbook source = WorkbookFactory.create(input);
                Workbook target = new HSSFWorkbook()) {
            DataFormatter formatter = new DataFormatter();
            for (Sheet sheet : source) {
                Sheet copy = target.createSheet(sheet.getSheetName());
                for (Row row : sheet) {
                    Row copyRow = copy.createRow(row.getRowNum());
                    for (Cell cell : row) {
                        copyRow.createCell(cell.getColumnIndex()).setCellValue(formatter.formatCellValue(cell));
                    }
                }
            }
            try (OutputStream output = Files.newOutputStream(legacy.toPath())) {
                target.write(output);
            }
        }
        File xml = convert("blanco.meta2xml.BlancoMeta2XmlMeta2Xml", legacy);
        assertEquals("BlancoMeta2XmlMeta2Xml", xpath(xml, "//blancometa2xml-process-common/name"));
    }

    @Test
    public void readsBlankTemplate() throws Exception {
        File xml = convert("blanco.meta2xml.BlancoMeta2XmlMeta2Xml",
                new File("meta/template/BlancoMeta2XmlTemplate.xlsx"));
        assertEquals("workbook", parse(xml).getDocumentElement().getTagName());
        assertEquals("", xpath(xml, "//blancometa2xml-process-common/name"));
    }

    @Test
    public void generatesAndCompilesRepresentativeValueObject() throws Exception {
        File xml = convert("blanco.valueobject.BlancoValueObjectMeta2Xml",
                new File("meta/program/BlancoMeta2XmlStructure.xlsx"));
        assertEquals("BlancoMeta2XmlStructure", xpath(xml, "//blancovalueobject-common/name"));
        File generated = generate("blanco.valueobject.BlancoValueObjectXml2JavaClass", xml);
        compile(generated);
        assertTrue(new File(generated, "main/blanco/meta2xml/valueobject/BlancoMeta2XmlStructure.java").isFile());
    }

    private File convert(String className, File input) throws Exception {
        File output = new File(temporary.newFolder(), "definition.xml");
        Class<?> type = loader.loadClass(className);
        type.getMethod("process", File.class, File.class).invoke(type.getDeclaredConstructor().newInstance(), input, output);
        parse(output);
        return output;
    }

    private File generate(String className, File xml) throws Exception {
        File output = temporary.newFolder();
        Class<?> type = loader.loadClass(className);
        Object generator = type.getDeclaredConstructor().newInstance();
        type.getMethod("setEncoding", String.class).invoke(generator, "UTF-8");
        type.getMethod("process", File.class, File.class).invoke(generator, xml, output);
        return output;
    }

    private File compile(File generated) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        assertNotNull("Integration tests require a JDK", compiler);
        File classes = temporary.newFolder();
        List<String> options = new ArrayList<String>(Arrays.asList("--release", "8", "-encoding", "UTF-8",
                "-classpath", jar + File.pathSeparator + runtimeClasspath, "-d", classes.toString()));
        try (Stream<Path> files = Files.walk(generated.toPath())) {
            files.filter(p -> p.toString().endsWith(".java")).sorted().forEach(p -> options.add(p.toString()));
        }
        assertTrue("Generator produced no Java source", options.size() > 8);
        assertEquals("Generated Java must compile", 0, compiler.run(null, null, null, options.toArray(new String[0])));
        return classes;
    }

    private Document parse(File xml) throws Exception {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xml);
    }

    private String xpath(File xml, String expression) throws Exception {
        return XPathFactory.newInstance().newXPath().evaluate(expression, parse(xml));
    }
}
