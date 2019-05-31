package a.latest.java.subjects;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Array and collection.
 *
 */
public interface ArrayAndCollection {

	/**
	 * Shows the arrays mismatch.
	 * 
	 */
	public static void showArraysMismatch() {

		final Integer[] arr1 = new Integer[] { 1, 2, 3, 4, 5 };
		final Integer[] arr2 = new Integer[] { 1, 2, 0, 4, 5 };
		System.out.printf("Array 1%s%nArray 2%s%n", Arrays.asList(arr1), Arrays.asList(arr2));
		System.out.printf("Arrays mismatch index[%d]%n", Arrays.mismatch(arr1, arr2));
		System.out.println("- ".repeat(50));
	}

	/**
	 * Goes from the multidimensional array to the multidimensional list.
	 * 
	 */
	public static void multidimensionalArrayToMultidimensionalList() {

		final Integer[][][] array3d = { { {} }, { {}, {}, { null, null, null, 12345 } } };
		final Integer[][][] arrCopy = { { {} }, { {}, {}, { null, null, null, 12345 } } };
		System.out.printf("Two arrays 3-dim equals[%b], deep equals[%b]%n%n", Arrays.equals(array3d, arrCopy),
				Arrays.deepEquals(array3d, arrCopy));
		final Integer[][] array2d = array3d[1];
		final Integer[] array1d = array2d[2];

		final Function<Integer[], List<Integer>> mapper1d = arr -> Stream.of(arr)/*-*/
				.collect(Collectors.toList());
		final Function<Integer[][], List<List<Integer>>> mapper2d = arr -> Stream.of(arr)/*-*/
				.map(mapper1d)/*-*/
				.collect(Collectors.toList());
		final List<List<List<Integer>>> list3d = Stream.of(array3d)/*-*/
				.map(mapper2d)/*-*/
				.collect(Collectors.toList());

		final List<List<Integer>> list2d = Stream.of(array2d)/*-*/
				.map(mapper1d)/*-*/
				.collect(Collectors.toList());

		final List<Integer> list1d = Stream.of(array1d)/*-*/
				.collect(Collectors.toList());

		System.out.printf("List-3-dim %s%n", list3d);
		System.out.printf("list-2-dim\t  %s%n", list2d);
		System.out.printf("List-1-dim\t\t   %s%n%n", list1d);

		System.out.printf("Element of the list:   list-3-dim[%d],  list-2-dim[%d],  list-1-dim[%d]%n",
				list3d.get(1).get(2).get(3), list2d.get(2).get(3), list1d.get(3));
		list3d.get(1).get(2).set(3, 30000);
		System.out.printf("Element of the list:   list-3-dim[%d],  list-2-dim[%d],  list-1-dim[%d]%n",
				list3d.get(1).get(2).get(3), list2d.get(2).get(3), list1d.get(3));
		list2d.get(2).set(3, 20000);
		System.out.printf("Element of the list:   list-3-dim[%d],  list-2-dim[%d],  list-1-dim[%d]%n",
				list3d.get(1).get(2).get(3), list2d.get(2).get(3), list1d.get(3));
		list1d.set(3, 10000);
		System.out.printf("Element of the list:   list-3-dim[%d],  list-2-dim[%d],  list-1-dim[%d]%n%n",
				list3d.get(1).get(2).get(3), list2d.get(2).get(3), list1d.get(3));

		final Function<Integer[][], List<List<Integer>>> mapper2dBacked = arr -> Stream.of(arr)/*-*/
				.map(Arrays::asList)/*-*/
				.collect(Collectors.toList());
		final List<List<List<Integer>>> list3dBacked = Stream.of(array3d)/*-*/
				.map(mapper2dBacked)/*-*/
				.collect(Collectors.toList());
		final List<Integer[]> list2dBacked = Arrays.asList(array2d);
		final List<Integer> list1dBacked = Arrays.asList(array1d);

		System.out.printf("Element of the array: array-3-dim[%d], array-2-dim[%d], array-1-dim[%d]%n", array3d[1][2][3],
				array2d[2][3], array1d[3]);
		System.out.printf("Element of the list:   list-3-dim[%d],  list-2-dim[%d],  list-1-dim[%d](array backed)%n",
				list3dBacked.get(1).get(2).get(3), list2dBacked.get(2)[3], list1dBacked.get(3));
		list1dBacked.set(3, 99999);
		System.out.printf("Element of the array: array-3-dim[%d], array-2-dim[%d], array-1-dim[%d]%n", array3d[1][2][3],
				array2d[2][3], array1d[3]);
		System.out.printf("Element of the list:   list-3-dim[%d],  list-2-dim[%d],  list-1-dim[%d](array backed)%n",
				list3dBacked.get(1).get(2).get(3), list2dBacked.get(2)[3], list1dBacked.get(3));
		System.out.println("- ".repeat(50));
	}

