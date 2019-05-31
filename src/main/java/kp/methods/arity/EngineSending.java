package kp.methods.arity;

import java.util.Date;
import java.util.function.BiConsumer;

/**
 * The engine with sending.
 * 
 */
public interface EngineSending {

	/**
	 * The field with quaternary consumer. It returns four values.
	 * 
	 */
	BiConsumer<BiConsumer<Integer, Double>, BiConsumer<String, Date>> QUATERNARY_CONSUMER = (consumer1, consumer2) -> {
		final int num1 = 123;
		final double num2 = 456.789;
		final String str = "abc";
		final Date date = new Date();
		consumer1.accept(num1, num2);
		consumer2.accept(str, date);
		System.out.printf("EngineSending.QUATERNARY_CONSUMER():\t1[%d], 2[%6.3f], 3[%s], 4[%tF]%n", num1, num2, str,
				date);
	};
}
