package a.latest.java.subjects;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * The collecting of the stream.
 *
 */
public interface StreamCollecting {

	/**
	 * Peeks in stream and collect.
	 * 
	 */
	public static void peekInStreamAndCollect() {

		// the supplier of a sorted stream with peek
		final Supplier<Stream<String>> supplier = () -> List.of("DBCADBCA".split("")).stream()/*-*/
				.sorted()/*-*/
				.peek(System.out::print);

		// An implementation may choose to not execute the stream pipeline,
		// if it is capable of computing the count directly from the stream source.
		System.out.printf("count[%d]%n", supplier.get().count());

		final Stream<String> concatenatedStreams = Stream.concat(/*-*/
				Stream.concat(/*-*/
						List.of("01,02,03".split(",")).stream().peek(System.out::print), /*-*/
						List.of("03,04,05".split(",")).stream().peek(System.out::print)), /*-*/
				Stream.concat(/*-*/
						List.of("06,07,08".split(",")).stream().peek(System.out::print), /*-*/
						List.of("09,10,11".split(",")).stream().peek(System.out::print)) /*-*/
		);
		System.out.printf("concat count[%d]%n", concatenatedStreams.count());
		System.out.println("After 'Stream.count()' calls. There was no output from 'peek' call above.\n");

		// equivalent to: reducing(0L, e -> 1L, Long::sum)
		System.out.printf("%nCounting[%s]%n", supplier.get()/*-*/
				.collect(Collectors.counting()));
		System.out.printf("%nJoining [%s]%n", supplier.get()/*-*/
				.collect(Collectors.joining("✖", "►", "◄")));
		System.out.printf("%nReducing[%s]%n", supplier.get()/*-*/
				.reduce(String::concat).get());
		System.out.printf("%nSet     %s%n", supplier.get()/*-*/
				.collect(Collectors.toSet()));
		System.out.printf("%nList    %s%n", supplier.get()/*-*/
				.collect(Collectors.toList()));
		System.out.printf("%nList    %s (from distinct stream)%n", supplier.get()/*-*/
				.distinct().collect(Collectors.toList()));

		System.out.printf("%nStream has any 'B' element[%b] and has no 'C' elements[%b]%n",
				supplier.get().anyMatch("B"::equals), supplier.get().noneMatch("C"::equals));
		System.out.println("- ".repeat(50));
	}

	/**
	 * Groups elements by difference from the expected value '50'.
	 * 
	 */
	public static void groupByDifferenceFromExpected() {

		final int EXPECTED = 50;
		final Supplier<Stream<Integer>> supplier = () -> IntStream.rangeClosed(40, 60).boxed();
		final Function<Integer, Character> classifier = number -> {
			final int delta = number - EXPECTED;
			if (delta < -5) {
				return 'A';
			} else if (delta < -1) {
				return 'B';
			} else if (delta <= 1) {
				return 'C';
			} else if (delta <= 5) {
				return 'D';
			} else {
				return 'E';
			}
		};
		final Map<Character, List<Integer>> resultMap = supplier.get()/*-*/
				.collect(Collectors.groupingBy(classifier));
		System.out.printf("Map of grouped elements:%n   %s%n", resultMap);

		final Map<Character, Long> numberMap = supplier.get()/*-*/
				.collect(Collectors.groupingBy(classifier, Collectors.counting()));
		System.out.printf("Map of the number of elements in groups: %s%n", numberMap);

		final Map<Boolean, List<Integer>> partitionedMap = supplier.get()/*-*/
				.collect(Collectors.partitioningBy(arg -> EXPECTED - 1 <= arg && arg <= EXPECTED + 1));
		System.out.printf("Map of partitioned elements:%n   %s%n", partitionedMap);
		System.out.println("The elements with key 'true' have the value 'expected' +- 1.");

		resultMap.replaceAll((key, numList) -> {
			numList.replaceAll(num -> num - EXPECTED);
			return numList;
		});
		System.out.printf("Map of grouped differences from the value 'expected':%n   %s%n", resultMap);
		System.out.println("- ".repeat(50));
	}

