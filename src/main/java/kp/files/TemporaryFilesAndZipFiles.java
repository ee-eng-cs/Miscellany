package kp.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Writing and reading the temporary files and the ZIP files.
 *
 */
public interface TemporaryFilesAndZipFiles {

	/**
	 * Writes and reads temporary files.
	 * 
	 */
	public static void writeAndReadTemporaries() {

		final Path previousTmpDir = Paths.get(System.getProperty("java.io.tmpdir"));
		System.setProperty("java.io.tmpdir", "c:\\Temp");
		System.out.printf("Temporary directory from property 'java.io.tmpdir': previous[%s], actual[%s]%n%n",
				previousTmpDir, Paths.get(System.getProperty("java.io.tmpdir")));
		try {
			writeAndReadTemporaryFiles();
			writeAndReadTemporaryZipFiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("- ".repeat(50));
	}

	/**
	 * Writes and reads temporary files.
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	private static void writeAndReadTemporaryFiles() throws IOException {

		final List<String> contentList = List.of("The first line.", "The second line.");

		final Path tempDirectory = Files.createTempDirectory("example");
		System.out.printf("Created temporary directory[%s]%n", tempDirectory);
		final Path tempSubDirectory = Files.createDirectories(tempDirectory.resolve("dir1").resolve("dir2"));
		System.out.printf("Created temporary directory[%s]%n", tempSubDirectory);

		final Path fileInTempDir = tempDirectory.resolve(Constants.EXAMPLE_TXT_FILE_NAME);
		System.out.printf("Created  temporary file[%s]%n", fileInTempDir);
		Files.write(fileInTempDir, contentList, Charset.defaultCharset());

		System.out.printf("▼▼▼ From temporary file[%s] ▼▼▼%n", fileInTempDir);
		/*- 'Files.lines()' - this method must be used within a try-with-resources statement. */
		try (Stream<String> linesStream = Files.lines(fileInTempDir)) {
			linesStream.forEach(System.out::println);
		}
		System.out.printf("▼▼▼ From temporary file[%s] ▼▼▼%n", fileInTempDir);
		Files.writeString(fileInTempDir, "example string");
		System.out.println(Files.readString(fileInTempDir));

		/*- 'Files.walk()' - this method must be used within a try-with-resources statement. */
		try (Stream<Path> pathStream = Files.walk(tempDirectory)) {
			pathStream.sorted(Comparator.reverseOrder())/*-*/
					.map(Path::toFile)/*-*/
					.peek(arg -> System.out.printf("Deleting [%s]%n", arg))/*-*/
					.forEach(File::delete);
		}
		final String prefix = Constants.EXAMPLE_TXT_FILE_NAME.substring(0,
				Constants.EXAMPLE_TXT_FILE_NAME.indexOf('.'));
		final String suffix = Constants.EXAMPLE_TXT_FILE_NAME.substring(Constants.EXAMPLE_TXT_FILE_NAME.indexOf('.'));
		final Path tempFile = Files.createTempFile(prefix, suffix);
		System.out.printf("%nCreated  temporary file[%s]%n", tempFile);
		try (BufferedWriter bufferedWriter = Files.newBufferedWriter(tempFile)) {
			for (String line : contentList) {
				bufferedWriter.write(line.concat("\n"));
			}
		}
		System.out.printf("▼▼▼ From temporary file[%s] ▼▼▼%n", tempFile);
		try (BufferedReader bufferedReader = Files.newBufferedReader(tempFile)) {
			bufferedReader.lines().forEach(System.out::println);
		}
		System.out.printf("Deleted  temporary file[%s], result[%b]%n%n", tempFile, Files.deleteIfExists(tempFile));
	}

	/**
	 * Writes and reads temporary ZIP files.
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	public static void writeAndReadTemporaryZipFiles() throws IOException {

		final String prefix = Constants.EXAMPLE_ZIP_FILE_NAME.substring(0,
				Constants.EXAMPLE_ZIP_FILE_NAME.indexOf('.'));
		final String suffix = Constants.EXAMPLE_ZIP_FILE_NAME.substring(Constants.EXAMPLE_ZIP_FILE_NAME.indexOf('.'));
		final Path tempFile = Files.createTempFile(prefix, suffix);
		System.out.printf("Created  temporary file[%s]%n", tempFile);
		try (ZipOutputStream zipOutputStream = new ZipOutputStream(/*-*/
				Files.newOutputStream(tempFile))) {
			final ZipEntry zipEntry = new ZipEntry(Constants.EXAMPLE_ZIP_ENTRY);
			zipOutputStream.putNextEntry(zipEntry);
			System.out.printf("Created zip entry[%s]%n", zipEntry);
			zipOutputStream.write(Constants.XML_CONTENT.getBytes());
		}
		final Predicate<ZipEntry> predicate = zipEntry -> Constants.EXAMPLE_ZIP_ENTRY.equals(zipEntry.getName());
		final BiConsumer<ZipFile, ZipEntry> zipConsumer = (zipFile, zipEntry) -> {
			try (BufferedReader bufferedReader = new BufferedReader(/*-*/
					new InputStreamReader(zipFile.getInputStream(zipEntry)))) {
				bufferedReader.lines().forEach(System.out::println);
			} catch (IOException e) {
				e.printStackTrace();
			}
		};
		try (ZipFile zipFile = new ZipFile(tempFile.toString())) {
			final ZipEntry zipEntry = zipFile.stream().filter(predicate).findFirst().get();
			System.out.printf("▼▼▼ From temporary file[%s], zip entry[%s] ▼▼▼%n", tempFile, zipEntry.getName());
			zipConsumer.accept(zipFile, zipEntry);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.printf("Deleted  temporary file[%s], result[%b]%n", tempFile, Files.deleteIfExists(tempFile));
	}
}
