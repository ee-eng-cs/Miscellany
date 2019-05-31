package kp.reactive.streams.impl;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

/**
 * The subscriber of the byte buffer list.
 * 
 */
public class SubscriberImplForByteBufferList implements Subscriber<List<ByteBuffer>> {

	private Subscription subscription;

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void onSubscribe(Subscription subscriptionParam) {

		subscription = subscriptionParam;
		subscription.request(1);
		System.out.println("SubscriberImplForByteBufferList.onSubscribe():");
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void onNext(List<ByteBuffer> byteBufferList) {

		subscription.request(1);
		for (int i = 0; i < byteBufferList.size(); i++) {
			System.out.printf("SubscriberImplForByteBufferList.onNext(): "/*-*/
					+ "list item with index [%d],%n   received 'ByteBuffer' has bytes:", i);
			while (byteBufferList.get(i).hasRemaining()) {
				System.out.printf(" '%c'", byteBufferList.get(i).get());
			}
			System.out.println();
		}
		System.out.printf("SubscriberImplForByteBufferList.onNext(): byteBufferList size[%s]%n", byteBufferList.size());
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void onError(Throwable throwable) {

		System.out.printf("SubscriberImplForByteBufferList.onError(): throwable[%s]%n", throwable.getMessage());
		throwable.printStackTrace();
		System.exit(1);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void onComplete() {

		System.out.println("SubscriberImplForByteBufferList.onComplete():");
	}
}
