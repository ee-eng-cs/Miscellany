package kp.files;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Reading the files and the ZIP files.
 *
 */
public interface FilesAndZipFiles {

	/**
	 * Reads files.
	 * 
	 */
	public static void readFiles() {
		final Consumer<Reader> readerConsumer = reader -> {
			final CharBuffer charBuffer = CharBuffer.allocate(Constants.XML_CONTENT.length());
			try {
				reader.read(charBuffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
			charBuffer.flip();
			System.out.println(charBuffer);
		};
		readFile(readerConsumer);
		listFiles();
		readZipFile(readerConsumer);
		listFilesInZipFile();
		System.out.println("- ".repeat(50));
	}

	/**
	 * Reads file.
	 * 
	 * @param readerConsumer the reader consumer
	 */
	private static void readFile(Consumer<Reader> readerConsumer) {

		final Consumer<InputStream> inputStreamConsumer = inputStream -> {
			final byte[] bytes = new byte[Constants.XML_CONTENT.length()];
			try {
				inputStream.read(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
			final String content = new String(bytes);
			System.out.println(content);
		};
		final Consumer<InputStream> inputStreamConsumerWithReadAll = inputStream -> {
			byte[] bytes = null;
			try {
				bytes = inputStream.readAllBytes();
			} catch (IOException e) {
				e.printStackTrace();
			}
			final String content = new String(bytes);
			System.out.println(content.substring(0, Constants.XML_CONTENT.length()));
		};
		final Consumer<InputStream> inputStreamConsumerWithTransfer = inputStream -> {
			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try {
				inputStream.transferTo(outputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			final String content = new String(outputStream.toByteArray());
			System.out.println(content.substring(0, Constants.XML_CONTENT.length()));
		};
		try {
			System.out.println("▼▼▼ Files.newInputStream(...) ▼▼▼▼▼▼▼▼▼ with 'read(...)'");
			// faster
			try (InputStream inputStream = Files.newInputStream(Constants.EXAMPLE_PATH)) {
				inputStreamConsumer.accept(inputStream);
			}
			System.out.println("▼▼▼ Files.newInputStream(...) ▼▼▼▼▼▼▼▼▼ with 'readAllBytes()'");
			try (InputStream inputStream = Files.newInputStream(Constants.EXAMPLE_PATH)) {
				inputStreamConsumerWithReadAll.accept(inputStream);
			}
			System.out.println("▼▼▼ Files.newInputStream(...) ▼▼▼▼▼▼▼▼▼ with 'transferTo()'");
			try (InputStream inputStream = Files.newInputStream(Constants.EXAMPLE_PATH)) {
				inputStreamConsumerWithTransfer.accept(inputStream);
			}
			System.out.println("▼▼▼ new FileInputStream(...) ▼▼▼▼▼▼▼▼▼▼");
			// for big files FileInputStream is faster
			try (InputStream inputStream = new FileInputStream(Constants.EXAMPLE_PATH.toFile())) {
				inputStreamConsumer.accept(inputStream);
			}
			System.out.println("▼▼▼ Channels.newInputStream(...) ▼▼▼▼▼▼");
			final FileChannel fileChannel1 = FileChannel.open(Constants.EXAMPLE_PATH);
			final InputStream inputStream1 = Channels.newInputStream(fileChannel1);
			try (fileChannel1; inputStream1) {
				inputStreamConsumer.accept(inputStream1);
			}
			System.out.println("▼▼▼ Channels.newReader(...) ▼▼▼▼▼▼▼▼▼▼▼");
			// faster
			final FileChannel fileChannel2 = FileChannel.open(Constants.EXAMPLE_PATH);
			final Reader reader1 = Channels.newReader(fileChannel2, StandardCharsets.UTF_8.name());
			try (fileChannel2; reader1) {
				readerConsumer.accept(reader1);
			}
			System.out.println("▼▼▼ new InputStreamReader(...) ▼▼▼▼▼▼▼▼");
			final InputStream inputStream2 = Files.newInputStream(Constants.EXAMPLE_PATH);
			final Reader reader2 = new InputStreamReader(inputStream2, StandardCharsets.UTF_8.name());
			try (inputStream2; reader2) {
				readerConsumer.accept(reader2);
			}
			System.out.println("▼▼▼ Files.newBufferedReader(...) ▼▼▼▼▼▼");
			try (Stream<String> linesStream = Files.newBufferedReader(Constants.EXAMPLE_PATH).lines()) {
				linesStream.findFirst().ifPresent(System.out::println);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Lists files.
	 * 
	 */
	private static void listFiles() {
		try {
			final Consumer<Path> pathConsumer = path -> System.out.printf("path[%s], absolute[%s], file[%s]%n", path,
					path.toAbsolutePath(), path.getFileName());
			System.out.println("\n▼▼▼ list '*.xml' and '*.xsd' files ▼▼▼▼▼▼▼▼▼▼▼▼ with 'DirectoryStream'");
			try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(Constants.EXAMPLE_PATH.getParent(),
					"*.{xml,xsd}")) {
				dirStream.forEach(pathConsumer);
			}
			final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:**\\\\*.zip");
			System.out.println("▼▼▼ list '*.zip' files             ▼▼▼▼▼▼▼▼▼▼▼▼ with 'Files.walk(...)'");
			/*- 'Files.walk()' - this method must be used within a try-with-resources statement */
			try (Stream<Path> pathStream = Files.walk(Constants.EXAMPLE_PATH.getParent())) {
				pathStream.filter(pathMatcher::matches).forEach(pathConsumer);
			}
			System.out.println("▼▼▼ list '*.zip' files             ▼▼▼▼▼▼▼▼▼▼▼▼ with 'Files.list(...)'");
			try (Stream<Path> pathStream = Files.list(Constants.EXAMPLE_PATH.getParent())) {
				pathStream.filter(pathMatcher::matches).forEach(pathConsumer);
			}
			// java.io.FileFilter - since Java 1.2
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	/**
	 * Lists file.
	 * 
	 * @param readerConsumer the reader consumer
	 */
	private static void readZipFile(Consumer<Reader> readerConsumer) {

		final BiConsumer<ZipFile, ZipEntry> zipConsumer = (zipFile, zipEntry) -> {
			try (Reader reader = new BufferedReader(/*-*/
					new InputStreamReader(/*-*/
							zipFile.getInputStream(zipEntry)))) {
				readerConsumer.accept(reader);
			} catch (IOException e) {
				e.printStackTrace();
			}
		};
		System.out.println("▼▼▼ zipFile.stream() ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
		final Predicate<ZipEntry> predicate = zipEntry -> Constants.EXAMPLE_ZIP_ENTRY.equals(zipEntry.getName());
		try (ZipFile zipFile = new ZipFile(Constants.EXAMPLE_ZIP_PATH.toString())) {
			final ZipEntry zipEntry = zipFile.stream().filter(predicate).findFirst().get();
			zipConsumer.accept(zipFile, zipEntry);
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * ZIP file system allows copying, moving, and renaming files.
		 */
		System.out.println("▼▼▼ ZIP File as FileSystem ▼▼▼▼▼▼▼▼▼▼▼▼");
		try (FileSystem fileSystem = FileSystems.newFileSystem(Constants.EXAMPLE_ZIP_PATH, null)) {
			final Path pathInZipfile = fileSystem.getPath(Constants.EXAMPLE_ZIP_ENTRY);
			try (InputStream inputStream = Files.newInputStream(pathInZipfile);
					final Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8.name())) {
				readerConsumer.accept(reader);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Lists files in ZIP file.
	 * 
	 */
	private static void listFilesInZipFile() {

		System.out.println("\n▼▼▼ list files in zip file ▼▼▼▼▼▼▼▼▼▼▼▼");
		final Consumer<ZipEntry> entryConsumer = entry -> System.out.printf("zipped %s[%s]%n",
				entry.isDirectory() ? " dir" : "file", entry.getName());
		try (Stream<? extends ZipEntry> zipEntryStream = new ZipFile(Constants.EXAMPLE_ZIP_PATH.toString()).stream()) {
			zipEntryStream.forEach(entryConsumer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}