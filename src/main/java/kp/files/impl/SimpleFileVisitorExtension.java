package kp.files.impl;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Simple file visitor extension.
 * 
 */
public class SimpleFileVisitorExtension extends SimpleFileVisitor<Path> {

	private static final List<String> KEYWORDS = List.of("class MainForFiles", "cryptographic nonce");
	private static final Path BASE_DIRECTORY = Paths.get("src");
	private static final List<String> EXCLUDED_DIRECTORIES = List.of("resources");
	private static final List<String> EXCLUDED_FILES = List.of("html");
	private static final List<Pattern> PATTERNS = KEYWORDS.stream()/*-*/
			.map(key -> Pattern.compile(".*".concat(key).concat(".*"), Pattern.CASE_INSENSITIVE))/*-*/
			.collect(Collectors.toList());
	private static final boolean VERBOSE = false;

	private final Map<Path, Map<String, List<String>>> resultMap = new TreeMap<>();

	/**
	 * Searches keywords in files.
	 * 
	 */
	public static void searchKeywordsInFiles() {

		final SimpleFileVisitorExtension fileVisitor = new SimpleFileVisitorExtension();
		final Instant start = Instant.now();
		try {
			if (true) {
				Files.walkFileTree(BASE_DIRECTORY, fileVisitor);
			} else {
				/*-
				 * The alternative solution which does not need
				 * the extending of a SimpleFileVisitor.
				 */
				fileVisitor.processWithoutUsingFileVisitor();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
		fileVisitor.showResults(start);
		System.out.println("- ".repeat(50));
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs) {

		if (Objects.isNull(path.getFileName()) || EXCLUDED_DIRECTORIES.contains(path.getFileName().toString())) {
			System.out.printf("preVisitDirectory(): excluded directory, path[%s], returning 'SKIP_SUBTREE'%n", path);
			return FileVisitResult.SKIP_SUBTREE;
		}
		if (VERBOSE) {
			System.out.printf("preVisitDirectory(): path[%s]%n", path);
		}
		return FileVisitResult.CONTINUE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {

		final Path fileName = path.getFileName();
		if (Objects.isNull(fileName) || fileName.toString().lastIndexOf('.') == -1) {
			System.out.printf("visitFile(): null file or file without extension, path[%s]%n", path);
			return FileVisitResult.CONTINUE;
		}
		if (!path.toFile().canRead()) {
			System.out.printf("visitFile(): unreadable path[%s]%n", path);
			return FileVisitResult.CONTINUE;
		}
		final String extension = fileName.toString().substring(fileName.toString().lastIndexOf('.') + 1);
		if (EXCLUDED_FILES.contains(extension)) {
			System.out.printf("visitFile(): excluded file with extension[%s], path[%s]%n", extension, path);
			return FileVisitResult.CONTINUE;
		}
		createMapForPath(path);
		if (VERBOSE) {
			System.out.printf("visitFile(): processed path[%s]%n", path);
		}
		return FileVisitResult.CONTINUE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public FileVisitResult visitFileFailed(Path path, IOException exception) {
		System.out.printf("visitFileFailed(): path[%s], IOException[%s]%n", path, exception);
		return FileVisitResult.CONTINUE;
	}

	/**
	 * Creates map for path.
	 * 
	 * @param path the path
	 */
	private void createMapForPath(Path path) {

		final Map<String, List<String>> map = new TreeMap<>();
		final Consumer<String> action = line -> IntStream.range(0, KEYWORDS.size()).boxed()/*-*/
				.filter(i -> PATTERNS.get(i).matcher(line).find())/*-*/
				.map(i -> KEYWORDS.get(i))/*-*/
				.peek(keyword -> map.putIfAbsent(keyword, new ArrayList<String>()))/*-*/
				.forEach(keyword -> map.get(keyword).add(line));
		try {
			String content = new String(Files.readAllBytes(path), "UTF-8");
			Stream.of(content.split("\\R+")).forEach(action);
		} catch (IOException ex) {
			System.out.printf("createMapForPath(): IOException[%s], path[%s]%n", ex.getMessage(), path);
			System.exit(1);
		}
		resultMap.put(path, map);
	}

	/**
	 * Shows results.
	 * 
	 * @param start the start instant
	 */
	private void showResults(Instant start) {

		System.out.printf("►►► Result, number of searched files[%d], time elapsed[%tT.%<tL]%n", resultMap.size(),
				LocalTime.MIDNIGHT.plus(Duration.between(start, Instant.now())));
		resultMap.keySet().stream()/*-*/
				.filter(Predicate.not(pathKey -> resultMap.get(pathKey).isEmpty()))/*-*/
				.forEach(pathKey -> showResultsFromFile(pathKey));
	}

	/**
	 * Shows results from file.
	 * 
	 * @param pathKey the path key
	 */
	private void showResultsFromFile(Path pathKey) {

		System.out.printf("%n▼▼▼ number of keywords[%d] ▼▼▼ file[%s] ▼▼▼%n", resultMap.get(pathKey).size(), pathKey);
		final BiConsumer<String, List<String>> action = (key, list) -> list.stream().map(String::trim)
				.forEach(line -> System.out.printf("[%-20s] line[%s]%n", key, line));
		resultMap.get(pathKey).forEach(action);
	}

	/**
	 * Processes without using the file visitor.
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	private void processWithoutUsingFileVisitor() throws IOException {

		final Deque<Path> stack = new ArrayDeque<Path>();
		stack.push(BASE_DIRECTORY);
		while (!stack.isEmpty()) {
			final DirectoryStream<Path> stream = Files.newDirectoryStream(stack.pop());
			try (stream) {
				for (Path path : stream) {
					if (Objects.isNull(path.getFileName())
							|| EXCLUDED_DIRECTORIES.contains(path.getFileName().toString())) {
						System.out.printf("processWithoutUsingFileVisitor(): "
								+ "excluded directory, path[%s], returning 'SKIP_SUBTREE'%n", path);
						continue;
					}
					if (Files.isDirectory(path)) {
						stack.push(path);
						continue;
					}
					visitFile(path, null);
				}
			}
		}
	}
}