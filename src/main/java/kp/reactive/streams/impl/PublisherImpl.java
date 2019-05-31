package kp.reactive.streams.impl;

import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

/**
 * The publisher implementation.
 *
 */
public class PublisherImpl implements Publisher<String>, AutoCloseable {

	private Subscriber<? super String> subscriber;
	private Subscription subscription;

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void subscribe(Subscriber<? super String> subscriberParam) {

		subscriber = subscriberParam;
		subscription = new SubscriptionImpl(subscriber);
		subscriber.onSubscribe(subscription);
		System.out.println("PublisherImpl.subscribe():");
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void close() {

		subscription.cancel();
		System.out.println("PublisherImpl.close():");
	}
}
