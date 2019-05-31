package a.latest.java.subjects;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * The fragmentation of the stream.
 *
 */
public interface StreamFragmentation {

	/**
	 * Skips and limits the stream.
	 * 
	 */
	public static void skipAndLimit() {

		final Supplier<Stream<String>> supplier = () -> IntStream.rangeClosed(1, 10)/*-*/
				.mapToObj(Integer::toString)/*-*/
				.collect(Collectors.toList())/*-*/
				.stream();
		System.out.printf("Processor list %s%n", supplier.get().collect(Collectors.toList()));

		final List<String> beginList = supplier.get()/*-*/
				.skip(0).limit(3)/*-*/
				.collect(Collectors.toList());
		System.out.printf("Begin list  %s%n", beginList);

		final List<String> middleList = supplier.get()/*-*/
				.skip(3).limit(4)/*-*/
				.collect(Collectors.toList());
		System.out.printf("Middle list          %s%n", middleList);

		final List<String> endList = supplier.get()/*-*/
				.skip(7).limit(3)/*-*/
				.collect(Collectors.toList());
		System.out.printf("End list                         %s%n", endList);
		System.out.println("- ".repeat(50));
	}

	/**
	 * Drops and takes the stream.
	 * 
	 */
	public static void dropAndTake() {

		final String[] textArray = { "a", "b", "c", "dd", "e", "f", "gg", "h", "i" };
		System.out.printf("Text array %s%n", Arrays.asList(textArray));
		final Supplier<Stream<String>> supplier = () -> Stream.of(textArray);

		Predicate<String> predicate = arg -> arg.length() == 1;
		final List<String> beginList = supplier.get()/*-*/
				.takeWhile(predicate)/*-*/
				.collect(Collectors.toList());
		System.out.printf("Begin list %s%n", beginList);

		final List<String> restList = supplier.get()/*-*/
				.dropWhile(predicate)/*-*/
				.collect(Collectors.toList());
		System.out.printf("Rest list           %s%n", restList);

		final List<String> middleList = supplier.get()/*-*/
				.dropWhile(predicate).skip(1)/*-*/
				.takeWhile(predicate)/*-*/
				.collect(Collectors.toList());
		System.out.printf("Middle list             %s%n", middleList);

		final List<String> endList = supplier.get()/*-*/
				.dropWhile(predicate).skip(1)/*-*/
				.dropWhile(predicate).skip(1)/*-*/
				.takeWhile(predicate)/*-*/
				.collect(Collectors.toList());
		System.out.printf("End list                          %s%n", endList);
		System.out.println("- ".repeat(50));
	}

	/**
	 * Filters the stream.
	 */
	public static void filter() {

		final String[] textArray = { null, "", "a", "b", "A", "B" };
		System.out.printf("Text array\t\t\t%s%n", Arrays.asList(textArray));
		final Supplier<Stream<String>> supplier = () -> Stream.of(textArray);

		final List<String> notNullOrEmptyList = supplier.get()/*-*/
				.filter(Objects::nonNull)/*-*/
				.filter(Predicate.not(String::isEmpty))/*-*/
				.collect(Collectors.toList());
		System.out.printf("Not null or empty element list\t\t%s%n", notNullOrEmptyList);

		final List<String> notAList = supplier.get()/*-*/
				.filter(Objects::nonNull)/*-*/
				.filter(Predicate.not(String::isEmpty))/*-*/
				.filter(Predicate.not("a"::equalsIgnoreCase))/*-*/
				.collect(Collectors.toList());
		System.out.printf("Not 'A' element list\t\t\t   %s%n", notAList);
		System.out.println("- ".repeat(50));
	}
}