package kp.regex;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * The main launcher for regular expressions research.
 *
 */
public class MainForRegex {
	private static final boolean ALL = true;
	private static final boolean REGEX = false;
	private static final boolean REPLACE_WITH_MATCHER = false;
	private static final boolean SPLIT = false;
	private static final boolean SCANNER = false;

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		if (ALL || REGEX) {
			showGreedyReluctantPossessiveQuantifiers();
		}
		if (ALL || REGEX) {
			showNamedCapturingGroups();
		}
		if (ALL || REGEX) {
			filterStreamWithMatchedRegexPredicate_1();
		}
		if (ALL || REGEX) {
			filterStreamWithMatchedRegexPredicate_2();
		}
		if (ALL || REGEX) {
			filterStreamWithPatternPredicates();
		}
		if (ALL || REPLACE_WITH_MATCHER) {
			replaceAllWithMatcherInSingleLine();
		}
		if (ALL || REPLACE_WITH_MATCHER) {
			replaceAllWithMatcherInMultiline();
		}
		if (ALL || REPLACE_WITH_MATCHER) {
			appendReplacementWithMatcher();
		}
		if (ALL || SPLIT) {
			splitTokenizing();
		}
		if (ALL || SCANNER) {
			scannerTokenizing();
		}
	}

	/**
	 * Show usage of greedy, reluctant and possessive quantifiers.
	 * 
	 */
	private static void showGreedyReluctantPossessiveQuantifiers() {

		final String text = "abc---abc---abc";
		System.out.printf("\t input text[%s]%n", text);
		final Stream<List<String>> dataStream = List.of( /*-*/
				List.of(".*abc", "greedy quantifiers"), /*-*/
				List.of("abc.*", "greedy quantifiers"), /*-*/
				List.of(".*+abc", "possessive quantifiers"), /*-*/
				List.of("abc.*+", "possessive quantifiers"), /*-*/
				List.of(".*?abc", "reluctant quantifiers"), /*-*/
				List.of("abc.*?", "reluctant quantifiers")) /*-*/
				.stream();

		dataStream.peek(arg -> System.out.printf("Pattern[%s], %s%n", arg.get(0), arg.get(1)))/*-*/
				.map(arg -> Pattern.compile(arg.get(0)))/*-*/
				.map(pat -> pat.matcher(text))/*-*/
				.peek(mat -> System.out.printf("\tmatches[%b]%n%s", mat.matches(),
						mat.matches() ? "" : "\tno output\n"))/*-*/
				.filter(Matcher::matches)/*-*/
				.map(mat -> mat.replaceAll("►$0◄"))/*-*/
				.forEach(str -> System.out.printf("\toutput text[%s]%n", str));
		System.out.println("- ".repeat(50));
	}

	/**
	 * Show named capturing groups.
	 * 
	 */
	private static void showNamedCapturingGroups() {

		// Mon Jan 01 12:00:00 CET 2000
		final String regex = "(?<DayOfWeek>\\S{3})\\s"/*-*/
				+ "(?<Date>(?<Month>\\S{3})\\s(?<Day>\\S{2}))\\s"/*-*/
				+ "(?<Time>(?<Hour>\\S{2}):(?<Minute>\\S{2}):(?<Second>\\S{2}))\\s"/*-*/
				+ "(?<TimeZone>\\S{3,4})\\s"/*-*/
				+ "(?<Year>\\S{4})";
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(new Date().toString());
		if (matcher.find()) {
			System.out.printf(
					"From named capturing groups: year[%s], month[%s], day[%s], date[%s], day of the week[%s]%n",
					matcher.group("Year"), matcher.group("Month"), matcher.group("Day"), matcher.group("Date"),
					matcher.group("DayOfWeek"));
			System.out.printf(
					"From named capturing groups: hour[%s], minute[%s], second[%s], time[%s], time zone[%s]%n",
					matcher.group("Hour"), matcher.group("Minute"), matcher.group("Second"), matcher.group("Time"),
					matcher.group("TimeZone"));
		}
		System.out.println("- ".repeat(50));
	}

	/**
	 * Filters stream with matched regex predicate.
	 * 
	 */
	private static void filterStreamWithMatchedRegexPredicate_1() {

		final Supplier<Stream<String>> streamSupplier = () -> Stream.of("One", "two", "Three");
		System.out.printf("Source %s%n", streamSupplier.get().collect(Collectors.toList()));
		// OK is word without any letter except an uppercase letter (subtraction)
		final String REGEX = "(?x) [ \\p{L} && [^\\p{Lu}] ]+";
		final Predicate<String> predicate = Predicate.not(arg -> arg.matches(REGEX));

		final List<String> list = streamSupplier.get()/*-*/
				.collect(Collectors.filtering(predicate, Collectors.toList()));
		System.out.printf("Result %s%n", list);
		System.out.println("- ".repeat(50));
	}

	/**
	 * Filters stream with matched regex predicate.
	 * 
	 */
	private static void filterStreamWithMatchedRegexPredicate_2() {

		final Stream<Character.UnicodeScript> stream = Stream.of(Character.UnicodeScript.values());
		final List<String> sourceList = stream.map(Enum::name).collect(Collectors.toList());
		System.out.printf("Enum 'Character.UnicodeScript', source list count[%d]%n%n", sourceList.size());

		final Consumer<String> regexConsumer = regex -> {
			final List<String> targetList = sourceList.stream()/*-*/
					.collect(Collectors.filtering(arg -> arg.matches(regex), Collectors.toList()));
			System.out.printf("Regex[%s], matched list size[%2d], matched list:%n  %s%n", regex, targetList.size(),
					targetList);
		};
		Stream.of(".{21,}", /*- 21 chars or more */
				".{1,3}", /*- 3 chars or less */
				"OLD.*", /*- starts with OLD */
				".*(.)\\1.*" /*- double letter */
		).forEach(regexConsumer);
		System.out.println("- ".repeat(50));
	}

	/**
	 * Filters stream with predicates from pattern.
	 * 
	 */
	private static void filterStreamWithPatternPredicates() {

		final Supplier<Stream<String>> streamSupplier = () -> Stream.of("ABC_1", "АBC_2", "AВC_3", "ABС_4");
		final List<Pattern> patternList = Stream.of("\\W", "\\w+", "\\w\\W\\w_\\d").map(Pattern::compile)
				.collect(Collectors.toList());

		System.out.println("The items of the source list contain homoglyphs:");
		streamSupplier.get().forEach(str -> {
			System.out.printf("\"%s\" ► ", str);
			str.chars().forEach(ch -> System.out.printf(" '%s'(%4d)", Character.toString(ch), ch));
			System.out.println();
		});
		/*- 
		For 'asPredicate()' the string or any of its substrings should match the pattern. 
		It is like "s -> matcher(s).find()".
		
		For 'asMatchPredicate()' the entire string should match the pattern.
		It is like "s -> matcher(s).matches()".
		*/
		for (Pattern pattern : patternList) {
			final List<String> list1 = streamSupplier.get()/*-*/
					.collect(Collectors.filtering(pattern.asPredicate(), Collectors.toList()));
			System.out.printf("%nFiltered with      'asPredicate' pattern[%9s], result list %s%n", pattern, list1);

			final List<String> list2 = streamSupplier.get()/*-*/
					.collect(Collectors.filtering(pattern.asMatchPredicate(), Collectors.toList()));
			System.out.printf("Filtered with 'asMatchPredicate' pattern[%9s], result list %s%n", pattern, list2);
		}
		System.out.println("- ".repeat(50));
	}

	/**
	 * Replace all with matcher in single line.
	 * 
	 */
	private static void replaceAllWithMatcherInSingleLine() {

		final Pattern pattern = Pattern.compile("(?x) ^\\w+ | (?<=▼)\\w+ | \\w+(?=▲) | \\w+$");
		String text = "abc-defg-hij▲klmnop▼qrs-tuvw-xyz";
		System.out.printf("Text original[%s]%n", text);

		final Matcher matcher = pattern.matcher(text);
		final Function<MatchResult, String> replacer1 = mr -> mr.group().toUpperCase();
		text = matcher.replaceAll(replacer1);
		System.out.printf("Text replaced[%s]%n", text);

		matcher.reset(text);
		final Function<MatchResult, String> replacer2 = mr -> new StringBuilder(mr.group()).reverse().toString();
		text = matcher.replaceAll(replacer2);
		System.out.printf("Text replaced[%s]%n", text);
		System.out.println("- ".repeat(50));
	}

	/**
	 * Replace all with matcher in multiline.
	 * 
	 */
	private static void replaceAllWithMatcherInMultiline() {

		// Mon Jan 01 12:00:00 CET 2000
		final String multilineText = IntStream.rangeClosed(1, 3).boxed().map(arg -> new Date()).map(Date::toString)
				.collect(Collectors.joining(System.lineSeparator()));
		final List<Pattern> patternList = Arrays.asList(new String[] { /*-*/
				// 0. simple
				"((?:\\w+\\s?){3}) \\s ((?:\\d+:?){3}) .+ (\\d{4})", /*-*/
				// 1. with lookbehind and lookahead
				"(?<=:\\d)(?:([012]) | ([3456]) | ([789]))(?=\\s)", /*-*/
				// 2. with linebreak sequence in the capturing group
				"(\\p{Alpha}{3}) \\s (\\p{Digit}{4} \\R \\p{Alpha}{3}) \\s (\\p{Alpha}{3})", /*-*/
				// 3. dotall mode and back references
				"(?s) (\\p{Lu}\\p{L}{2}) .+ (\\p{Lu}\\p{L}{2}) . \\1 .+ (\\2 . \\1)", /*-*/
				// 4. nested capturing groups
				"   (((\\d{2}) : \\d{2}) : \\d{2})   ", /*-*/
				// 5. like pattern 4. above but only capturing groups
				".* (((\\d{2}) : \\d{2}) : \\d{2}) .*", /*-*/
		}).stream().map(arg -> Pattern.compile(arg, Pattern.COMMENTS)).collect(Collectors.toList());

		for (int i = 0; i < patternList.size(); i++) {
			System.out.printf("i[%d], pattern[%s]%n", i, patternList.get(i));
			final Matcher matcher = patternList.get(i).matcher(multilineText);
			int j = 1;
			while (matcher.find()) {
				System.out.printf("i[%d], match[%d], start index[%2d], end index[%2d]:%n", i, j++, matcher.start(),
						matcher.end());
				for (int k = 1; k <= matcher.groupCount(); k++) {
					System.out.printf("\tgroup(%d)[%s]%n", k, matcher.group(k));
				}
			}
			System.out.printf("i[%d], multiline text before replacement:%n%s%n", i, multilineText);
			System.out.printf("i[%d], multiline text after  replacement:%n%s%n", i, matcher.replaceAll("[$1][$2][$3]"));
			System.out.println("- ".repeat(50));
		}
	}

	/**
	 * Append replacement with matcher.
	 * 
	 */
	private static void appendReplacementWithMatcher() {

		final String text = "AAA=pat1=BBB=pat2=CCC";
		System.out.println(text);
		final StringBuffer strBuf = new StringBuffer();

		final Pattern pattern = Pattern.compile("=pat.=");
		final Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			System.out.printf("► matcher.group()[%s]%n", matcher.group());
			matcher.appendReplacement(strBuf, "-REPL-");
			System.out.println(strBuf.toString());
		}
		matcher.appendTail(strBuf);
		System.out.println(strBuf.toString());
		System.out.println("- ".repeat(50));
	}

	/**
	 * The split tokenizing. Splits the string around matches.<BR>
	 * <BR>
	 * The 'StringTokenizer' class is a legacy!
	 */
	private static void splitTokenizing() {

		final String textForSplit = "aBBBcB";
		final Pattern pattern = Pattern.compile("B");
		System.out.printf("Text for split[%s], regex[%s]%n", textForSplit, pattern.pattern());
		System.out.println("|1|2|3|4|5|");
		System.out.println("| B B B B |");
		System.out.println("|a|_|_|c|_|\n");

		// split(regex) <- method works as if by invoking split(regex, 0)
		for (int limit = 0; limit < 8; limit++) {
			final String[] tokens = pattern.split(textForSplit, limit);
			System.out.printf("limit[%d], tokens:", limit);
			for (String token : tokens) {
				System.out.printf(" [%s]", token);
			}
			System.out.println();
		}
		System.out.println("- ".repeat(50));
	}

	/**
	 * The scanner tokenizing.
	 * 
	 */
	private static void scannerTokenizing() {

		// Mon Jan 01 12:00:00 CET 2000
		final String multilineText = IntStream.rangeClosed(1, 3).boxed().map(arg -> new Date()).map(Date::toString)
				.collect(Collectors.joining(System.lineSeparator()));
		System.out.printf("Multiline text for scanners:%n%s%n%n", multilineText);

		final String delimiter1 = " \\p{L}+\\s";// "\\s\\p{L}+\\s" gives different result
		System.out.printf("Scanner 'next', delimiter[%s]:%n", delimiter1);
		try (Scanner scanner = new Scanner(multilineText)) {
			scanner.useDelimiter(delimiter1);
			int i = 1;
			while (scanner.hasNext()) {
				System.out.printf("(%d)[%s]%n", i, scanner.next());
				i++;
			}
		}
		System.out.println();

		final String delimiter2 = "\\s|:";
		System.out.printf("Scanner 'next', delimiter[%s], first line 'non integer', second line 'integer':%n",
				delimiter2);
		try (Scanner scanner = new Scanner(multilineText)) {
			scanner.useDelimiter(delimiter2);
			int i = 1;
			StringBuilder strBldI = new StringBuilder(), strBldA = new StringBuilder();
			while (scanner.hasNext()) {
				if (scanner.hasNextInt()) {
					strBldI.append(String.format("(%d)[%s] ", i, scanner.next()));
				} else {
					strBldA.append(String.format("(%d)[%s] ", i, scanner.next()));
				}
				i++;
			}
			System.out.printf("%s%n%s%n", strBldA.toString(), strBldI.toString());
		}
		System.out.println();

		final String delimiter3 = "\\s+|:|\\d+";
		final Set<String> tokenSet;
		try (Scanner scanner = new Scanner(multilineText)) {
			tokenSet = scanner/*-*/
					.useDelimiter(delimiter3)/*-*/
					.tokens()/*-*/
					.filter(Predicate.not(String::isEmpty))/*-*/
					.collect(Collectors.toSet());
		}
		System.out.printf("Scanner 'tokens', delimiter[%s], tokenSet%s%n%n", delimiter3, tokenSet);

		// with '\Z' delimiter it reads in the entire file at once
		final String delimiter4 = "\\Z";
		final Pattern pattern1 = Pattern.compile(".*Example Domain.*");
		try {
			final URL url = new URL("http://example.org/");
			System.out.printf("Scanner 'findAll', delimiter[%s], regex pattern[%s], url[%s]:%n", delimiter4,
					pattern1.pattern(), url.toExternalForm());
			final URLConnection urlConnection = url.openConnection();
			try (Scanner scanner = new Scanner(urlConnection.getInputStream())) {
				scanner.useDelimiter(delimiter4)/*-*/
						.findAll(pattern1)/*-*/
						.map(MatchResult::group)/*-*/
						.forEach(System.out::println);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
		System.out.println();

		final Pattern pattern2 = Pattern.compile("(?x) ((?:\\w+\\s?){3}) \\s ((?:\\d+:?){3}) .+ (\\d{4})");
		System.out.printf("Scanner 'findInLine', regex pattern[%s]:%n", pattern2.pattern());
		try (Scanner scanner = new Scanner(multilineText)) {
			scanner.findInLine(pattern2);
			final MatchResult matchResult = scanner.match();
			for (int i = 1; i <= matchResult.groupCount(); i++) {
				System.out.printf("(%d)[%s]%n", i, matchResult.group(i));
			}
		}
		System.out.println("- ".repeat(50));
	}
}