	/**
	 * The source list is modified before the terminal collect.
	 * 
	 */
	public static void sourceListModifiedBeforeTheTerminalCollect() {

		final List<String> srcList = new ArrayList<>(Arrays.asList("aaaaaaaa", "bbbbbbbb", "cccccccc"));
		System.out.printf("source list   %s%n", srcList);
		final Stream<String> stream1 = srcList.stream();
		final Stream<String> stream2 = srcList.stream();

		final List<String> trgList1 = stream1.collect(Collectors.toList());
		System.out.printf("target list 1 %s%n", trgList1);

		// the change in the source list modifies the stream2 defined above
		int index = srcList.indexOf("bbbbbbbb");
		srcList.remove(index);
		srcList.add(index, "REPLACED");
		final List<String> trgList2 = stream2.collect(Collectors.toList());

		// the change in the source list does not modify the target list
		index = srcList.indexOf("cccccccc");
		srcList.remove(index);
		srcList.add(index, "REPLACED");

		System.out.printf("target list 2 %s%n", trgList2);
		System.out.printf("source list   %s%n", srcList);
		System.out.println("- ".repeat(50));
	}

	/**
	 * The stream of optionals.
	 * 
	 */
	public static void streamOfOptionals() {

		System.out.println("Mapping the stream element from 'null' to optional 'empty':");
		final List<String> srcList1 = new ArrayList<>();
		srcList1.add("a");
		srcList1.add(null);
		srcList1.add("b");
		System.out.printf("Source list 1: size[%d], list%s%n", srcList1.size(), srcList1);

		System.out.println("Stream peek:");
		final List<String> trgList = srcList1.stream()/*-*/
				.map(Optional::ofNullable)/*-*/
				.peek(System.out::println)/*-*/
				.collect(Collectors.flatMapping(Optional::stream, Collectors.toList()));
		System.out.printf("Target list 1: size[%d], list%s%n%n", trgList.size(), trgList);

		System.out.println("Mapping the stream element from optional 'empty' to 'FIXED':");
		final List<Optional<String>> srcList2 = srcList1.stream()/*-*/
				.map(Optional::ofNullable)/*-*/
				.collect(Collectors.toList());
		System.out.printf("Source list 2: size[%d], list%s%n", srcList2.size(), srcList2);

		System.out.println("Stream peek:");
		final Consumer<String> notEmptyAction = arg -> System.out.printf("«found not empty element[%s]»%n", arg);
		final Runnable emptyAction = () -> System.out.println("«found empty element»");
		final Consumer<Optional<String>> peekAction = optional -> optional.ifPresentOrElse(notEmptyAction, emptyAction);

		final List<String> trgList2 = srcList2.stream()/*-*/
				.peek(peekAction)/*-*/
				.map(arg -> arg.or(() -> Optional.of("FIXED")))/*-*/
				.collect(Collectors.flatMapping(Optional::stream, Collectors.toList()));
		System.out.printf("Target list 2: size[%d], list%s%n", trgList2.size(), trgList2);
		System.out.println("- ".repeat(50));
	}

	/**
	 * Creates stream from nullable element and concatenates it.
	 * 
	 */
	public static void createStreamFromNull() {

		final Supplier<Stream<String>> supplierB = () -> Stream.of("A", "B");
		final Supplier<Stream<String>> supplierE = () -> Stream.of("D", "E");
		for (String nullableElement : new String[] { "C", null }) {
			final Stream<String> singleElementStream = Stream.ofNullable(nullableElement);
			final Stream<String> stream = Stream.concat(/*-*/
					Stream.concat(supplierB.get(), singleElementStream), /*-*/
					supplierE.get());
			System.out.printf("Concatenated with stream from nullable element[%4s], result%s%n", nullableElement,
					stream.collect(Collectors.toList()));
		}
		System.out.println("- ".repeat(50));
	}

