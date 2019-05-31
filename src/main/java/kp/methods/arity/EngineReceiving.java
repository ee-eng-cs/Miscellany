package kp.methods.arity;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * The engine with receiving.
 * 
 */
public interface EngineReceiving {
	/**
	 * The field with ternary function.
	 */
	BiFunction<String, String, Consumer<String>> TERNARY_FUNCTION = (arg1, arg2) -> {
		final Consumer<String> consumer = arg3 -> System.out
				.printf("EngineReceiving.TERNARY_FUNCTION():\t1[%s], 2[%s], 3[%s]%n", arg1, arg2, arg3);
		return consumer;
	};

	/**
	 * The field with quaternary function.
	 */
	BiFunction<String, String, BiConsumer<String, String>> QUATERNARY_FUNCTION = (arg1, arg2) -> {
		final BiConsumer<String, String> biConsumer = (arg3, arg4) -> System.out
				.printf("EngineReceiving.QUATERNARY_FUNCTION():\t1[%s], 2[%s], 3[%s], 4[%s]%n", arg1, arg2, arg3, arg4);
		return biConsumer;
	};

	/**
	 * The field with quinary function.
	 */
	BiFunction<String, String, BiFunction<String, String, Consumer<String>>> QUINARY_FUNCTION = (arg1, arg2) -> {
		final BiFunction<String, String, Consumer<String>> biFunction = (arg3, arg4) -> {
			final Consumer<String> consumer = arg5 -> System.out.printf(
					"EngineReceiving.QUINARY_FUNCTION():\t1[%s], 2[%s], 3[%s], 4[%s], 5[%s]%n", arg1, arg2, arg3, arg4,
					arg5);
			return consumer;
		};
		return biFunction;
	};

	/**
	 * The field with septenary function.
	 */
	BiFunction<String, String, BiFunction<String, String, BiFunction<String, String, Consumer<String>>>> SEPTENARY_FUNCTION = (
			arg1, arg2) -> {
		final BiFunction<String, String, BiFunction<String, String, Consumer<String>>> biFunction1 = (arg3, arg4) -> {
			final BiFunction<String, String, Consumer<String>> biFunction2 = (arg5, arg6) -> {
				final Consumer<String> consumer = arg7 -> System.out.printf(
						"EngineReceiving.SEPTENARY_FUNCTION():\t1[%s], 2[%s], 3[%s], 4[%s], 5[%s], 6[%s], 7[%s]%n",
						arg1, arg2, arg3, arg4, arg5, arg6, arg7);
				return consumer;
			};
			return biFunction2;
		};
		return biFunction1;
	};
}