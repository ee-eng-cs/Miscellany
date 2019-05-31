package kp.methods.functional;

/**
 * Any interface with a Single Abstract Method is a <B>functional interface</B>.
 * 
 */
@FunctionalInterface
public interface TheFunctionalInterface {

	/**
	 * The abstract method.
	 * 
	 * @param arg the arguments
	 */
	void first(String arg);

	/**
	 * The default method.
	 * 
	 * @param arg the arguments
	 */
	default void second(String arg) {
		System.out.printf("second(): arg[%s]%n\t'this' class[%s]%n", arg, this.getClass().getSimpleName());
		secondPrivate(arg);
	}

	/**
	 * The static unary method (takes one argument).
	 * 
	 * @param arg the arguments
	 */
	static void third(String arg) {
		System.out.printf("third(): arg[%s]%n", arg);
		thirdPrivate(arg);
	}

	/**
	 * The static nullary method (takes no arguments).
	 * 
	 * @return the result
	 */
	static String fourth() {
		String result = "four";
		System.out.printf("fourth(): returning result[%s]%n", result);
		return result;
	}

	/**
	 * The class method with the return type Void.
	 * 
	 * @param arg the arguments
	 * @return the Void result
	 */
	static Void fifth(String arg) {
		System.out.printf("fifth(): arg[%s]%n", arg);
		return null;
	}

	/**
	 * The private method.
	 * 
	 * @param arg the arguments
	 */
	private void secondPrivate(String arg) {
		System.out.printf("secondPrivate(): arg[%s]%n", arg);
	}

	/**
	 * The private method.
	 * 
	 * @param arg the arguments
	 */
	private static void thirdPrivate(String arg) {
		System.out.printf("thirdPrivate(): arg[%s]%n", arg);
	}
}