	/**
	 * The iterative stream.
	 * 
	 */
	public static void iterativeStream() {

		/*
		 * Integer
		 */
		final int seedInt = 1;
		final IntPredicate hasNextInt = num -> num <= 3;
		final IntUnaryOperator nextInt = num -> ++num;
		final List<Integer> intList = IntStream.iterate(seedInt, hasNextInt, nextInt)/*-*/
				.boxed()/*-*/
				.collect(Collectors.toList());
		System.out.printf("result number list%s%n", intList);

		/*
		 * String
		 */
		final String seedString = "a";
		final Predicate<String> hasNextString = str -> str.codePointAt(0) < "a".codePointAt(0) + 3;
		final UnaryOperator<String> nextString = str -> Character.toString(str.codePointAt(0) + 1);
		final List<String> stringList = Stream.iterate(seedString, hasNextString, nextString)/*-*/
				.collect(Collectors.toList());
		System.out.printf("result string list%s%n", stringList);
		/*
		 * Array
		 */
		final String[] seedArray = new String[] { "a" };
		final Predicate<String[]> hasNextArray = arr -> arr.length <= 3;
		final UnaryOperator<String[]> nextArray = arr -> {
			arr = Arrays.copyOf(arr, arr.length + 1);
			arr[arr.length - 1] = Character.toString(arr[arr.length - 2].codePointAt(0) + 1);
			return arr;
		};
		final List<String[]> arrayList = Stream.iterate(seedArray, hasNextArray, nextArray)/*-*/
				.collect(Collectors.toList());
		System.out.printf("result array list%s%n", Arrays.deepToString(arrayList.toArray(Object[]::new)));
		/*
		 * List
		 */
		final List<String> seedList = new ArrayList<>();
		seedList.add("a");
		final Predicate<List<String>> hasNextList = list -> list.size() <= 3;
		final UnaryOperator<List<String>> nextList = list -> {
			final List<String> resultList = new ArrayList<>();
			resultList.addAll(list);
			resultList.add(Character.toString(resultList.get(resultList.size() - 1).codePointAt(0) + 1));
			return resultList;
		};
		final List<List<String>> listList = Stream.iterate(seedList, hasNextList, nextList)/*-*/
				.collect(Collectors.toList());
		System.out.printf("result list  list%s%n", listList);
		/*
		 * Map
		 */
		final Map<String, String> seedMap = new TreeMap<>();
		seedMap.put("a", "A");
		final Predicate<Map<String, String>> hasNextMap = map -> map.size() <= 3;
		final UnaryOperator<Map<String, String>> nextMap = map -> {
			final String key = map.keySet().toArray(String[]::new)[map.size() - 1];
			final String value = map.get(key);
			final Map<String, String> resultMap = new TreeMap<>();
			resultMap.putAll(map);
			resultMap.put(Character.toString(key.codePointAt(0) + 1), Character.toString(value.codePointAt(0) + 1));
			return resultMap;
		};
		final List<Map<String, String>> mapList = Stream.iterate(seedMap, hasNextMap, nextMap)/*-*/
				.collect(Collectors.toList());
		System.out.printf("result map list%s%n", mapList);
		System.out.println("- ".repeat(50));
	}

	/**
	 * Traverses thread-safe variant of list.
	 * 
	 */
	public static void traverseList() {

		final Supplier<Stream<String>> streamSupplier = () -> Stream.of("E", "X", "A", "M", "P", "L", "E");
		final StringBuilder strBld1 = new StringBuilder();
		streamSupplier.get().forEach(strBld1::append);
		System.out.println("Traverse list:");
		System.out.printf("Supplied source \t\t  [%s]%n", strBld1.toString());

		// There are exceptions when THREAD_SAFE_LIST flag is false.
		final boolean THREAD_SAFE_LIST = true;
		final Supplier<List<String>> supplier = THREAD_SAFE_LIST ? CopyOnWriteArrayList::new : ArrayList::new;
		final List<String> list = streamSupplier.get().collect(supplier, List::add, List::addAll);

		final List<Iterator<String>> iteratorList = new ArrayList<>();
		iteratorList.add(list.iterator());
		list.removeIf("E"::equals);
		iteratorList.add(list.iterator());
		list.removeIf(Predicate.not("M"::equals));
		iteratorList.add(list.iterator());

		for (int i = 0; i < iteratorList.size(); i++) {
			final StringBuilder strBld2 = new StringBuilder();
			try {
				iteratorList.get(i).forEachRemaining(strBld2::append);
				System.out.printf("List traversed with iterator[%d] : [%s]%n", i, strBld2.toString());
			} catch (ConcurrentModificationException e) {
				System.out.printf("Catched 'ConcurrentModificationException' from fail-fast iterator[%d]%n", i);
			}
		}
		System.out.printf("List  %s, list class name[%s]%n", list, list.getClass().getName());
		System.out.println("- ".repeat(50));
	}

