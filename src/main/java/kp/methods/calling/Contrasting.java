package kp.methods.calling;

import java.util.function.Consumer;

/**
 * Contrasting the methods calling.
 *
 */
public class Contrasting {

	/**
	 * The example interface used by anonymous class.
	 * 
	 */
	interface TheInterface {
		default void defaultInterfaceMethod() {
			System.out.println("defaultInterfaceMethod():");
		}
	}

	/**
	 * Using of <b>local class</b> versus using of <b>anonymous class</b> versus
	 * using of <b>lambda expression</b>.<br>
	 * <br>
	 * Alternatives:
	 * <ol>
	 * <li>local class
	 * <li>anonymous class
	 * <li>lambda expression
	 * <li>static nested class
	 * <li>inner class
	 * </ol>
	 * 
	 */
	public static void usingLocalClassVersusAnonymousClassVersusLambdaExpression() {

		/**
		 * The example local class used here.
		 */
		class TheLocalClass {
			void localClassMethod() {
				System.out.println("localClassMethod():");
			}
		}
		final TheLocalClass theLocalClass = new TheLocalClass();
		theLocalClass.localClassMethod();

		final TheInterface anonymousClass1 = new TheInterface() {
		};
		anonymousClass1.defaultInterfaceMethod();
		final TheInterface anonymousClass2 = new TheInterface() {
			@Override
			public void defaultInterfaceMethod() {
				System.out.println("defaultInterfaceMethod(): overriden");
			}
		};
		anonymousClass2.defaultInterfaceMethod();

		final Consumer<Void> consumer = arg -> System.out.println("consumer operation");
		consumer.accept(null);
		System.out.println("- ".repeat(50));
	}

	/**
	 * Accessing methods with the lambda expressions or calling them directly on the
	 * processor.
	 * 
	 */
	public static void lambdaAccessingVersusCallingDirectly() {

		final int number1 = 1, number2 = 2;
		final Processor processor = new Processor();
		final LambdaExpressionsBox lambdaExpressionsBox = processor.getLambdaExpressionsBox();

		lambdaExpressionsBox.biConsumer.accept(number1, number2);
		processor.methodForBiConsumer(number1, number2);
		System.out.printf("Contrasted 'biConsumer': number1[%d], number2[%d]%n%n", number1, number2);

		final int resultInt11 = lambdaExpressionsBox.supplier.get();
		final int resultInt12 = processor.methodForSupplier();
		System.out.printf("Contrasted 'supplier': result[%d]/[%d]%n%n", resultInt11, resultInt12);

		final boolean resultBoolean1 = lambdaExpressionsBox.biPredicate.test(number1, number2);
		final boolean resultBoolean2 = processor.methodForBiPredicate(number1, number2);
		System.out.printf("Contrasted 'biPredicate': number1[%d], number2[%d], result[%b]/[%b]%n%n", number1, number2,
				resultBoolean1, resultBoolean2);

		final int resultInt21 = lambdaExpressionsBox.intBinaryOperator.applyAsInt(number1, number2);
		final int resultInt22 = processor.methodForIntBinaryOperator(number1, number2);
		System.out.printf("Contrasted 'intBinaryOperator': number1[%d], number2[%d], result[%d]/[%d]%n", number1,
				number2, resultInt21, resultInt22);
		System.out.println("- ".repeat(50));
	}
}
