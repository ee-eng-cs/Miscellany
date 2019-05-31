package kp.methods.calling;

/**
 * The processor which uses the lambda expressions box.
 *
 */
public class Processor {
	private LambdaExpressionsBox lambdaExpressionsBox;

	/**
	 * Constructor.
	 * 
	 */
	public Processor() {
		super();
		this.lambdaExpressionsBox = new LambdaExpressionsBox(/*-*/
				this::methodForBiConsumer, /*-*/
				this::methodForSupplier, /*-*/
				this::methodForBiPredicate, /*-*/
				this::methodForIntBinaryOperator);
	}

	/**
	 * Gets the box with lambda expressions.
	 * 
	 * @return the lambda expressions box
	 */
	public LambdaExpressionsBox getLambdaExpressionsBox() {
		return lambdaExpressionsBox;
	}

	/**
	 * The method for two-arity consumer.
	 * 
	 * @param number1 the number 1
	 * @param number2 the number 2
	 */
	public void methodForBiConsumer(int number1, int number2) {

		System.out.printf("methodForBiConsumer():   number1[%d], number2[%d]%n", number1, number2);
	}

	/**
	 * The method for supplier.
	 * 
	 * @return the result
	 */
	public int methodForSupplier() {

		final int result = 3;
		System.out.printf("methodForSupplier():   result[%d]%n", result);
		return result;
	}

	/**
	 * The method for two-arity predicate.
	 * 
	 * @param number1 the number 1
	 * @param number2 the number 2
	 * @return the result
	 */
	public boolean methodForBiPredicate(int number1, int number2) {

		final boolean result = number1 < number2;
		System.out.printf("methodForBiPredicate():   number1[%d], number2[%d], result[%b]%n", number1, number2, result);
		return result;
	}

	/**
	 * The method for binary operator.
	 * 
	 * @param number1 the number 1
	 * @param number2 the number 2
	 * @return the result
	 */
	public int methodForIntBinaryOperator(int number1, int number2) {

		final int result = 3;
		System.out.printf("methodForIntBinaryOperator():   number1[%d], number2[%d], result[%d]%n", number1, number2,
				result);
		return result;
	}
}