package kp.reactive.streams.impl;

import java.util.concurrent.Flow.Processor;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Function;

/*-
 * Based on 'TransformProcessor':
 * https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/SubmissionPublisher.html
 */
/**
 * The processor implementation.
 *
 * @param <S> the subscribed item type
 * @param <T> the published item type
 */
public class ProcessorImpl<S, T> extends SubmissionPublisher<T> implements Processor<S, T> {

	private final Function<? super S, ? extends T> function;
	private Subscription subscription;

	/**
	 * Constructor.
	 * 
	 * @param function the function
	 */
	public ProcessorImpl(Function<? super S, ? extends T> function) {

		super();
		this.function = function;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void onSubscribe(Subscription subscription) {

		this.subscription = subscription;
		this.subscription.request(1);
		System.out.println("ProcessorImpl.onSubscribe():");
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void onNext(S item) {

		subscription.request(1);
		submit(function.apply(item));
		System.out.printf("ProcessorImpl.onNext(): item[%s]%n", item.toString());
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void onError(Throwable throwable) {

		closeExceptionally(throwable);
		System.out.printf("ProcessorImpl.onError(): throwable[%s]%n", throwable.getMessage());
		throwable.printStackTrace();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void onComplete() {

		close();
		System.out.println("ProcessorImpl.onComplete():");
	}
}