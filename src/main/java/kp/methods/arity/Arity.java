package kp.methods.arity;

import java.util.Date;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Multi arity.
 * 
 */
public interface Arity {

	/**
	 * Research sending.
	 * 
	 * @param ternaryFunction    the ternary function
	 * @param quaternaryFunction the quaternary function
	 * @param quinaryFunction    the quinary function
	 * @param septenaryFunction  the septenary function
	 */
	public static void methodWithFiveFunctionParameters(BiFunction<String, String, Consumer<String>> ternaryFunction,
			BiFunction<String, String, BiConsumer<String, String>> quaternaryFunction,
			BiFunction<String, String, BiFunction<String, String, Consumer<String>>> quinaryFunction,
			BiFunction<String, String, BiFunction<String, String, BiFunction<String, String, Consumer<String>>>> septenaryFunction) {

		ternaryFunction.apply("a", "b").accept("c");
		quaternaryFunction.apply("a", "b").accept("c", "d");
		quinaryFunction.apply("a", "b").apply("c", "d").accept("e");
		septenaryFunction.apply("a", "b").apply("c", "d").apply("e", "f").accept("g");
		System.out.println("- ".repeat(50));
	}

	/**
	 * Research receiving.
	 * 
	 * @param quaternaryConsumer the quaternary consumer
	 */
	public static void methodWithConsumerParameter(
			BiConsumer<BiConsumer<Integer, Double>, BiConsumer<String, Date>> quaternaryConsumer) {

		final StringBuffer strBuf = new StringBuffer();
		final BiConsumer<Integer, Double> numConsumer = (num1, num2) -> {
			strBuf.append(String.format("1[%d], 2[%6.3f]", num1, num2));
		};
		final BiConsumer<String, Date> strAndDateconsumer = (str, date) -> {
			strBuf.append(String.format(", 3[%s], 4[%tF]", str, date));
		};
		quaternaryConsumer.accept(numConsumer, strAndDateconsumer);
		System.out.printf("Returned from 'quaternaryConsumer':\t%s%n", strBuf.toString());
		System.out.println("- ".repeat(50));
	}

	/**
	 * Research sending with receiving.
	 * 
	 * @param biFunction the function
	 */
	public static void methodWithFunctionParameter(
			BiFunction<BiConsumer<Integer, Double>, Consumer<String>, BiFunction<Integer, Double, Consumer<String>>> biFunction) {

		final StringBuffer strBuf = new StringBuffer();
		final BiConsumer<Integer, Double> numConsumer = (num1, num2) -> {
			strBuf.append(String.format("4[%d], 5[%2.1f]", num1, num2));
		};
		final Consumer<String> strConsumer = str -> {
			strBuf.append(String.format(", 6[%s]", str));
		};
		final int num1 = 111;
		final double num2 = 2.2;
		final String str = "ccc";
		System.out.printf("Sending to 'biFunction':\t\t   1[%d], 2[%2.1f], 3[%s]%n", num1, num2, str);
		biFunction.apply(numConsumer, strConsumer).apply(num1, num2).accept(str);
		System.out.printf("Returned from 'biFunction':\t\t   %s%n", strBuf.toString());
		System.out.println("- ".repeat(50));
	}
}