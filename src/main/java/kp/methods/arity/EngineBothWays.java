package kp.methods.arity;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * The engine with sending and receiving.
 * 
 */
public interface EngineBothWays {
	/**
	 * The field with function.
	 * 
	 */
	BiFunction<BiConsumer<Integer, Double>, Consumer<String>, BiFunction<Integer, Double, Consumer<String>>> FUNCTION = (
			cons1, cons2) -> {
		BiFunction<Integer, Double, Consumer<String>> function = (num1, num2) -> {
			Consumer<String> consumer = str -> System.out
					.printf("EngineBothWays.FUNCTION(): received params 1[%d], 2[%2.1f], 3[%s]%n", num1, num2, str);
			return consumer;
		};
		final int num1 = 444;
		final double num2 = 5.5;
		final String str = "fff";
		cons1.accept(num1, num2);
		cons2.accept(str);
		System.out.printf("EngineBothWays.FUNCTION(): sending  params 1[%d], 2[%2.1f], 3[%s]%n", num1, num2, str);
		return function;
	};
}
