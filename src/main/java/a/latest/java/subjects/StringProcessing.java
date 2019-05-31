package a.latest.java.subjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The processing of the string.
 * 
 */
public class StringProcessing {

	/**
	 * Counts the letter frequency.
	 * 
	 */
	public static void countLetterFrequency() {

		final Map<String, Integer> map = new HashMap<>();
		Stream.of("abcdebcdc".split("")).forEach(ch -> map.merge(ch, 1, Integer::sum));
		System.out.printf("Letter frequency map%s%n", map);
		System.out.println("- ".repeat(50));
	}

	/**
	 * Uses the string joiners.
	 * 
	 */
	public static void useStringJoiners() {

		final Set<String> set = Set.of("R", "S", "T");
		System.out.printf("With 'join':  set[%s]%n", String.join(" ~ ", set));

		final StringJoiner joinerForSet = new StringJoiner(", ", "Joiner        set[", "]");
		set.stream().forEach(joinerForSet::add);
		System.out.println(joinerForSet);

		final StringJoiner joinerForSortedSet = new StringJoiner(", ", "Joiner sorted set[", "]");
		final Set<String> sortedSet = set.stream().sorted().collect(Collectors.toSet());
		sortedSet.forEach(joinerForSortedSet::add);
		System.out.println(joinerForSortedSet);

		final StringJoiner joinerForList = new StringJoiner(", ", "Joiner       list[", "]");
		List.of("K", "L", "M").stream().forEach(joinerForList::add);
		System.out.println(joinerForList);

		final StringJoiner mergeJoiner = new StringJoiner(", ", "Joiners     merge[", "]");
		System.out.println(mergeJoiner.merge(joinerForSortedSet).merge(joinerForList));
		System.out.println("- ".repeat(50));
	}

	/**
	 * Gets results with the string producer.
	 * 
	 */
	public static void getResultsWithStringProducer() {

		final StringBuffer stringBuffer = new StringBuffer();
		final List<String> list = new ArrayList<>();
		final Map<String, String> map = new TreeMap<>();
		final List<Consumer<String>> consumerList = List.of(System.out::print, stringBuffer::append, list::add);

		produceStrings(consumerList, map::put);
		System.out.println();
		System.out.println(stringBuffer);
		System.out.println(list);
		System.out.println(map);
		System.out.println("- ".repeat(50));
	}

	/**
	 * Produces the strings.
	 * 
	 * @param consumerList the consumer list
	 * @param consumer     the consumer
	 */
	private static void produceStrings(List<Consumer<String>> consumerList, BiConsumer<String, String> consumer) {

		Arrays.asList("ABC".split("")).stream().forEach(consumerList.get(0)::accept);
		Arrays.asList("KLM".split("")).stream().forEach(consumerList.get(1)::accept);
		Arrays.asList("XYZ".split("")).stream().forEach(consumerList.get(2)::accept);
		final Consumer<String> action = arg -> consumer.accept(arg.substring(0, arg.indexOf('=')),
				arg.substring(arg.indexOf('=') + 1));
		Arrays.asList("abc=ABC,klm=KLM,xyz=XYZ".split(",")).stream().forEach(action);
	}

}
