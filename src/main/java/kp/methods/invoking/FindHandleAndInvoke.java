package kp.methods.invoking;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.util.List;

/**
 * Find the method handle and invoke it.
 *
 */
public interface FindHandleAndInvoke {

	/**
	 * The box.
	 */
	class Box {
		int number;
	}

	/**
	 * Invokes methods of the Object class.
	 * 
	 */
	public static void invokeObjectMethods() {

		final MethodHandles.Lookup lookup = MethodHandles.lookup();

		// The signature of a method 'equals(...)'
		final MethodType methodTypeEQ = MethodType.methodType(boolean.class, Object.class);
		// The signature of a method 'hashCode(...)'
		final MethodType methodTypeHC = MethodType.methodType(int.class);
		// The signature of a method 'toString(...)'
		final MethodType methodTypeTS = MethodType.methodType(String.class);

		final FindHandleAndInvoke findHandleAndInvoke = new FindHandleAndInvoke() {
			@Override
			public String toString() {
				return "example";
			}
		};
		try {
			for (Object object : new Object[] { new Object(), "a", findHandleAndInvoke }) {
				final MethodHandle methodHandleEQ = lookup.findVirtual(object.getClass(), "equals", methodTypeEQ);
				final MethodHandle methodHandleHC = lookup.findVirtual(object.getClass(), "hashCode", methodTypeHC);
				final MethodHandle methodHandleTS = lookup.findVirtual(object.getClass(), "toString", methodTypeTS);

				System.out.printf("Invoking results: 'equals()'[%b], 'hashCode()'[%8x], 'toString()'[%s]%n",
						methodHandleEQ.invoke(object, object), methodHandleHC.invoke(object),
						methodHandleTS.invoke(object));
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
		// The signature of a setter method
		@SuppressWarnings("unused")
		final MethodType methodTypeSET = MethodType.methodType(void.class, Object.class);
		// The signature of a method 'compare(...)' from Comparator<String>
		@SuppressWarnings("unused")
		final MethodType methodTypeSC = MethodType.methodType(int.class, String.class, String.class);

		System.out.println("- ".repeat(50));
	}

	/**
	 * Invokes first the setter method then the getter method.
	 * 
	 */
	public static void invokeFirstSetterThenGetter() {

		final Box box = new Box();
		final MethodHandles.Lookup lookup = MethodHandles.lookup();
		try {
			final MethodHandle methodHandleSET = lookup.findSetter(Box.class, "number", int.class);
			final MethodHandle methodHandleGET = lookup.findGetter(Box.class, "number", int.class);

			methodHandleSET.invoke(box, 123);
			final int number = (int) methodHandleGET.invoke(box);
			System.out.printf("Invoking result from getter: box.number[%d]%n", number);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("- ".repeat(50));
	}

	/**
	 * Replaces an array element.
	 * 
	 */
	public static void replaceArrayElement() {

		final String[] strArray = { "A", "expected", "C" };
		System.out.printf("Array before%s%n", List.of(strArray));

		final VarHandle varHandle = MethodHandles.arrayElementVarHandle(String[].class);
		final boolean result = varHandle.compareAndSet(strArray, 1, "expected", "replaced");

		System.out.printf("Array  after%s%n", List.of(strArray));
		System.out.printf("Replacing the array element was successful[%b]%n", result);
		System.out.println("- ".repeat(50));
	}
}