	/**
	 * Traverses queue.
	 * 
	 */
	public static void traverseQueue() {

		final Supplier<Stream<String>> streamSupplier = () -> Stream.of("E", "X", "A", "M", "P", "L", "E");
		final StringBuilder strBld1 = new StringBuilder();
		streamSupplier.get().forEach(strBld1::append);
		System.out.println("Traverse queue:");
		System.out.printf("Supplied source [%s]%n", strBld1.toString());
		/*-
		 * For the 'Queue' interface, 'LinkedList' is the most commonly used implementation
		 */
		final Queue<String> queue1 = streamSupplier.get().collect(Collectors.toCollection(LinkedList::new));
		final StringBuilder strBld2 = new StringBuilder();
		queue1.stream().forEach(strBld2::append);
		System.out.printf("Queue traversed [%s]%n", strBld2.toString());

		final Queue<String> piorityQueue = streamSupplier.get().collect(Collectors.toCollection(PriorityQueue::new));
		final StringBuilder strBld3 = new StringBuilder();
		piorityQueue.stream().forEach(strBld3::append);
		System.out.printf("Queue traversed [%s] (priority queue)%n", strBld3.toString());
		final StringBuilder strBld4 = new StringBuilder();
		while (!piorityQueue.isEmpty()) {
			strBld4.append(piorityQueue.poll());
		}
		System.out.printf("                [%s] ◄- pooled sorted from priority queue%n", strBld4.toString());

		final boolean THREAD_SAFE_QUEUE = true;
		final Supplier<Queue<String>> supplier = THREAD_SAFE_QUEUE ? ConcurrentLinkedQueue::new : LinkedList::new;
		final Queue<String> queue2 = streamSupplier.get().collect(supplier, Queue::add, Queue::addAll);
		System.out.printf("Queue %s%n", queue2);
		System.out.println("- ".repeat(50));
	}

	/**
	 * Traverses deque.
	 * 
	 */
	public static void traverseDeque() {

		final Supplier<Stream<String>> streamSupplier = () -> Stream.of("E", "X", "A", "M", "P", "L", "E");
		final StringBuilder strBld1 = new StringBuilder();
		streamSupplier.get().forEach(strBld1::append);
		System.out.println("Traverse deque:");
		System.out.printf("Supplied source \t\t [%s]%n", strBld1.toString());
		/*-
		 * The 'ArrayDeque' class is the resizable array implementation of the 'Deque' interface,
		 * whereas the 'LinkedList' class is the list implementation.
		 * 
		 * In terms of efficiency, 'ArrayDeque' is more efficient than
		 * the 'LinkedList' for add and remove operation at both ends.
		 * The best operation in a 'LinkedList' implementation is
		 * removing the current element during the iteration.
		 * 'LinkedList' implementations are not ideal structures to iterate.
		 * 
		 * For the 'Deque' interface, 'ArrayDeque' is the most commonly used implementation.
		 */
		final Deque<String> deque1 = streamSupplier.get().collect(Collectors.toCollection(ArrayDeque::new));
		final StringBuilder strBld2 = new StringBuilder();
		// an iterator in proper sequence
		deque1.iterator().forEachRemaining(strBld2::append);
		System.out.printf("Deque traversed in proper  order [%s]%n", strBld2.toString());
		final StringBuilder strBld3 = new StringBuilder();
		// an iterator in reverse sequential order
		deque1.descendingIterator().forEachRemaining(strBld3::append);
		System.out.printf("Deque traversed in reverse order [%s]%n", strBld3.toString());

		final boolean THREAD_SAFE_DEQUE = true;
		final Supplier<Deque<String>> supplier = THREAD_SAFE_DEQUE ? ConcurrentLinkedDeque::new : ArrayDeque::new;
		final Deque<String> deque2 = streamSupplier.get().collect(supplier, Deque::add, Deque::addAll);
		System.out.printf("Deque%s%n", deque2);
		System.out.println("- ".repeat(50));
	}

