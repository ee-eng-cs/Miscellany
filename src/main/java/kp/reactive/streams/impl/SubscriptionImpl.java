package kp.reactive.streams.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The subscription implementation with limited sequence numbers.
 *
 */
public class SubscriptionImpl implements Subscription {

	private final int LIMIT = 2;

	private final Subscriber<? super String> subscriber;
	private final AtomicInteger sequenceNumber = new AtomicInteger(0);
	private final ExecutorService executor = ForkJoinPool.commonPool();
	private Future<?> future;

	/**
	 * Constructor.
	 * 
	 * @param subscriber the subscriber
	 */
	public SubscriptionImpl(Subscriber<? super String> subscriber) {

		this.subscriber = subscriber;
		System.out.println("SubscriptionImpl():");
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void request(long n) {

		int seq = sequenceNumber.incrementAndGet();
		if (seq > LIMIT) {
			subscriber.onComplete();
			System.out.printf("SubscriptionImpl.request(): over limit, item[%d], n[%d]%n", seq, n);
			return;
		}
		future = executor.submit(() -> subscriber.onNext(String.valueOf(seq)));
		System.out.printf("SubscriptionImpl.request(): item[%d], n[%d]%n", seq, n);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void cancel() {

		if (future != null) {
			future.cancel(false);
		}
		System.out.println("SubscriptionImpl.cancel():");
	}
}
