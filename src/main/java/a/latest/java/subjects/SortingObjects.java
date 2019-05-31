package a.latest.java.subjects;

import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Sorting the objects.
 *
 */
public interface SortingObjects {

	/**
	 * Sorts a stream.
	 * 
	 */
	public static void sortStream() {

		final Supplier<Stream<String>> streamSupplier = () -> Stream.of("B-1", "A-2", "D-3", "C-4");
		System.out.println("Unsorted stream collected into:");
		final Set<String> hashSet = streamSupplier.get().collect(Collectors.toCollection(HashSet::new));
		System.out.printf(" HashSet       %s (unordered)%n", hashSet);

		final Set<String> linkedHashSet = streamSupplier.get().collect(Collectors.toCollection(LinkedHashSet::new));
		System.out.printf(" LinkedHashSet %s (insertion order)%n", linkedHashSet);

		final Set<String> treeSet = streamSupplier.get().collect(Collectors.toCollection(TreeSet::new));
		System.out.printf(" TreeSet       %s (natural order)%n", treeSet);

		final List<String> list1 = streamSupplier.get().collect(Collectors.toList());
		System.out.printf(" List          %s (insertion order)%n", list1);

		System.out.println("Natural order sorted stream collected into:");
		final List<String> list2 = streamSupplier.get().sorted().collect(Collectors.toList());
		System.out.printf(" List          %s (insertion order)%n", list2);

		System.out.println("Reverse order sorted stream collected into:");
		final List<String> list3 = streamSupplier.get()/*-*/
				.sorted(Collections.reverseOrder()).collect(Collectors.toList());
		System.out.printf(" List          %s (insertion order)%n%n", list3);

		System.out.printf("Before list1 sorting: list1 equals list2 [%b]%n", list1.equals(list2));
		list1.sort(null);
		System.out.printf("After  list1 sorting: list1 equals list2 [%b]%n", list1.equals(list2));
		System.out.println("- ".repeat(50));
	}

	/**
	 * Sorts a stream with the locale.
	 * 
	 */
	public static void sortStreamWithLocale() {

		final Supplier<Stream<String>> streamSupplier = () -> Stream.of("Z", "z", "Ä", "ä", "À", "à", "A", "a");
		System.out.println("Sort a stream with the 'Locale.FRANCE':");
		final List<String> listUns = streamSupplier.get().collect(Collectors.toList());
		System.out.printf("Collected unsorted stream \t\t\t\t%s%n", listUns);

		final List<String> listNat = streamSupplier.get().sorted().collect(Collectors.toList());
		System.out.printf("Collected sorted stream - natural order \t\t%s%n", listNat);

		final Collator collator = Collator.getInstance(Locale.FRANCE);
		collator.setStrength(Collator.PRIMARY);
		final List<String> listPri = streamSupplier.get().sorted(collator).collect(Collectors.toList());
		System.out.printf("Collected sorted stream - collator strength 'PRIMARY'   %s%n", listPri);
		collator.setStrength(Collator.SECONDARY);
		final List<String> listSec = streamSupplier.get().sorted(collator).collect(Collectors.toList());
		System.out.printf("Collected sorted stream - collator strength 'SECONDARY' %s%n", listSec);
		collator.setStrength(Collator.TERTIARY);
		final List<String> listTri = streamSupplier.get().sorted(collator).collect(Collectors.toList());
		System.out.printf("Collected sorted stream - collator strength 'TERTIARY'  %s%n", listTri);

		final Map<String, String> map = new TreeMap<>(collator);
		streamSupplier.get().forEach(arg -> map.put(arg, "example"));
		System.out.printf("'TreeMap' (created with 'TERTIARY' collator) - its keys %s%n", map.keySet());
		listUns.sort(collator);
		System.out.printf("List (unsorted stream) sorted with 'TERTIARY' collator  %s%n", listUns);
		System.out.println("- ".repeat(50));
	}

