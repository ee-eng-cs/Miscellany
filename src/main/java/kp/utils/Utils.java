package kp.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Utilities.
 *
 */
public interface Utils {

	/**
	 * Calculates elapsed times.
	 * 
	 * @param instantList the instant list
	 * @return the formated message with elapsed times
	 */
	public static String calculateElapsedTimes(ArrayList<Instant> instantList) {

		if (instantList.size() < 2) {
			return "";
		}
		final StringBuffer strBuf = new StringBuffer();
		for (int i = 0; i < instantList.size() - 1; i++) {
			strBuf.append(String.format("%d.diff[%3dms]", i + 1,
					Duration.between(instantList.get(i), instantList.get(i + 1)).toMillis()));
			if (i < instantList.size() - 2) {
				strBuf.append(", ");
			}
		}
		return strBuf.toString();
	}

	/**
	 * Breaks the line using the given line length.
	 * 
	 * @param message the input message
	 * @param length  the line length
	 * @return the output message
	 */
	public static String breakLine(String message, int length) {

		final StringBuilder strBuf = new StringBuilder();
		for (int begin = 0, end = 0; end < message.length(); begin = end) {
			end = begin + length < message.length() ? end + length : message.length();
			strBuf.append(message.substring(begin, end)).append("\n");
		}
		return strBuf.toString();
	}

	/**
	 * Formats elapsed time.
	 * 
	 * @param start  the start
	 * @param finish the finish
	 * @return the formated elapsed time
	 */
	public static String formatElapsed(Instant start, Instant finish) {
		return formatElapsed("time elapsed", Duration.between(start, finish));
	}

	/**
	 * Formats elapsed time.
	 * 
	 * @param label            the label
	 * @param durationOptional the optional of duration
	 * @return the formated elapsed time
	 */
	public static String formatElapsed(String label, Optional<Duration> durationOptional) {
		return formatElapsed(label, durationOptional.orElse(Duration.ofMillis(0)));
	}

	/**
	 * Formats elapsed time.
	 * 
	 * @param label    the label
	 * @param duration the duration
	 * @return the formated elapsed time
	 */
	public static String formatElapsed(String label, Duration duration) {

		final long time = duration.toNanos();
		final long nanos = time % 1_000;
		final long micros = time / 1_000 % 1_000;
		final long millis = time / 1_000_000 % 1_000;
		final long seconds = time / 1_000_000_000;
		if (seconds > 0) {
			return String.format("%s[%ds %3dms %3dμs %3dns]", label, seconds, millis, micros, nanos);
		} else if (millis > 0) {
			return String.format("%s[%3dms %3dμs %3dns]", label, millis, micros, nanos);
		} else if (micros > 0) {
			return String.format("%s[%3dμs %3dns]", label, micros, nanos);
		} else {
			return String.format("%s[%3dns]", label, nanos);
		}
	}

	/**
	 * Formats instant as time.
	 * 
	 * @param label           the label
	 * @param instantOptional the optional of instant
	 * @return the formated instant
	 */
	public static String formatInstant(String label, Optional<Instant> instantOptional) {

		final LocalTime localTime = LocalTime.ofInstant(instantOptional.orElse(Instant.now()), ZoneId.systemDefault());
		return String.format("%1$s[%2$tT.%2$tL]", label, localTime);
	}

	/**
	 * Formats number.
	 * 
	 * @param number the number
	 * @return the formated string
	 */
	public static String formatNumber(long number) {

		final NumberFormat numberFormat = NumberFormat.getInstance();
		if (!(numberFormat instanceof DecimalFormat)) {
			return numberFormat.format(number);
		}
		final DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
		DecimalFormatSymbols decimalFormatSymbols = decimalFormat.getDecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator('.');
		decimalFormatSymbols.setGroupingSeparator('\'');
		decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
		return decimalFormat.format(number);
	}

	/**
	 * Pauses for seconds.
	 * 
	 * @param seconds the seconds
	 */
	public static void sleepSeconds(int seconds) {

		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			// ignore
		}
	}

	/**
	 * Pauses for milliseconds.
	 * 
	 * @param milliseconds the milliseconds
	 */
	public static void sleepMillis(int milliseconds) {

		try {
			TimeUnit.MILLISECONDS.sleep(milliseconds);
		} catch (InterruptedException e) {
			// ignore
		}
	}
}