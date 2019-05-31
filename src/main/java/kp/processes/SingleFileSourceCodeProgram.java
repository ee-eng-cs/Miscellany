package kp.processes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * The single-file source-code program with logging.
 * 
 */
public class SingleFileSourceCodeProgram {
	private static final Logger logger = Logger.getLogger(SingleFileSourceCodeProgram.class.getName());
	static {
		final String line = "java.util.logging.SimpleFormatter.format = %1$tF %1$tT.%1$tL %4$-7s %3$s.%5$s%6$s%n";
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(line.getBytes());
		try {
			LogManager.getLogManager().readConfiguration(byteArrayInputStream);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		final ConsoleHandler consoleHandler = new ConsoleHandler();
		logger.addHandler(consoleHandler);
		consoleHandler.setLevel(Level.ALL);
		logger.setLevel(Level.ALL);
	}

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 * @throws InterruptedException the interrupted exception
	 */
	public static void main(String[] args) throws InterruptedException {

		logger.finest("main(): logging with level finest");
		TimeUnit.MILLISECONDS.sleep(10);
		logger.fine("main(): logging with level fine");
		TimeUnit.MILLISECONDS.sleep(10);
		logger.config("main(): logging with level config");
		TimeUnit.MILLISECONDS.sleep(10);
		logger.info("main(): logging with level info");
		TimeUnit.MILLISECONDS.sleep(10);
		logger.warning("main(): logging with level warning");
		TimeUnit.MILLISECONDS.sleep(10);
		logger.severe("main(): logging with level severe");
	}
}