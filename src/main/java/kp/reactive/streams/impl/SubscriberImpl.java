package kp.reactive.streams.impl;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

/**
 * The subscriber implementation.
 *
 */
public class SubscriberImpl<T> implements Subscriber<T> {

	private Subscription subscription;

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void onSubscribe(Subscription subscriptionParam) {

		subscription = subscriptionParam;
		subscription.request(1);
		System.out.println("SubscriberImpl.onSubscribe():");
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void onNext(T item) {

		subscription.request(1);
		System.out.printf("SubscriberImpl.onNext(): item[%s]%n", item.toString());
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void onError(Throwable throwable) {

		System.out.printf("SubscriberImpl.onError(): throwable[%s]%n", throwable.getMessage());
		throwable.printStackTrace();
		System.exit(1);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void onComplete() {

		System.out.println("SubscriberImpl.onComplete():");
	}
}
