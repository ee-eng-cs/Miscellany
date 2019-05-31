package kp.files;

import java.nio.file.Path;

/**
 * Constants for files and directories.
 *
 */
public interface Constants {
	/**
	 * Example directory.
	 */
	Path EXAMPLE_DIR = Path.of("src/main/resources");
	/**
	 * Example XML file.
	 */
	Path EXAMPLE_PATH = Path.of(EXAMPLE_DIR.toString(), "example.xml");
	/**
	 * Example text file name.
	 */
	String EXAMPLE_TXT_FILE_NAME = "example.txt";
	/**
	 * Example ZIP file name.
	 */
	String EXAMPLE_ZIP_FILE_NAME = "example.zip";
	/**
	 * Example ZIP file.
	 */
	Path EXAMPLE_ZIP_PATH = Path.of(EXAMPLE_DIR.toString(), EXAMPLE_ZIP_FILE_NAME);
	/**
	 * Example ZIP file entry.
	 */
	String EXAMPLE_ZIP_ENTRY = "data/example.xml";
	/**
	 * Example properties file.
	 */
	Path EXAMPLE_PROPERTIES_PATH = Path.of(System.getProperty("user.dir"), EXAMPLE_DIR.toString(),
			"example.properties");
	/**
	 * Length of string to read.
	 */
	String XML_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
}
