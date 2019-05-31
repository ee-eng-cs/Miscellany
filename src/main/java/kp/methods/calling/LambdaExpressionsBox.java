package kp.methods.calling;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.IntBinaryOperator;
import java.util.function.Supplier;

/**
 * The box with lambda expressions.
 * 
 */
public class LambdaExpressionsBox {
	public final BiConsumer<Integer, Integer> biConsumer;
	public final Supplier<Integer> supplier;
	public final BiPredicate<Integer, Integer> biPredicate;
	public final IntBinaryOperator intBinaryOperator;

	/**
	 * Constructor.
	 * 
	 * @param biConsumer        the consumer
	 * @param supplier          the supplier
	 * @param biPredicate       the predicate
	 * @param intBinaryOperator the operator
	 */
	public LambdaExpressionsBox(BiConsumer<Integer, Integer> biConsumer, Supplier<Integer> supplier,
			BiPredicate<Integer, Integer> biPredicate, IntBinaryOperator intBinaryOperator) {
		super();
		this.biConsumer = biConsumer;
		this.supplier = supplier;
		this.biPredicate = biPredicate;
		this.intBinaryOperator = intBinaryOperator;
	}
}