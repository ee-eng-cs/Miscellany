package kp.reactive.streams;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

import kp.reactive.streams.impl.PeriodicPublisher;
import kp.reactive.streams.impl.ProcessorImpl;
import kp.reactive.streams.impl.PublisherImpl;
import kp.reactive.streams.impl.SubscriberImpl;
import kp.utils.Utils;

/*-
 * Asynchronous streams of data with non-blocking back pressure.
 * https://community.oracle.com/docs/DOC-1006738
 * 
 * http://www.reactive-streams.org/
 * https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/Flow.html
 * https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/SubmissionPublisher.html
 * https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpResponse.BodySubscribers.html
 * https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpRequest.BodyPublishers.html
 */
/**
 * The main launcher for reactive streams research.<br>
 * The Flow APIs correspond to the Reactive Streams Specification.
 * 
 */
public class MainForReactiveStreams {

	private static final boolean ALL = true;
	private static final boolean FLOW = false;
	private static final boolean WEB = false;

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		if (ALL || FLOW) {
			launchSubscriberImpl();
		}
		if (ALL || FLOW) {
			launchPeriodicPublisher();
		}
		if (ALL || FLOW) {
			launchProcessorImpl();
		}
		if (ALL || FLOW) {
			launchPublisherImpl();
		}
		if (ALL || WEB) {
			WebWithSubscribers.receiveResponseUsingLineSubscriber();
		}
		if (ALL || WEB) {
			WebWithSubscribers.receiveResponseUsingListSubscriber();
		}
		if (ALL || WEB) {
			WebWithSubscribers.receiveResponseUsingPublisher();
		}
	}

	/**
	 * Launches simple subscriber implementation.
	 * 
	 */
	private static void launchSubscriberImpl() {

		final Stream<String> stream = Stream.of("A", "B", "C");
		final SubmissionPublisher<String> submissionPublisher = new SubmissionPublisher<>();
		final CompletableFuture<Void> future = submissionPublisher
				.consume(item -> System.out.printf("→ Consumed item[%s]%n", item));
		final Subscriber<String> subscriber = new SubscriberImpl<>();

		try (submissionPublisher) {
			submissionPublisher.subscribe(subscriber);
			stream.forEach(submissionPublisher::submit);
			Utils.sleepMillis(10);
		}
		CompletableFuture.allOf(future).join();
		System.out.println("- ".repeat(50));
	}

	/**
	 * Launches periodic publisher.
	 * 
	 */
	private static void launchPeriodicPublisher() {

		final AtomicInteger sequenceNumber = new AtomicInteger(0);
		System.out.printf("sequenceNumber[%d]%n", sequenceNumber.get());
		// with period 10ms the results are unrepeatable
		final int PERIOD = 100;
		final PeriodicPublisher<Integer> periodicPublisher = new PeriodicPublisher<>(sequenceNumber::getAndIncrement,
				PERIOD, TimeUnit.MILLISECONDS);
		final CompletableFuture<Void> future = periodicPublisher
				.consume(str -> System.out.printf("→ Consumed item[%s]%n", str));
		final Subscriber<Integer> subscriber = new SubscriberImpl<>();
		System.out.printf("sequenceNumber[%d]%n", sequenceNumber.get());
		try (periodicPublisher) {
			periodicPublisher.subscribe(subscriber);
			Utils.sleepMillis(3 * PERIOD);
		}
		CompletableFuture.allOf(future).join();
		Utils.sleepMillis(PERIOD);
		System.out.println("- ".repeat(50));
	}

	/**
	 * Launches simple processor implementation.
	 * 
	 */
	private static void launchProcessorImpl() {

		// run the processor with an error because it is closed too soon
		boolean RUN_WITH_ERROR = false;

		final Stream<String> stream = Stream.of("A", "B", "C");
		final SubmissionPublisher<String> submissionPublisher = new SubmissionPublisher<>();
		final Function<String, String> function = str -> Character.toString((char) (str.codePointAt(0) + 10));
		final ProcessorImpl<String, String> processorImpl = new ProcessorImpl<>(function);
		final CompletableFuture<Void> future = processorImpl
				.consume(str -> System.out.printf("→ Consumed item[%s]%n", str));
		final Subscriber<String> subscriber = new SubscriberImpl<>();

		try (submissionPublisher; processorImpl) {
			submissionPublisher.subscribe(processorImpl);
			processorImpl.subscribe(subscriber);
			stream.forEach(submissionPublisher::submit);
			Utils.sleepMillis(RUN_WITH_ERROR ? 0 : 100);
		}
		CompletableFuture.allOf(future).join();
		Utils.sleepMillis(100);
		System.out.println("- ".repeat(50));
	}

	/**
	 * Launches simple publisher implementation.
	 * 
	 */
	private static void launchPublisherImpl() {

		final PublisherImpl publisherImpl = new PublisherImpl();
		final Subscriber<String> subscriber = new SubscriberImpl<>();
		try (publisherImpl) {
			publisherImpl.subscribe(subscriber);
		}
		Utils.sleepMillis(10);
		System.out.println("- ".repeat(50));
	}
}
