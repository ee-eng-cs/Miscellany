package a.latest.java.subjects;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import kp.utils.Utils;

/**
 * Parallelism.<br>
 * <a href=
 * "https://docs.oracle.com/javase/tutorial/collections/streams/parallelism.html">Parallelism</a>
 */
public class Parallelism {

	/**
	 * Joined streams.
	 * 
	 */
	public static void joinedStreams() {

		final Function<String, String> mapper = arg -> String.valueOf(Character.toChars(arg.codePointAt(0) + 3));

		final var srcList = Arrays.asList("ABC".split(""));
		System.out.printf("*** source list ***%n%s%n", srcList);

		/* Conclusion: elapsed times are quite random. */
		if (true) {
			new Parallelism().joinedStreamsParallelSingleCollector(srcList, mapper);
			new Parallelism().joinedStreamsParallelRepeatedCollecting(srcList, mapper);
			System.out.println();
		}
		for (int i = 0; i < 3; i++) {
			new Parallelism().joinedStreamsSequentialRepeatedCollecting(srcList, mapper);
			new Parallelism().joinedStreamsParallelRepeatedCollecting(srcList, mapper);
		}
		System.out.println("- ".repeat(50));
	}

	/**
	 * Joined streams with parallel repeated collecting.
	 * 
	 * @param srcList the source list
	 * @param mapper  the mapper
	 */
	private void joinedStreamsParallelRepeatedCollecting(List<String> srcList, Function<String, String> mapper) {

		final StringBuffer strBuf = new StringBuffer();
		final BiConsumer<String, Integer> reporter = (arg1, arg2) -> strBuf.append(String.format("%s%s ", arg1, arg2));

		Instant start = Instant.now();
		final var streamP1 = srcList.stream().peek(arg -> reporter.accept(arg, 1));
		final var streamP2 = streamP1.parallel().map(mapper).collect(Collectors.toList()).stream()
				.peek(arg -> reporter.accept(arg, 2));
		final var streamP3 = streamP2.parallel().map(mapper).collect(Collectors.toList()).stream()
				.peek(arg -> reporter.accept(arg, 3));
		final var streamP4 = streamP3.parallel().map(mapper).collect(Collectors.toList()).stream()
				.peek(arg -> reporter.accept(arg, 4));
		final var streamP5 = streamP4.parallel().map(mapper).collect(Collectors.toList()).stream()
				.peek(arg -> reporter.accept(arg, 5));
		final var streamP6 = streamP5.parallel().map(mapper).collect(Collectors.toList()).stream()
				.peek(arg -> reporter.accept(arg, 6));
		final var streamP7 = streamP6.parallel().map(mapper).collect(Collectors.toList()).stream()
				.peek(arg -> reporter.accept(arg, 7));
		final var streamP8 = streamP7.parallel().map(mapper).peek(arg -> reporter.accept(arg, 8));
		final var trgListP = streamP8.collect(Collectors.toList());
		Instant finish = Instant.now();
		System.out.printf("*** parallel ***%n%s%n%s\t%s%n", strBuf, trgListP, Utils.formatElapsed(start, finish));
	}

	/**
	 * Joined streams with sequential repeated collecting.
	 * 
	 * @param srcList the source list
	 * @param mapper  the mapper
	 */
	private void joinedStreamsSequentialRepeatedCollecting(List<String> srcList, Function<String, String> mapper) {

		final StringBuffer strBuf = new StringBuffer();
		final BiConsumer<String, Integer> reporter = (arg1, arg2) -> strBuf.append(String.format("%s%s ", arg1, arg2));

		Instant start = Instant.now();
		final var stream1 = srcList.stream().peek(arg -> reporter.accept(arg, 1));
		final var stream2 = stream1.sequential().map(mapper).collect(Collectors.toList()).stream()
				.peek(arg -> reporter.accept(arg, 2));
		final var stream3 = stream2.sequential().map(mapper).collect(Collectors.toList()).stream()
				.peek(arg -> reporter.accept(arg, 3));
		final var stream4 = stream3.sequential().map(mapper).collect(Collectors.toList()).stream()
				.peek(arg -> reporter.accept(arg, 4));
		final var stream5 = stream4.sequential().map(mapper).collect(Collectors.toList()).stream()
				.peek(arg -> reporter.accept(arg, 5));
		final var stream6 = stream5.sequential().map(mapper).collect(Collectors.toList()).stream()
				.peek(arg -> reporter.accept(arg, 6));
		final var stream7 = stream6.sequential().map(mapper).collect(Collectors.toList()).stream()
				.peek(arg -> reporter.accept(arg, 7));
		final var stream8 = stream7.sequential().map(mapper).peek(arg -> reporter.accept(arg, 8));
		final var trgList = stream8.collect(Collectors.toList());
		Instant finish = Instant.now();
		System.out.printf("*** sequential ***%n%s%n%s\t%s%n", strBuf, trgList, Utils.formatElapsed(start, finish));
	}

	/**
	 * Joined streams with parallel single collector.
	 * 
	 * @param srcList the source list
	 * @param mapper  the mapper
	 */
	private void joinedStreamsParallelSingleCollector(List<String> srcList, Function<String, String> mapper) {

		final StringBuffer strBuf = new StringBuffer();
		final BiConsumer<String, Integer> reporter = (arg1, arg2) -> strBuf.append(String.format("%s%s ", arg1, arg2));

		final Instant start = Instant.now();
		final var streamP1 = srcList.stream().peek(arg -> reporter.accept(arg, 1));
		final var streamP2 = streamP1.parallel().map(mapper).peek(arg -> reporter.accept(arg, 2));
		final var streamP3 = streamP2.parallel().map(mapper).peek(arg -> reporter.accept(arg, 3));
		final var streamP4 = streamP3.parallel().map(mapper).peek(arg -> reporter.accept(arg, 4));
		final var streamP5 = streamP4.parallel().map(mapper).peek(arg -> reporter.accept(arg, 5));
		final var streamP6 = streamP5.parallel().map(mapper).peek(arg -> reporter.accept(arg, 6));
		final var streamP7 = streamP6.parallel().map(mapper).peek(arg -> reporter.accept(arg, 7));
		final var streamP8 = streamP7.parallel().map(mapper).peek(arg -> reporter.accept(arg, 8));
		final var trgListP = streamP8.collect(Collectors.toList());
		final Instant finish = Instant.now();
		System.out.printf("*** parallel single collector ***%n%s%n%s\t%s%n", strBuf, trgListP,
				Utils.formatElapsed(start, finish));
	}
}