package kp.methods.proxy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Proxy and invoke.
 *
 */
public interface ProxyAndInvoke {

	/**
	 * Invokes method.
	 * 
	 */
	public static void invokeMethod() {

		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		final Class<?>[] interfacesArr = new Class[] { Duck.class };
		final InvocationHandler invocationHandler = (proxyInstance, methodInstance, arguments) -> {
			final MethodHandle methodHandle = MethodHandles.lookup().findSpecial(/*-*/
					Duck.class, /*-*/
					methodInstance.getName(), /*-*/
					MethodType.methodType(void.class, new Class[0]), /*-*/
					Duck.class);
			methodHandle.bindTo(proxyInstance).invokeWithArguments();
			System.out.printf("proxy[%s], method[%s], arguments[%s]%n", proxyInstance.getClass().getName(),
					methodInstance.getName(), arguments);
			return null;
		};
		final Duck duckProxy = (Duck) Proxy.newProxyInstance(classLoader, interfacesArr, invocationHandler);
		duckProxy.quack();
		System.out.printf("Is proxy class: Duck[%s], Duck proxy[%s]%n", Proxy.isProxyClass(new Duck() {
		}.getClass()), Proxy.isProxyClass(duckProxy.getClass()));
		System.out.println("- ".repeat(50));
	}
}

/**
 * Example interface 'Duck'.
 * 
 */
interface Duck {
	/**
	 * Example default method 'quack'.
	 * 
	 */
	default void quack() {

		final Stream<StackTraceElement> stream = Stream.of(new Throwable().getStackTrace());
		final String elements = stream.map(StackTraceElement::toString).collect(Collectors.joining("\n\t"));
		System.out.printf("Stack trace elements from the 'quack' method invocation:%n\t%s%n", elements);
		System.out.printf("class[%s]%n", this.getClass().getName());
	}
}
