package kp.methods.composing;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * The function composer.
 *
 */
public interface FunctionComposer {

	Supplier<Stream<Integer>> supplier = () -> IntStream.rangeClosed(1, 9).boxed();

	Function<Integer, Cardinal> mapperCardinal = number -> {
		final Cardinal result = Stream.of(Cardinal.values())/*-*/
				.filter(arg -> arg.ordinal() + 1 == number).findFirst().orElse(null);
		System.out.printf("%-6s", result);
		return result;
	};
	Function<Integer, OrdinalSpatial> mapperOrdinalSpatial = ordinal -> {
		final OrdinalSpatial result = Stream.of(OrdinalSpatial.values())/*-*/
				.filter(arg -> arg.ordinal() == ordinal).findFirst().orElse(null);
		System.out.printf(" → %-7s", result);
		return result;
	};
	Function<Integer, OrdinalPrecedence> mapperOrdinalPrecedence = ordinal -> {
		final OrdinalPrecedence result = Stream.of(OrdinalPrecedence.values())/*-*/
				.filter(arg -> arg.ordinal() == ordinal).findFirst().orElse(null);
		System.out.printf(" → %-10s", result);
		return result;
	};
	Function<Integer, Multiplier> mapperMultiplier = ordinal -> {
		final Multiplier result = Stream.of(Multiplier.values())/*-*/
				.filter(arg -> arg.ordinal() == ordinal).findFirst().orElse(null);
		System.out.printf(" → %-9s", result);
		return result;
	};

	/**
	 * Composes the function.
	 * 
	 */
	static void composeFunction() {

		final Function<Integer, Integer> composedFunction = mapperCardinal.andThen(Cardinal::ordinal)/*-*/
				.andThen(mapperOrdinalSpatial).andThen(OrdinalSpatial::ordinal)/*-*/
				.andThen(mapperOrdinalPrecedence).andThen(OrdinalPrecedence::ordinal)/*-*/
				.andThen(mapperMultiplier).andThen(Multiplier::ordinal)/*-*/
				.andThen(ordinal -> ++ordinal);

		supplier.get().map(composedFunction).forEach(arg -> System.out.printf(" → %d%n", arg));
		System.out.println("- ".repeat(50));
	}
}
