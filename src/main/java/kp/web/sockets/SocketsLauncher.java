package kp.web.sockets;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.LongSummaryStatistics;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;

import kp.utils.Utils;
import kp.web.sockets.helper.RandomTextsGenerator;

/*-
 * The JSSE (Java Secure Socket Extension) Reference Guide (for Java SE 11):
 * https://docs.oracle.com/en/java/javase/11/security/java-secure-socket-extension-jsse-reference-guide.html
 * 
 * ------------------------------------------------------------------------------------------------
 * Secure channels:
 *    - SSLSocket (based on a blocking I/O model)
 *    - SSLServerSocket
 *    - SSLEngine (based on a nonblocking I/O model)
 * 
 * The TLS protocol (Transport Layer Security) version 1.3.
 *    TLS 1 is the successor of the SSL (Secure Socket Layer) 3.0 protocol.
 *    SSL 3.0 protocol has been deactivated in Java because it has security flaw.
 * The SSLEngine is an advanced API and beginners should use SSLSocket.
 * 
 * A KeyManager determines which authentication credentials to send to the remote host.
 * 
 * A TrustManager determines whether the remote authentication credentials
 *    (and thus the connection) should be trusted.
 */
/**
 * The launcher for insecure and secure sockets.<br>
 * Researched classes:
 * <ul>
 * <li>Socket
 * <li>ServerSocket
 * <li>SSLSocket
 * <li>SSLServerSocket
 * </ul>
 * The SSLEngine class is not researched here.<br>
 */
public class SocketsLauncher {

	static {
		System.setProperty("javax.net.ssl.keyStore", "src/main/resources/security/testkeys");
		System.setProperty("javax.net.ssl.keyStorePassword", "passphrase");
		System.setProperty("javax.net.ssl.trustStore", "src/main/resources/security/samplecacerts");
		System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
		/*- Enables the JSSE system debugging system property:
		System.setProperty("javax.net.debug", "all");
		 */
	}

	public static final boolean PROLIX = true;

	private static final int ITERATIONS = 5;

	private static final int RANDOM_TEXT_LENGTH = 100_000_000;

	private static final boolean USE_RANDOM_TEXTS_FROM_FILE = true;

	private static final RandomTextsGenerator RANDOM_TEXTS_GENERATOR = new RandomTextsGenerator(ITERATIONS,
			RANDOM_TEXT_LENGTH, USE_RANDOM_TEXTS_FROM_FILE);

	/**
	 * Processes the loop.
	 * 
	 * @param socketSecurityType the socket security type
	 */
	public static void processLoop(SocketSecurityType socketSecurityType) {

		final long[] elapsedArray = new long[ITERATIONS];
		final TheSockets sockets = TheSocketsFactory.buildSockets(socketSecurityType);
		final AtomicInteger atomicInteger = new AtomicInteger();
		final ExecutorService executorService = Executors.newFixedThreadPool(10);

		if (PROLIX) {
			System.out.println("✖	✖	✖");
		}
		final Instant startAll = Instant.now();
		for (int index = 0; index < ITERATIONS; index++) {
			process(sockets, atomicInteger, executorService, elapsedArray);
			if (PROLIX) {
				System.out.println("✖	✖	✖");
			}
		}
		if (Objects.nonNull(sockets)) {
			sockets.closeServerSocket();
		}
		final Instant endAll = Instant.now();
		executorService.shutdown();
		final LongSummaryStatistics stats = Arrays.stream(elapsedArray).summaryStatistics();
		System.out.printf("processLoop(): security type[%s], min[%03dms], avg[%3.1fms], max[%03dms], %s%n",
				socketSecurityType, stats.getMin(), stats.getAverage(), stats.getMax(),
				Utils.formatElapsed(startAll, endAll));
		System.out.println("- ".repeat(50));
	}

	/**
	 * Processes single loop item.
	 * 
	 * @param sockets         the sockets
	 * @param atomicInteger   the atomic integer
	 * @param executorService the executor service
	 * @param elapsedArray    the array of elapsed times
	 */
	private static void process(TheSockets sockets, AtomicInteger atomicInteger, ExecutorService executorService,
			long[] elapsedArray) {

		final Phaser endPhaser = new Phaser(3);
		final int number = atomicInteger.incrementAndGet();
		final Runnable serverTask = () -> sockets.runServer(endPhaser, RANDOM_TEXTS_GENERATOR.getTextForServer(number),
				number);
		final Runnable clientTask = () -> sockets.runClient(endPhaser, RANDOM_TEXTS_GENERATOR.getTextForClient(number),
				number);
		final Instant start = Instant.now();
		executorService.execute(serverTask);
		executorService.execute(clientTask);
		if (PROLIX) {
			System.out.printf("process():   number[%d], both tasks accepted for execution%n", number);
		}
		endPhaser.arriveAndAwaitAdvance();
		elapsedArray[number - 1] = Duration.between(start, Instant.now()).toMillis();
	}
}
