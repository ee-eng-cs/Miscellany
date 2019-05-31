package kp.files;

import kp.files.impl.SimpleFileVisitorExtension;

/**
 * The main launcher for files research.
 *
 */
public class MainForFiles {
	private static final boolean ALL = true;
	private static final boolean FILES_AND_ZIP_FILES = false;
	private static final boolean PROPERTY_FILES = false;
	private static final boolean TEMPORARY_FILES_AND_ZIP_FILES = false;
	private static final boolean XML_FILES = false;
	private static final boolean SIMPLE_FILE_VISITOR_EXTENSION = false;

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		if (ALL || FILES_AND_ZIP_FILES) {
			FilesAndZipFiles.readFiles();
		}
		if (ALL || PROPERTY_FILES) {
			PropertyFiles.readProperties();
		}
		if (ALL || TEMPORARY_FILES_AND_ZIP_FILES) {
			TemporaryFilesAndZipFiles.writeAndReadTemporaries();
		}
		if (ALL || XML_FILES) {
			XmlFiles.readXmlFile();
		}
		if (ALL || SIMPLE_FILE_VISITOR_EXTENSION) {
			SimpleFileVisitorExtension.searchKeywordsInFiles();
		}
	}
}