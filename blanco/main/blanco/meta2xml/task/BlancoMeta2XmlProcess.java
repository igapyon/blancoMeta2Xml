package blanco.meta2xml.task;

import java.io.IOException;

import blanco.meta2xml.task.valueobject.BlancoMeta2XmlProcessInput;

/**
 * Process [BlancoMeta2XmlProcess] interface.
 *
 * Inherit this interface and create a [BlancoMeta2XmlProcessclass in the [blanco.meta2xml.task] package to implement the actual batch processing.<br>
 */
interface BlancoMeta2XmlProcess {
    /**
     * The entry point for intastantiating the class and executing the process.
     *
     * @param input Input parameters for a process.
     * @return Result of the process.
     * @throws IOException If an I/O exception occurs.
     * @throws IllegalArgumentException If an invalid input value is found.
     */
    int execute(final BlancoMeta2XmlProcessInput input) throws IOException, IllegalArgumentException;

    /**
     * Whenever an item is processed in the process, it is called back as a progress report.
     *
     * @param argProgressMessage Message about the item currently processed.
     * @return It is false if you want to continue the process, or it is true  if you want to request to suspend the process.
     */
    boolean progress(final String argProgressMessage);
}