	/**
	 * Sorts an array.
	 * 
	 */
	public static void sortArray() {

		System.out.println("Sort array:");
		final String[] stringArrSrc = "def,abc,ghi".split(",");
		System.out.printf("source array %s%n", Arrays.asList(stringArrSrc));
		final String[] stringArr1 = Arrays.copyOf(stringArrSrc, stringArrSrc.length);
		final String[] stringArr2 = Arrays.copyOf(stringArrSrc, stringArrSrc.length);
		final String[] stringArr3 = Arrays.copyOf(stringArrSrc, stringArrSrc.length);
		final String[] stringArr4 = Arrays.copyOf(stringArrSrc, stringArrSrc.length);
		final String[] stringArr5 = Arrays.copyOf(stringArrSrc, stringArrSrc.length);

		Arrays.sort(stringArr1);// sort the array using the natural order
		Arrays.sort(stringArr2, String.CASE_INSENSITIVE_ORDER);
		Arrays.sort(stringArr3, String::compareToIgnoreCase);
		final Comparator<String> comparatorForStrings = String::compareToIgnoreCase;
		Arrays.sort(stringArr4, comparatorForStrings);
		final BiFunction<String, String, Integer> comparatorAsFunction = String::compareToIgnoreCase;
		Arrays.sort(stringArr5, comparatorAsFunction::apply);

		System.out.printf(
				"sorted array %s, 'String'  arr1 equals: arrSrc[%b], arr2[%b], arr3[%b], arr4[%b], arr5[%b]%n%n",
				Arrays.asList(stringArr1), Arrays.equals(stringArr1, stringArrSrc),
				Arrays.equals(stringArr1, stringArr2), Arrays.equals(stringArr1, stringArr3),
				Arrays.equals(stringArr1, stringArr4), Arrays.equals(stringArr1, stringArr5));

		final Integer[] integerArrSrc = { 456, 123, 789 };
		System.out.printf("source array %s%n", Arrays.asList(integerArrSrc));
		final Integer[] integerArr1 = Arrays.copyOf(integerArrSrc, integerArrSrc.length);
		final Integer[] integerArr2 = Arrays.copyOf(integerArrSrc, integerArrSrc.length);
		final Integer[] integerArr3 = Arrays.copyOf(integerArrSrc, integerArrSrc.length);
		final Integer[] integerArr4 = Arrays.copyOf(integerArrSrc, integerArrSrc.length);
		final Integer[] integerArr5 = Arrays.copyOf(integerArrSrc, integerArrSrc.length);

		Arrays.sort(integerArr1);// sort the array using the natural order
		Arrays.sort(integerArr2, Integer::compareTo);
		final Comparator<Integer> comparatorForIntegers = (first, second) ->
		/*-                                             */ first < second ? -1 : first > second ? 1 : 0;
		Arrays.sort(integerArr3, comparatorForIntegers);
		final BinaryOperator<Integer> comparatorAsOperator = (first, second) ->
		/*-                                             */ first < second ? -1 : first > second ? 1 : 0;
		Arrays.sort(integerArr4, comparatorAsOperator::apply);
		Arrays.sort(integerArr5, Collections.reverseOrder(Collections.reverseOrder()));// reversing the reverse order
		System.out.printf(
				"sorted array %s, 'Integer' arr1 equals: arrSrc[%b], arr2[%b], arr3[%b], arr4[%b], arr5[%b]%n",
				Arrays.asList(integerArr1), Arrays.equals(integerArr1, integerArrSrc),
				Arrays.equals(integerArr1, integerArr2), Arrays.equals(integerArr1, integerArr3),
				Arrays.equals(integerArr1, integerArr4), Arrays.equals(integerArr1, integerArr5));
		System.out.println("- ".repeat(50));
	}

