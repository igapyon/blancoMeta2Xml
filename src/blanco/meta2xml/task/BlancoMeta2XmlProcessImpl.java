/*
 * blanco Framework
 * Copyright (C) 2004-2009 IGA Tosiki
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.meta2xml.task;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.TransformerException;

import blanco.meta2xml.BlancoMeta2XmlConstants;
import blanco.meta2xml.BlancoMeta2XmlMeta2Xml;
import blanco.meta2xml.BlancoMeta2XmlXml2JavaClass;
import blanco.meta2xml.task.valueobject.BlancoMeta2XmlProcessInput;

public class BlancoMeta2XmlProcessImpl implements BlancoMeta2XmlProcess {
    /**
     * {@inheritDoc}
     */
    public int execute(final BlancoMeta2XmlProcessInput input)
            throws IOException, IllegalArgumentException {
        System.out.println("- " + BlancoMeta2XmlConstants.PRODUCT_NAME + " ("
                + BlancoMeta2XmlConstants.VERSION + ")");

        try {
            final File fileMetadir = new File(input.getMetadir());
            if (fileMetadir.exists() == false) {
                throw new IllegalArgumentException("メタディレクトリ["
                        + input.getMetadir() + "]が存在しません。");
            }

            // テンポラリディレクトリを作成。
            new File(input.getTmpdir()
                    + BlancoMeta2XmlConstants.TARGET_SUBDIRECTORY).mkdirs();

            // 指定されたメタディレクトリを処理します。
            final BlancoMeta2XmlMeta2Xml meta2Xml = new BlancoMeta2XmlMeta2Xml();
            meta2Xml.setCacheMeta2Xml(input.getCache().equals("true"));
            meta2Xml.processDirectory(fileMetadir, input.getTmpdir()
                    + BlancoMeta2XmlConstants.TARGET_SUBDIRECTORY);

            // XML化されたメタファイルからValueObjectを生成
            // 最初にテンポラリフォルダを走査
            final File fileWorkdir = new File(input.getTmpdir()
                    + BlancoMeta2XmlConstants.TARGET_SUBDIRECTORY);
            final File[] fileMeta2 = fileWorkdir.listFiles();
            if (fileMeta2 == null) {
                throw new IllegalArgumentException("ワークディレクトリ["
                        + fileWorkdir.getAbsolutePath() + "]のファイル一覧の取得に失敗しました。");
            }

            for (int index = 0; index < fileMeta2.length; index++) {
                if (fileMeta2[index].getName().endsWith(".xml") == false) {
                    continue;
                }

                final BlancoMeta2XmlXml2JavaClass xml2source = new BlancoMeta2XmlXml2JavaClass();
                xml2source.setEncoding(input.getEncoding());
                xml2source.process(fileMeta2[index], new File(input
                        .getTargetdir()));
            }
            return 0;
        } catch (IOException e) {
            throw new IllegalArgumentException(e.toString());
        } catch (TransformerException e) {
            throw new IllegalArgumentException(e.toString());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean progress(final String argProgressMessage) {
        System.out.println(argProgressMessage);
        return false;
    }
}
