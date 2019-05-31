package a.latest.java.subjects;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The local variable type inference. Using a var instead of a type.
 */
public interface Var {
	/**
	 * Launches.
	 * 
	 */
	static void launch() {

		final List<?> notVar = List.of(1, 2, 3);
		System.out.printf("'notVar' list \t\tclass[%s]%n%n", notVar.getClass().getName());
		final byte one = 1;
		final var mixedList = List.of(/*-*/
				one, /*- Byte */
				2, /*- Integer */
				3.0, /*- Double */
				BigInteger.valueOf(4), /*- BigInteger */
				BigDecimal.valueOf(5.0), /*- BigDecimal */
				'6', /*- Character */
				"7"/*- String */
		);
		System.out.printf("Mixed  list size[%d], \tclass[%s], elements:%n", mixedList.size(),
				mixedList.getClass().getName());
		for (var elem : mixedList) {
			System.out.printf("\telement[%3s], class[%20s]%n", elem, elem.getClass().getName());
		}
		final StringBuilder strBld = new StringBuilder();
		strBld.append("Mixed list values from loop  :");
		for (var i = 0; i < mixedList.size(); i++) {
			strBld.append(" ").append(mixedList.get(i));
		}
		strBld.append("\nMixed list values from stream:");
		final var mixedStream = mixedList.stream();
		mixedStream.forEach(elem -> strBld.append(" ").append(elem));
		System.out.println(strBld.append("\n").toString());

		final var box = new Box<>((byte) 1, BigDecimal.TEN) {
		};
		final var boxT = box.getT();
		System.out.printf("box.t: value[%2s], class[%20s]%n", boxT, boxT.getClass().getName());
		final var boxU = box.getU();
		System.out.printf("box.u: value[%2s], class[%20s]%n", boxU, boxU.getClass().getName());
		System.out.println("- ".repeat(50));
	}

	/**
	 * This method is intentionally not used.
	 * 
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unused")
	static void unused() throws Exception {
		final var arrayList = new ArrayList<String>();
		final var linkedList = new LinkedList<>();
		final var url = new URL("http://kp.org");
		final var connection = url.openConnection();
		final var reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		final var path = Paths.get("src/main/java/a.latest.java/LatestJava.java");
		final var bytes = Files.readAllBytes(path);
		final var array = new String[][][] { { {} } };
		for (var arr1 : array) {
			for (var arr2 : arr1) {
				for (var arr3 : arr2) {
				}
			}
		}
		var list = new ArrayList<String>();
		// list = new LinkedList<>();
		// The line above causes error "Cannot infer type arguments for LinkedList<>"
	}
}

/**
 * Box.
 *
 * @param <T> the T
 * @param <U> the U
 */
class Box<T, U> {
	/**
	 * t the t
	 */
	private final T t;
	/**
	 * u the u
	 */
	private final U u;

	/**
	 * Constructor.
	 * 
	 * @param t the t
	 * @param u the u
	 */
	Box(T t, U u) {
		this.t = t;
		this.u = u;
	}

	/**
	 * 
	 * @return the t
	 */
	T getT() {
		return t;
	}

	/**
	 * 
	 * @return the u
	 */
	U getU() {
		return u;
	}
}