	/**
	 * Sorts a map by the key or by the value.
	 * 
	 */
	public static void sortMapByKeyOrByValue() {

		System.out.println("Sort map:");
		final Map<String, String> map = Map.ofEntries(Map.entry("kB", "vC"), Map.entry("kC", "vA"),
				Map.entry("kA", "vB"));
		System.out.printf("Processor map \t\t%s%n", map);

		final Map<String, Comparator<? super Entry<String, String>>> comparatorMap = new TreeMap<>();
		comparatorMap.put("by key", Map.Entry.comparingByKey());
		comparatorMap.put("by key reverse", Collections.reverseOrder(Map.Entry.comparingByKey()));
		comparatorMap.put("by value", Map.Entry.comparingByValue());
		comparatorMap.put("by value reverse", Collections.reverseOrder(Map.Entry.comparingByValue()));

		for (String key : comparatorMap.keySet()) {
			final Map<String, String> sortedMap = new LinkedHashMap<>();
			final Consumer<? super Entry<String, String>> action = /*-*/
					entry -> sortedMap.put(entry.getKey(), entry.getValue());
			map.entrySet().stream()/*-*/
					.sorted(comparatorMap.get(key))/*-*/
					.forEachOrdered(action);
			System.out.printf("Sorted %-16s %s%n", key, sortedMap);
		}
		System.out.println("- ".repeat(50));
	}

	/**
	 * Sorts with the comparable and the comparator.
	 * 
	 */
	public static void sortWithComparableAndComparator() {

		System.out.println("Sort with comparable and comparator:");
		final List<ValueObject> listSrc = IntStream.iterate(ValueObject.ITEMS, i -> i > 0, i -> --i)
				.mapToObj(ValueObject::new).collect(Collectors.toList());
		System.out.printf("        source %s%n", listSrc);

		final Set<ValueObject> set = listSrc.stream().collect(Collectors.toCollection(TreeSet::new));
		System.out.printf("      tree set %s%n", set);

		final List<ValueObject> list1 = listSrc.stream().sorted().collect(Collectors.toList());
		System.out.printf("       lists 1 %s (natural order)%n", list1);
		final Comparator<ValueObject> compMinInCen = Comparator.comparing(ValueObject::getValueForMinimumInCenter);
		final List<ValueObject> list2 = listSrc.stream().sorted(compMinInCen).collect(Collectors.toList());
		System.out.printf("       lists 2 %s (ascending from center)%n", list2);
		final Comparator<ValueObject> compEvnOddPairs = Comparator.comparing(ValueObject::getValueForEvenAndOdd);
		final List<ValueObject> list3 = listSrc.stream().sorted(compEvnOddPairs).collect(Collectors.toList());
		System.out.printf("       lists 3 %s (even & odd pairs ascending)%n", list3);
		// chain of comparators
		final Comparator<ValueObject> compFirstEvnThenOdd = Comparator.comparing(ValueObject::getValueForMod2)
				.thenComparing(Comparator.naturalOrder());
		final List<ValueObject> list4 = listSrc.stream().sorted(compFirstEvnThenOdd).collect(Collectors.toList());
		System.out.printf("       lists 4 %s (first even ascending then odd ascending)%n", list4);
		System.out.println("- ".repeat(50));
	}
}

/**
 * The value object.
 * 
 */
class ValueObject extends Object implements Comparable<ValueObject> {
	/**
	 * The items.
	 */
	static final int ITEMS = 8;
	/**
	 * The value.
	 */
	private final int value;

	/**
	 * Constructor.
	 * 
	 * @param value the value
	 */
	ValueObject(int value) {
		super();
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param arg the argument
	 * @return the result
	 */
	@Override
	public int compareTo(ValueObject arg) {
		return this.value < arg.value ? -1 : this.value > arg.value ? 1 : 0;
	}

	/**
	 * Gets the value for minimum in center.
	 * 
	 * @return the result
	 */
	int getValueForMinimumInCenter() {
		return value < (ITEMS / 2) + 1 ? value : -value;
	}

	/**
	 * Gets the value for even and odd.
	 * 
	 * @return the result
	 */
	int getValueForEvenAndOdd() {
		return value / 2 + value % 2;
	}

	/**
	 * Gets the value for modulo 2.
	 * 
	 * @return the result
	 */
	int getValueForMod2() {
		return -value % 2;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return the result
	 */
	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
