package kp.web.sockets.helper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import kp.utils.Utils;

/**
 * The random texts generator.
 *
 */
public class RandomTextsGenerator {

	private List<String> textsList;
	private final Random random = new Random();
	private static final Path RANDOM_TEXTS_FILE = Paths.get(System.getProperty("java.io.tmpdir"))
			.resolve("RandomTexts.txt");

	/**
	 * Constructor.
	 * 
	 * @param listSize               the size of list
	 * @param textLength             the length of text
	 * @param useRandomTextsFromFile the flag for using texts from file
	 */
	public RandomTextsGenerator(int listSize, int textLength, boolean useRandomTextsFromFile) {

		super();
		final int totalListSize = 2 * listSize;
		System.out.printf(
				"RandomTextsGenerator(): total list size[%d], text length[%s], use random texts from file[%b]%n",
				totalListSize, Utils.formatNumber(textLength), useRandomTextsFromFile);
		final Instant start = Instant.now();
		try {
			if (useRandomTextsFromFile) {
				boolean successFlag = readTextsListFromFile(totalListSize);
				if (!successFlag) {
					System.out.printf("RandomTextsGenerator(): failed reading from file[%s]. Generating ...%n",
							RANDOM_TEXTS_FILE);
					useRandomTextsFromFile = false;
				}
			}
			if (!useRandomTextsFromFile) {
				generateTextsList(listSize, totalListSize, textLength);
			}
		} catch (IOException e) {
			System.out.printf("RandomTextsGenerator(): IOException[%s], random texts file[%s]%n", e.getMessage(),
					RANDOM_TEXTS_FILE);
			e.printStackTrace();
			System.exit(1);
		}
		final Instant finish = Instant.now();
		System.out.printf("RandomTextsGenerator(): %s%n", Utils.formatElapsed(start, finish));
	}

	/**
	 * Gets text from list for server.
	 * 
	 * @param number the text number
	 * @return the text element
	 */
	public String getTextForServer(int number) {
		return textsList.get(number - 1);
	}

	/**
	 * Gets text from list for client.
	 * 
	 * @param number the text number
	 * @return the text element
	 */
	public String getTextForClient(int number) {
		return textsList.get(textsList.size() / 2 + number - 1);
	}

	/**
	 * Reads texts list from file.
	 * 
	 * @param totalListSize the total size of list
	 * @return the success flag
	 * @throws IOException if an I/O error occurs
	 */
	private boolean readTextsListFromFile(int totalListSize) throws IOException {

		if (!RANDOM_TEXTS_FILE.toFile().canRead()) {
			return false;
		}
		textsList = Files.readAllLines(RANDOM_TEXTS_FILE);
		return Objects.nonNull(textsList) && totalListSize == textsList.size();
	}

	/**
	 * Generates texts list.
	 * 
	 * @param listSize      the size of list
	 * @param totalListSize the total size of list
	 * @param textLength    the length of text
	 * @throws IOException if an I/O error occurs
	 */
	private void generateTextsList(int listSize, int totalListSize, int textLength) throws IOException {

		final Function<Integer, String> stampFunc = index -> String.format("#%03d",
				index <= listSize ? index : index - listSize);
		textsList = IntStream.rangeClosed(1, totalListSize).boxed() /*-*/
				.map(index -> generateElement(textLength, stampFunc.apply(index))) /*-*/
				.collect(Collectors.toList());

		Files.write(RANDOM_TEXTS_FILE, textsList, Charset.defaultCharset());
	}

	/**
	 * Generates texts list element.
	 * 
	 * @param textLength the length of text
	 * @param stamp      the text stamp
	 * @return the random text
	 */
	private String generateElement(int textLength, String stamp) {

		final StringBuilder strBuf = random
				/*- Generate random numbers within the range 48 (Unicode for 0) to 122 (Unicode for z) */
				.ints(48, 122)
				/*- Only allow numbers: less than 57 (the digits  0-9) or
				 *  greater than 65 and less than 90 (the letters A-Z) or
				 *                   greater than 97 (the letters a-z) */
				.filter(i -> (i < 57 || i > 65) && (i < 90 || i > 97))
				/*- Map each number to a char */
				.mapToObj(i -> (char) i)
				/*- Stop when you have the required length of the string */
				.limit(textLength - 4)
				/*- Collect the chars produced into a StringBuilder */
				.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
				/*- Append the stamp */
				.append(stamp);
		return strBuf.toString();
	}

}
