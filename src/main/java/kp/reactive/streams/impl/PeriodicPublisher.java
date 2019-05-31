package kp.reactive.streams.impl;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/*-
 * Based on 'PeriodicPublisher':
 * https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/SubmissionPublisher.html
 */
/**
 * The periodic publisher;
 * 
 */
public class PeriodicPublisher<T> extends SubmissionPublisher<T> {

	private final ScheduledFuture<?> periodicTask;
	private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);

	/**
	 * Constructor.
	 * 
	 * @param supplier the supplier
	 * @param period   the period
	 * @param unit     the unit
	 */
	public PeriodicPublisher(Supplier<? extends T> supplier, long period, TimeUnit unit) {

		super();
		this.periodicTask = scheduler.scheduleAtFixedRate(() -> submit(supplier.get()), 0, period, unit);
		System.out.printf("PeriodicPublisher(): period[%d], unit[%s]%n", period, unit.name());
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void close() {

		periodicTask.cancel(false);
		scheduler.shutdown();
		super.close();
		System.out.println("PeriodicPublisher.close():");
	}
}