	/**
	 * Changes from the multidimensional list to the flat list.
	 * 
	 */
	public static void multidimensionalToFlat() {

		final Byte[][][] byteArray = { { { 1, 2 }, { 3, 4 } }, { { 5, 6 }, { 7, 8 } } };
		System.out.printf("Byte array deep to string %s%n", Arrays.deepToString(byteArray));

		final Function<Byte[][], List<List<Byte>>> mapper = arr -> Stream.of(arr)/*-*/
				.map(Arrays::asList)/*-*/
				.collect(Collectors.toList());
		final List<List<List<Byte>>> multiDimByteList = Stream.of(byteArray)/*-*/
				.map(mapper)/*-*/
				.collect(Collectors.toList());
		System.out.printf("Multi dim byte list\t  %s%n", multiDimByteList);

		final List<Byte> byteFlatList = multiDimByteList.stream()/*-*/
				.flatMap(List::stream)/*-*/
				.flatMap(List::stream)/*-*/
				.collect(Collectors.toList());
		System.out.printf("Flat byte list\t\t  %s%n%n", byteFlatList);

		final List<List<List<String>>> multiDimStrList = List.of(/*-*/
				List.of(List.of("A", "B"), List.of("C", "D")), /*-*/
				List.of(List.of("E", "F"), List.of("G", "H")) /*-*/
		);
		System.out.printf("Multi dim list\t\t  %s%n", multiDimStrList);

		final StringBuilder strBld = new StringBuilder();
		final Consumer<String> stringCons = strBld::append;
		final Consumer<List<String>> listCons = list -> list.forEach(stringCons::accept);
		final Consumer<List<List<String>>> listOfListsCons = listOfLists -> listOfLists.forEach(listCons::accept);
		multiDimStrList.forEach(listOfListsCons);
		System.out.printf("Multi dim list joined with consumers\t[%s]%n", strBld.toString());

		final Supplier<Stream<String>> supplier = () -> multiDimStrList.stream()/*-*/
				.flatMap(List::stream)/*-*/
				.flatMap(List::stream);
		final List<String> flatStrList = supplier.get().collect(Collectors.toList());
		System.out.printf("Flat list\t\t  %s%n", flatStrList);

		System.out.printf("Flat list joined with collectors\t[%s]%n", supplier.get().collect(Collectors.joining()));
		System.out.println("- ".repeat(50));
	}

	/**
	 * The nulls in stream without optionals.
	 * 
	 */
	public static void nullsInStreamWithoutOptionals() {

		final Supplier<List<List<Integer>>> supplier = () -> {
			final List<List<Integer>> listOfLists = new ArrayList<>();
			for (int i = 1; i <= 3; i += 2) {
				final List<Integer> list = IntStream.rangeClosed(i, i + 1).boxed().collect(Collectors.toList());
				listOfLists.add(list);
			}
			return listOfLists;
		};
		for (int counter = 0; counter != -1;) {
			counter = nullsInStreamWithoutOptionals(supplier, counter);
		}
		System.out.println("- ".repeat(50));
	}

