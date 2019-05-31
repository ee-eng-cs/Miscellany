package a.latest.java.declarations;

import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.LongBinaryOperator;
import java.util.function.ObjDoubleConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The annotation type <b>@Example</b> definition.
 *
 */
@interface Example {
	/**
	 * The array.
	 * 
	 * @return the array
	 */
	String[] array() default { "A", "B", "C" };
}

/**
 * Only local (mostly unused) variables declarations.
 *
 */
public interface Declarations {

	/**
	 * This method has intentionally many unused declarations.<br>
	 * It uses many method references<br>
	 * (like for example 'Random::new' - the reference to a constructor)
	 * 
	 */
	@SuppressWarnings("unused")
	static void declareLocalVariables() {

		System.out.println("Declarations");
		/*
		 * Function.
		 */
		final Stream<Function<String, String>> stringFunctionStream = Stream.of(String::trim, String::toUpperCase);
		final Function<String, char[]> toCharArrayFunction = String::toCharArray;
		final Function<char[], String> valueOfStringFunction = String::valueOf;
		final Function<Integer, String> valueOfIntegerFunction = String::valueOf;
		final Function<String, List<String>> asListFunction = Arrays::asList;

		final Function<String, String> joinFunction = str -> String.join("-");
		final Function<String, Integer> integerFunction = str -> Objects.nonNull(str) || str.isEmpty() ? 0 : 1;
		final Function<Object, Object> objectFunction = Function.identity().compose(arg -> new Object());
		final Function<Void, Void> voidFunction1 = arg -> null;
		final Function<Object, Void> voidFunction2 = objectFunction.andThen(arg -> null);
		final Function<List<String>, String[]> toArrayFunction = arg -> arg.toArray(String[]::new);
		/*
		 * Primitive specializations of Function.
		 */
		final IntFunction<int[]> intArrDim1Function1 = arg -> {
			return new int[arg];
		};
		final IntFunction<int[]> intArrDim1Function2 = int[]::new;
		final IntFunction<int[][][]> intArrDim3Function1 = arg -> {
			return new int[arg][][];
		};
		final IntFunction<int[][][]> intArrDim3Function2 = int[][][]::new;

		final ToDoubleFunction<String> toDoubleFunction = Double::valueOf;
		/*
		 * Operators - specializations of Function.
		 */
		final UnaryOperator<BigDecimal> bigDecimalUnaryOperator = BigDecimal::negate;
		final DoubleUnaryOperator xToY = Math::exp;
		final DoubleUnaryOperator yToX = Math::log;
		final DoubleUnaryOperator yToYToX = xToY.andThen(yToX);
		{
			final UnaryOperator<String> unaryOperator001 = String::toUpperCase;
			final Function<String, String> functionForUnaryOperator001 = unaryOperator001::apply;
			final UnaryOperator<String> unaryOperator002 = functionForUnaryOperator001::apply;
			final Function<String, String> functionForUnaryOperator002 = unaryOperator002::apply;
		}
		/*
		 * Two-arity specializations of Function.
		 */
		final BiFunction<Integer, String, List<String>> nCopiesFunction = Collections::nCopies;
		final BiFunction<List<String>, String, Integer> frequencyFunction = Collections::frequency;
		final BiFunction<Integer, Long, Stream<Number>> numberStreamBiFunction = Stream::of;

		final ToDoubleBiFunction<Double, Integer> toDoubleBiFunction = Math::scalb;

		final BinaryOperator<String> joinBinaryOperator = String::join;
		final BinaryOperator<String> maxLengthString = BinaryOperator.maxBy(Comparator.comparingInt(String::length));
		final BinaryOperator<Integer> integerBinaryOperator = BinaryOperator.maxBy(Comparator.reverseOrder());
		final BinaryOperator<BigDecimal> bigDecimalBinaryOperator = BigDecimal::multiply;
		final IntBinaryOperator intBinaryOperator = Math::multiplyExact;
		final LongBinaryOperator longBinaryOperator = Math::multiplyExact;
		final DoubleBinaryOperator doubleBinaryOperator = (s, t) -> s * t;
		{
			final BinaryOperator<String> plusBinaryOperator001 = (s, t) -> s + t;
			final BiFunction<String, String, String> plusBiFunction001 = plusBinaryOperator001::apply;
			final BinaryOperator<String> plusBinaryOperator002 = plusBiFunction001::apply;
			final BiFunction<String, String, String> plusBiFunction002 = plusBinaryOperator002::apply;
		}
		/*
		 * Consumer.
		 */
		final Consumer<Object> objectConsumer = arg -> {
		};
		final Consumer<List<?>> listConsumerFull = (final @Example(array = { "A", "B", "C" }) List<?> arg) -> {
		};
		final Consumer<Character> characterConsumer = objectConsumer::accept;
		final Consumer<Void> voidConsumer = objectConsumer::accept;
		final BiConsumer<Object, Object> objectBiConsumer = (arg1, arg2) -> {
		};
		final BiConsumer<Integer, String> biConsumer = objectBiConsumer::accept;
		final ObjDoubleConsumer<String> stringDoubleConsumer = objectBiConsumer::accept;
		/*
		 * Primitive specializations of Consumer.
		 */
		final IntConsumer intConsumer = objectConsumer::accept;
		/*
		 * Supplier.
		 */
		final Supplier<Declarations> thisSupplier = () -> new Declarations() {
			final Supplier<String> stringSupplier = this::toString;
			final Supplier<String> stringSupplierBis = stringSupplier::get;
			final Supplier<String> nameSupplier = this.getClass()::getName;
			final IntSupplier intSupplier = this::hashCode;
		};
		final Supplier<Void> voidSupplier = () -> null;
		final Supplier<Character> characterSupplier = () -> Character.MAX_VALUE;
		final Supplier<Character> characterSupplierBis = characterSupplier::get;
		final Supplier<Random> randomSupplier = Random::new;
		final Supplier<String> stringSupplier = Object.class::getName;
		final Supplier<List<String>> listSupplier = ArrayList::new;
		final Supplier<Integer> integerSupplier = () -> Integer.MAX_VALUE;
		/*
		 * Primitive specializations of Supplier.
		 */
		final IntSupplier intSupplier = () -> Integer.MAX_VALUE;
		final IntSupplier intSupplierBis = integerSupplier::get;
		/*
		 * Predicate.
		 */
		final Stream<Predicate<List<String>>> listPredicateStream = Stream.of(List::isEmpty, Objects::nonNull);
		final Predicate<String> predicate = String::isEmpty;
		final Predicate<String> predicateBis = predicate::test;
		final Predicate<String> composedPredicate = predicate.or(Objects::isNull).negate().and(Objects::nonNull);
		final Function<String, Integer> testFunction = str -> predicate.test(str) ? 0 : 1;
		final Predicate<String> prefixPredicate = str -> str.startsWith("prefix");
		/*
		 * Predicate versus function.
		 */
		new Declarations() {
			final Function<Object, Boolean> function = this::equals;
			final Predicate<Object> predicate = this::equals;
		};
		final BiFunction<String, String, Boolean> stringMatchesFunction = String::matches;
		final BiPredicate<String, String> stringMatchesPredicate = String::matches;
		final BiFunction<String, String, Boolean> patternMatchesFunction = Pattern::matches;
		final BiPredicate<String, String> patternMatchesPredicate = Pattern::matches;
		final BiFunction<LocalDate, LocalDate, Boolean> isBeforeFunction = LocalDate::isBefore;
		final BiPredicate<LocalDate, LocalDate> isBeforePredicate = LocalDate::isBefore;
		/*
		 * Listener.
		 */
		final Consumer<Object> processingConsumer = arg -> {
		};
		final ActionListener actionListener = processingConsumer::accept;
		final PropertyChangeListener propertyChangeListener = processingConsumer::accept;
		/*
		 * Collecting.
		 */
		final Set<Integer> set1 = Stream.of(1).collect(Collectors.toSet());
		final List<Integer> list1 = Stream.of(1).collect(Collectors.toList());
		final List<Integer> list2 = Stream.of(1).collect(Collectors.toCollection(ArrayList::new));

		final Set<Integer> set2 = Stream.of(1).collect(HashSet::new, Set::add, Set::addAll);
		final List<Integer> list3 = Stream.of(1).collect(ArrayList::new, List::add, List::addAll);
		final Queue<Integer> queue = Stream.of(1).collect(LinkedList::new, Queue::add, Queue::addAll);
		final Deque<Integer> deque = Stream.of(1).collect(ArrayDeque::new, Deque::add, Deque::addAll);
		final Map<Integer, Integer> map = Map.of(1, 1).entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		final StringBuffer stringBuffer = Stream.of(1).collect(StringBuffer::new, StringBuffer::append,
				StringBuffer::append);
		/*
		 * Action starts endless loop.
		 */
		final Supplier<Integer> endlessSupplier = () -> {
			while (true)
				;
		};
		final Function<Integer, Integer> endlessFunction = (arg) -> {
			while (true)
				;
		};
		final Consumer<Integer> endlessConsumer = (arg) -> {
			while (true)
				;
		};
		System.out.println("- ".repeat(50));
	}

}