	/**
	 * Apportions the concurrent set.
	 * 
	 */
	public static void apportionSet() {

		final ConcurrentSkipListSet<String> set = Stream.of("aE", "bE", "cE", "dE", "eE")
				.collect(Collectors.toCollection(ConcurrentSkipListSet::new));
		System.out.printf("Source set %s%n", set);
		final Set<String> headSet = set.headSet("cE");
		System.out.printf("Head   set %s%n", headSet);
		final Set<String> subSet = set.subSet("bE", "eE");
		System.out.printf("Sub    set     %s%n", subSet);
		final Set<String> tailSet = set.tailSet("dE");
		System.out.printf("Tail   set             %s%n%n", tailSet);

		set.removeIf("bE"::equals);
		set.removeIf("dE"::equals);
		System.out.println("After removal of two elements from source set: 'bE' and 'dE'");
		System.out.printf("Source set %s%n", set);
		System.out.printf("Head   set %s%n", headSet);
		System.out.printf("Sub    set     %s%n", subSet);
		System.out.printf("Tail   set         %s%n", tailSet);
		System.out.println("- ".repeat(50));
	}

	/**
	 * Apportions the concurrent map.
	 * 
	 */
	public static void apportionMapAndMerge() {

		final ConcurrentNavigableMap<String, String> srcMap = new ConcurrentSkipListMap<>();
		srcMap.putAll(Map.of("aK", "AV", "bK", "BV", "cK", "CV", "dK", "DV", "eK", "EV"));
		System.out.printf("Source map %s%n", srcMap);
		final Map<String, String> headMap = srcMap.headMap("cK");
		System.out.printf("Head   map %s%n", headMap);
		final Map<String, String> subMap = srcMap.subMap("bK", "eK");
		System.out.printf("Sub    map        %s%n", subMap);
		final Map<String, String> tailMap = srcMap.tailMap("dK");
		System.out.printf("Tail   map                      %s%n", tailMap);

		final Collector<Map.Entry<String, String>, ?, TreeMap<String, String>> collector = Collectors
				.toMap(Map.Entry::getKey, Map.Entry::getValue, String::concat, TreeMap::new);
		final Map<String, String> mergedThreeMaps = Stream.of(headMap, subMap, tailMap)/*-*/
				.flatMap(map -> map.entrySet().stream())/*-*/
				.collect(collector);
		System.out.printf("Merged 3 maps ('head' + 'sub' + 'tail'): %s%n%n", mergedThreeMaps);

		srcMap.entrySet().removeIf(entry -> "bK".equals(entry.getKey()));
		srcMap.entrySet().removeIf(entry -> "DV".equals(entry.getValue()));
		System.out.println("After removal of two elements from source map: with key 'bK' and with value 'DV'");
		System.out.printf("Source map %s%n", srcMap);
		System.out.printf("Head   map %s%n", headMap);
		System.out.printf("Sub    map        %s%n", subMap);
		System.out.printf("Tail   map               %s%n", tailMap);
		System.out.println("- ".repeat(50));
	}

	/**
	 * Iterates over a vector.
	 * 
	 */
	public static void iterateOverVector() {

		final Vector<Integer> vector = new Vector<>(Arrays.asList(2, 3, 1));
		Collections.sort(vector);
		System.out.println("Iterate over a vector:");
		vector.forEach(arg -> System.out.printf("%d ", arg));
		System.out.println();
		/*
		 * Advice: use Iterator in preference to Enumeration.
		 */
		final Iterator<Integer> iterator1 = vector.iterator();
		iterator1.forEachRemaining(arg -> System.out.printf("%d ", arg));
		System.out.println();

		final Enumeration<Integer> enumeration = vector.elements();
		final Iterator<Integer> iterator2 = enumeration.asIterator();
		iterator2.forEachRemaining(arg -> System.out.printf("%d ", arg));
		System.out.println();
		System.out.println("- ".repeat(50));
	}

}