	/**
	 * The nulls in stream without optionals.
	 * 
	 * @param supplier the list supplier
	 * @param counter  the counter
	 * @return the counter
	 */
	private static int nullsInStreamWithoutOptionals(Supplier<List<List<Integer>>> supplier, int counter) {

		final List<List<Integer>> srcList = supplier.get();
		switch (++counter) {
		case 1:
			System.out.println("List unchanged:");
			break;
		case 2:
			srcList.remove(1);
			System.out.println("\nRemove the element on top dimension:");
			break;
		case 3:
			srcList.set(1, null);
			System.out.println("\nSet 'null' on top dimension:");
			break;
		case 4:
			srcList.get(1).remove(1);
			System.out.println("\nRemove the element on bottom dimension:");
			break;
		default:
			srcList.get(1).set(1, null);
			System.out.println("\nSet 'null' on bottom dimension:");
			counter = -1;
			break;
		}
		System.out.printf("Processor list size[%d], Processor list%s%n", srcList.size(), srcList);
		final List<Integer> trgList = srcList.stream()/*- */
				.filter(Objects::nonNull)/*- guards against NPE */
				.flatMap(List<Integer>::stream)/*- */
				.filter(Objects::nonNull)/*- guards against null element in list */
				.collect(Collectors.toList());
		System.out.printf("Target list size[%d], target list%s%n", trgList.size(), trgList);
		return counter;
	}

	/**
	 * The nulls in stream with optionals.
	 * 
	 */
	public static void nullsInStreamWithOptionals() {

		final Supplier<Optional<List<Optional<List<Optional<Integer>>>>>> supplier = () -> {
			Optional<List<Optional<List<Optional<Integer>>>>> optional = Optional.of(new ArrayList<>());
			for (int i = 1; i <= 3; i += 2) {
				final List<Optional<Integer>> list = IntStream.rangeClosed(i, i + 1).boxed()/*-*/
						.map(Optional::of).collect(Collectors.toList());
				optional.get().add(Optional.of(list));
			}
			return optional;
		};
		for (int counter = 0; counter != -1;) {
			counter = nullsInStreamWithOptionals(supplier, counter);
		}
		System.out.println("- ".repeat(50));
	}

	/**
	 * The nulls in stream with optionals.
	 * 
	 * @param supplier the optional supplier
	 * @param counter  the counter
	 * @return the counter
	 */
	private static int nullsInStreamWithOptionals(Supplier<Optional<List<Optional<List<Optional<Integer>>>>>> supplier,
			int counter) {

		Optional<List<Optional<List<Optional<Integer>>>>> srcListOptional = supplier.get();

		switch (++counter) {
		case 1:
			System.out.println("List of optionals unchanged:");
			break;
		case 2:
			srcListOptional = Optional.empty();
			System.out.println("\nSet 'empty' source list optional:");
			break;
		case 3:
			srcListOptional.get().remove(1);
			System.out.println("\nRemove the optional on top dimension:");
			break;
		case 4:
			srcListOptional.get().set(1, null);
			System.out.println("\nSet 'null' optional on top dimension:");
			break;
		case 5:
			srcListOptional.get().set(1, Optional.empty());
			System.out.println("\nSet 'empty' optional on top dimension:");
			break;
		case 6:
			srcListOptional.get().get(1).get().remove(1);
			System.out.println("\nRemove the optional on bottom dimension:");
			break;
		case 7:
			srcListOptional.get().get(1).get().set(1, null);
			System.out.println("\nSet 'null' optional on bottom dimension:");
			break;
		default:
			srcListOptional.get().get(1).get().set(1, Optional.empty());
			System.out.println("\nSet 'empty' optional on bottom dimension:");
			counter = -1;
			break;
		}
		if (srcListOptional.isEmpty()) {
			System.out.println("Processor list optional is 'empty'");
		} else {
			System.out.printf("Processor list size[%d], Processor list%s%n", srcListOptional.get().size(),
					srcListOptional.get());
		}
		final List<Integer> trgList = srcListOptional.stream()/*- */
				.flatMap(List::stream)/*- */
				.filter(Objects::nonNull)/*- guards against NPE (when 'null' on top dimension)*/
				.flatMap(Optional::stream)/*- */
				.flatMap(List::stream)/*- */
				.filter(Objects::nonNull)/*- guards against NPE (when 'null' on bottom dimension)*/
				.filter(Optional::isPresent)/*- guards against NoSuchElementException ('empty' optional on bottom dimension)*/
				.map(Optional::get)/*- */
				.collect(Collectors.toList());
		System.out.printf("Target list size[%d], target list%s%n", trgList.size(), trgList);
		return counter;
	}
}