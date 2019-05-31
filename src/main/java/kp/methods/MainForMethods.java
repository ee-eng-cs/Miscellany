package kp.methods;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import kp.methods.arity.Arity;
import kp.methods.arity.EngineBothWays;
import kp.methods.arity.EngineReceiving;
import kp.methods.arity.EngineSending;
import kp.methods.calling.Contrasting;
import kp.methods.composing.FunctionComposer;
import kp.methods.functional.TheFunctionalInterface;
import kp.methods.invoking.FindHandleAndInvoke;
import kp.methods.proxy.ProxyAndInvoke;

/**
 * The main launcher for methods research.
 *
 */
public class MainForMethods {

	private static final boolean ALL = true;
	private static final boolean ARITY = false;
	private static final boolean CONTRASTING = false;
	private static final boolean FUNCTION_COMPOSER = false;
	private static final boolean FUNCTIONAL_INTERFACE = false;
	private static final boolean INVOKING = false;
	private static final boolean PROXY_AND_INVOKE = false;

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		if (ALL || ARITY) {
			Arity.methodWithFiveFunctionParameters(EngineReceiving.TERNARY_FUNCTION,
					EngineReceiving.QUATERNARY_FUNCTION, EngineReceiving.QUINARY_FUNCTION,
					EngineReceiving.SEPTENARY_FUNCTION);
		}
		if (ALL || ARITY) {
			Arity.methodWithConsumerParameter(EngineSending.QUATERNARY_CONSUMER);
		}
		if (ALL || ARITY) {
			Arity.methodWithFunctionParameter(EngineBothWays.FUNCTION);
		}
		if (ALL || CONTRASTING) {
			Contrasting.usingLocalClassVersusAnonymousClassVersusLambdaExpression();
		}
		if (ALL || CONTRASTING) {
			Contrasting.lambdaAccessingVersusCallingDirectly();
		}
		if (ALL || FUNCTION_COMPOSER) {
			FunctionComposer.composeFunction();
		}
		if (ALL || FUNCTIONAL_INTERFACE) {
			launchTheFunctionalInterface();
		}
		if (ALL || INVOKING) {
			FindHandleAndInvoke.invokeObjectMethods();
		}
		if (ALL || INVOKING) {
			FindHandleAndInvoke.invokeFirstSetterThenGetter();
		}
		if (ALL || INVOKING) {
			FindHandleAndInvoke.replaceArrayElement();
		}
		if (ALL || PROXY_AND_INVOKE) {
			ProxyAndInvoke.invokeMethod();
		}
	}

	/**
	 * Launches the functional interface.
	 * 
	 */
	private static void launchTheFunctionalInterface() {

		final TheFunctionalInterface theFunctionalInterface = arg -> System.out.printf("first(): arg[%s]%n", arg);
		theFunctionalInterface.first("one");
		theFunctionalInterface.second("two");
		TheFunctionalInterface.third("three");
		TheFunctionalInterface.fourth();
		TheFunctionalInterface.fifth("five");
		System.out.println();

		final Stream<Consumer<String>> consumerStream = Stream.of(theFunctionalInterface::first,
				theFunctionalInterface::second, TheFunctionalInterface::third);
		consumerStream.forEach(consumer -> consumer.accept("consumer accepted value"));
		final Supplier<String> supplier = TheFunctionalInterface::fourth;
		System.out.printf("From 'fourth()' supplier: result[%s]%n", supplier.get());
		final Function<String, Void> function = TheFunctionalInterface::fifth;
		System.out.printf("From 'fifth()' function: 'Void' result[%s]%n", function.apply("function applied value"));
		System.out.println("- ".repeat(50));
	}

}
