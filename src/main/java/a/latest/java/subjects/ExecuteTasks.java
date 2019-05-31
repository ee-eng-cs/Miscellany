package a.latest.java.subjects;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import kp.utils.Utils;

/**
 * Execute tasks.<br>
 * Researched classes:
 * <ul>
 * <li>Runnable
 * <li>Callable
 * <li>Future
 * <li>CompletableFuture
 * <li>FutureTask
 * <li>ExecutorService
 * <li>ForkJoinPool
 * <li>ThreadFactory
 * </ul>
 */
public interface ExecuteTasks {
	/**
	 * Completes the future with the given executor.
	 * 
	 */
	public static void futureWithGivenExecutor() {

		final Map<String, ExecutorService> poolMap = Map.of(/*-*/
				/*- For fixed size thread pool it sets up a thread pool with one thread per core
				    which is appropriate for CPU bound tasks */
				"A", Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()), /*-*/
				"B", Executors.newCachedThreadPool(), /*- unbounded thread pool, with automatic thread reclamation */
				"C",
				new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
						new LinkedBlockingQueue<Runnable>()), /*- constructor from 'newSingleThreadExecutor()' code */
				"D", Executors.newSingleThreadExecutor(), /*- single background thread */
				"E", Executors.newWorkStealingPool(), /*-*/
				"F", new ForkJoinPool()/*-*/
		);
		final Consumer<String> keyConsumer = key -> {
			final ExecutorService pool = poolMap.get(key);
			final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "supplied value", pool);
			final String result;
			try {
				result = future.get();
			} catch (InterruptedException | ExecutionException e) {
				pool.shutdownNow();
				throw new IllegalStateException(e);
			}
			shutdownAndAwaitTermination(pool);
			System.out.printf("Pool %s: future result[%s], pool class[%s]%n", key, result, pool.getClass().getName());
		};
		poolMap.keySet().stream().sorted().forEach(keyConsumer);
		System.out.println("- ".repeat(50));
	}

	/**
	 * Shutdowns pool and awaits termination.<br>
	 * The way of pool shutdown recommended by Oracle.
	 * 
	 * @param pool the pool
	 */
	private static void shutdownAndAwaitTermination(ExecutorService pool) {

		pool.shutdown(); // Disable new tasks from being submitted
		try {
			if (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
				pool.shutdownNow(); // Cancel currently executing tasks
				if (!pool.awaitTermination(1, TimeUnit.SECONDS))
					System.err.println("Pool did not terminate");
			}
		} catch (InterruptedException e) {
			pool.shutdownNow();
			Thread.currentThread().interrupt();// Preserve interrupt status
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Launches simple completable futures.
	 * 
	 */
	public static void launchCompletableFutures() {

		final CompletableFuture<String> completableFuture1 = CompletableFuture.supplyAsync(() -> "supplied value 1");
		final CompletableFuture<String> completableFuture2 = new CompletableFuture<String>()
				.completeAsync(() -> "supplied value 2");
		final CompletableFuture<String> completableFuture3 = new CompletableFuture<String>().newIncompleteFuture();
		completableFuture3.complete("completing value 3");
		// If you already know the result of a computation
		final Future<String> completableFuture4 = CompletableFuture.completedFuture("completing value 4");
		System.out.printf("Completable futures are completed: 1[%S], 2[%S], 3[%S], 4[%S]%n",
				completableFuture1.isDone(), completableFuture2.isDone(), completableFuture3.isDone(),
				completableFuture4.isDone());
		try {
			System.out.printf("Completable futures results: 1[%s], 2[%s], 3[%s], 4[%s]%n", completableFuture1.get(),
					completableFuture2.get(), completableFuture3.get(), completableFuture4.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("- ".repeat(50));
	}

	/**
	 * Completes the single completion stage with all its dependencies.
	 * 
	 */
	public static void completeSingleStageWithDependencies() {

		final boolean withSleep = false;
		System.out.println("The state reported at Java code points A, B, C, D, E, F.");
		final StringBuffer report = new StringBuffer();
		final CompletableFuture<String> future1 = new CompletableFuture<String>();
		final Supplier<String> supplier = () -> {
			if (withSleep) {
				Utils.sleepSeconds(1);
			}
			report.append("«Completed»");
			return "A";
		};
		final CompletableFuture<String> future2 = future1.completeAsync(supplier);
		System.out.printf("At point A: %s%n", report.toString());
		final Function<String, String> functionAppl = str -> {
			report.append(" → «Then-Applied-Function»");
			return str.concat("-B");
		};
		final CompletableFuture<String> future3 = future2.thenApply(functionAppl);
		System.out.printf("At point B: %s%n", report.toString());
		final CompletableFuture<String> alternativeFuture = new CompletableFuture<String>();
		final Function<String, CompletableFuture<String>> functionComp = str -> {
			report.append(" → «Then-Composed-Function»");
			alternativeFuture.completeAsync(() -> str.concat("-C"));
			return alternativeFuture;
		};
		final CompletableFuture<String> future4 = future3.thenCompose(functionComp);
		System.out.printf("At point C: %s%n", report.toString());
		final CompletableFuture<Void> future5 = future4.thenAccept(str -> report.append(" → «Then-Accepted-Consumer»"));
		System.out.printf("At point D: %s%n", report.toString());
		final CompletableFuture<Void> future6 = future5.thenRun(() -> report.append(" → «Then-Run-Runnable»"));
		System.out.printf("At point E: %s%n", report.toString());

		final int choice = 1;
		try {
			switch (choice) {
			case 1:
				System.out.printf("future1 result[%s]%n", future1.get());
				break;
			case 2:
				System.out.printf("future1 result[%s]%n", future1.get());
				System.out.printf("future2 result[%s]%n", future2.get());
				System.out.printf("future3 result[%s]%n", future3.get());
				System.out.printf("future4 result[%s]%n", future4.get());
				break;
			case 3:
				System.out.printf("future6 result[%s]%n", future6.get());
				break;
			default:
				break;
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.printf("At point F: %s%n", report.toString());
		System.out.println("- ".repeat(50));
	}

	/**
	 * Completes either of two stages: slow versus fast.
	 * 
	 */
	public static void completeEitherOfTwoStages() {

		final LongAdder counter = new LongAdder();
		final CompletableFuture<String> future100 = CompletableFuture.supplyAsync(() -> {
			Utils.sleepMillis(100);
			counter.increment();
			return "100MS-ACTION";
		});
		final CompletableFuture<String> future10 = CompletableFuture.supplyAsync(() -> {
			Utils.sleepMillis(10);
			counter.increment();
			return "10MS-ACTION";
		});
		final CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
			Utils.sleepMillis(1);
			counter.increment();
			return "1MS-ACTION";
		});
		// rather unstable results
		future10.acceptEitherAsync(future1,
				arg -> System.out.printf("Accepted either  '10MS-ACTION' or   '1MS-ACTION': [%12s]%n", arg));
		future10.acceptEitherAsync(future100,
				arg -> System.out.printf("Accepted either  '10MS-ACTION' or '100MS-ACTION': [%12s]%n", arg));

		future1.applyToEitherAsync(future10,
				arg -> System.out.printf("Applied  either   '1MS-ACTION' or ' 10MS-ACTION': [%12s]%n", arg));
		future100.applyToEitherAsync(future10,
				arg -> System.out.printf("Applied  either '100MS-ACTION' or ' 10MS-ACTION': [%12s]%n", arg));
		do {
			Utils.sleepMillis(10);
		} while (counter.sum() < 3);
		System.out.println("- ".repeat(50));
	}

	/**
	 * Completes both of two stages.
	 * 
	 */
	public static void completeBothOfTwoStages() {

		final CompletableFuture<String> futureOne = CompletableFuture.supplyAsync(() -> "One");
		final CompletableFuture<String> futureTwo = CompletableFuture.supplyAsync(() -> "Two");
		final CompletableFuture<String> futureOneApplied = futureOne.thenApply(arg -> arg.concat("➕applied"));
		final CompletableFuture<String> futureTwoApplied = futureTwo.thenApply(arg -> arg.concat("➕applied"));

		final BiConsumer<String, String> consumer = (arg1, arg2) -> System.out.printf(/*-*/
				"Accepted both (output from consumer)  : [%s] AND [%s]%n", arg1, arg2);
		futureOneApplied.thenAcceptBothAsync(futureTwoApplied, consumer);
		futureOne.thenAcceptBothAsync(futureTwo, consumer);

		final Runnable runnable = () -> {
			try {
				System.out.printf("Run      both (output from runnable)  : [%s] AND [%s]%n", futureOne.get(),
						futureTwo.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
				System.exit(1);
			}
		};
		futureOne.runAfterBothAsync(futureTwo, runnable);

		final BiFunction<String, String, String> function = (arg1, arg2) -> arg1.concat(" & ").concat(arg2);
		final CompletableFuture<String> futureCombined = futureOne.thenCombineAsync(futureTwo, function);
		try {
			System.out.printf("Combined both (combined future result): [%s]%n", futureCombined.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			System.exit(1);
		}
		Utils.sleepMillis(10);
		System.out.println("- ".repeat(50));
	}

	/**
	 * Completes some futures in a safe way.
	 * 
	 */
	public static void safeFuture() {

		try {
			safeFuture(true, true);
			System.out.println("✖\t".repeat(8));
			safeFuture(true, false);
			System.out.println("✖\t".repeat(8));
			safeFuture(false, false);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("- ".repeat(50));
	}

	/**
	 * Completes the future in a safe way.
	 * 
	 * @param setDivisorToZero         the flag for setting divisor to zero
	 * @param withoutGetFromFutureCall the flag for switching off get call
	 * @throws InterruptedException the interrupted exception
	 * @throws ExecutionException   the concurrent exception
	 */
	public static void safeFuture(boolean setDivisorToZero, boolean withoutGetFromFutureCall)
			throws InterruptedException, ExecutionException {

		final int[] divisor = { 1 };

		final Function<Throwable, Integer> exceptionHandler = ex -> {
			System.out.printf("Handled exception: message [%s], returning '-1'%n", ex.getMessage());
			return -1;
		};
		final CompletableFuture<Integer> futureThrowingException = CompletableFuture
				.supplyAsync(() -> 100 / divisor[0]);
		if (setDivisorToZero) {
			// Here the value is zeroed and in the next assignment it is reset to normal.
			// That is enough to cause the ArithmeticException!
			divisor[0] = 0;
		}
		System.out.printf("divisor[%d]%n", divisor[0]);
		divisor[0] = 1;
		System.out.printf("divisor[%d]%n", divisor[0]);
		final CompletableFuture<Integer> safeFutureWithExceptionally = futureThrowingException
				.exceptionally(exceptionHandler);
		final CompletableFuture<Integer> safeFutureWithHandle = futureThrowingException.handle(/*-*/
				(ok, exc) -> Objects.nonNull(ok) ? ok : exceptionHandler.apply(exc));

		if (withoutGetFromFutureCall) {
			System.out.println("Run without any 'get()' call.");
		} else {
			System.out.printf("Safe future results: 'with exceptionally'[%d], 'with handle'[%d]%n",
					safeFutureWithExceptionally.get(), safeFutureWithHandle.get());
		}
		Utils.sleepMillis(50);
	}

	/**
	 * Invokes callable tasks collection using executor service.
	 * 
	 */
	public static void invokeCallableTasksCollection() {

		final ExecutorService pool = Executors.newFixedThreadPool(10);
		final List<Callable<String>> callableList = List.of(/*-*/
				() -> "from callable task A", () -> "from callable task B");
		List<Future<String>> futureList = null;
		try {
			futureList = pool.invokeAll(callableList);
		} catch (InterruptedException e) {
			pool.shutdownNow();
			throw new IllegalStateException(e);
		}
		final Consumer<Future<String>> futureConsumer = future -> {
			try {
				System.out.printf("Consumed future result[%s]%n", future.get());
			} catch (InterruptedException | ExecutionException e) {
				pool.shutdownNow();
				throw new IllegalStateException(e);
			}
		};
		futureList.stream().forEach(futureConsumer);
		pool.shutdownNow();
		System.out.println("- ".repeat(50));
	}

	/**
	 * Executes future tasks with runnable or callable.
	 * 
	 */
	public static void executeFutureTasks() {

		final ExecutorService pool = Executors.newFixedThreadPool(10);
		// The FutureTask wraps a Callable object.
		final FutureTask<String> futureTaskRun​ = new FutureTask<>(() -> System.out.println("«runnable action 1»"),
				"successful completion result");
		// The FutureTask wraps a Runnable object.
		final FutureTask<String> futureTaskCal = new FutureTask<>(() -> "callable result");
		pool.execute(futureTaskRun​);
		pool.execute(futureTaskCal);
		System.out.printf("Future task is completed: R[%5S], C[%5S]%n", futureTaskRun​.isDone(),
				futureTaskCal.isDone());
		try {
			System.out.printf("Future task results: R[%s], C[%s]%n", futureTaskRun​.get(), futureTaskCal.get());
		} catch (InterruptedException | ExecutionException e) {
			pool.shutdownNow();
			throw new IllegalStateException(e);
		}
		System.out.printf("Future task is completed: R[%5S], C[%5S]%n", futureTaskRun​.isDone(),
				futureTaskCal.isDone());
		pool.shutdownNow();
		System.out.println("- ".repeat(50));
	}

	/**
	 * Executes various runnables and callables.
	 * 
	 */
	public static void executeRunnablesAndCallables() {

		final ExecutorService pool = Executors.newFixedThreadPool(10);
		final Thread thread = new Thread(() -> System.out.print("«action 1»"));
		final ThreadFactory threadFactory = Executors.defaultThreadFactory();
		final Thread threadFromFactory = threadFactory.newThread(() -> System.out.print("«action 2»"));
		System.out.printf("Thread names: thread[%s], threadFromFactory[%s]%n", thread.getName(),
				threadFromFactory.getName());

		System.out.println("Runnable actions (with '➕'  - Java code line markers):");
		System.out.print("   ➕1  ");
		thread.start();
		Utils.sleepMillis(10);
		System.out.print(" ➕2  ");
		threadFromFactory.start();
		pool.execute(() -> System.out.print("«action 3»"));
		System.out.print(" ➕3  ");
		CompletableFuture.runAsync(() -> System.out.print("«action 4»"), pool);
		System.out.print(" ➕4  ");

		/*- 3 alternatives of using the 'pool.submit(...)' */
		final Future<?> taskFutureRun = pool.submit(() -> System.out.print("«action 5»"));
		System.out.print(" ➕5  ");
		final Runnable runnable = () -> System.out.print("«action 6»");
		System.out.print(" ➕6  ");
		final Future<?> taskFutureCalFromRun = pool.submit(Executors.callable(runnable, "callable task result (R►C)"));
		final Future<?> taskFutureCal = pool.submit(() -> "callable task result (C)");
		System.out.printf(" ➕7%n%n");

		System.out.printf("Future task is completed: 'runnable'[%5S], 'runnable►callable'[%5S], 'callable'[%5S]%n",
				taskFutureRun.isDone(), taskFutureCalFromRun.isDone(), taskFutureCal.isDone());
		try {
			// Future from runnable returns null if the task has finished correctly.
			System.out.printf("Future results: 'runnable'[%s], 'runnable►callable'[%s], 'callable'[%s]%n",
					taskFutureRun.get(), taskFutureCalFromRun.get(), taskFutureCal.get());
		} catch (InterruptedException | ExecutionException e) {
			pool.shutdownNow();
			throw new IllegalStateException(e);
		}
		pool.shutdownNow();
		System.out.println("- ".repeat(50));
	}

	/**
	 * Uses the <B>Phaser</B> to open the gate for tasks.<br>
	 * The <B>Phaser</B> is a reusable synchronization barrier.
	 * 
	 */
	public static void usePhaserToOpenGateForTasks() {

		final List<Runnable> tasks = List.of(/*-*/
				() -> System.out.print("«Task A»\t"), /*-*/
				() -> System.out.print("«Task B»\t"), /*-*/
				() -> System.out.print("«Task C»\t"));

		final Phaser startingGate = new Phaser(1); // "1" to register self
		System.out.println("(1) before tasks starting");
		for (Runnable task : tasks) {
			startingGate.register();
			CompletableFuture.runAsync(() -> startingGate.arriveAndAwaitAdvance()).thenRunAsync(task);
		}
		System.out.println("(2) after  tasks starting");
		Utils.sleepMillis(10);
		// deregister self to allow threads to proceed
		startingGate.arriveAndDeregister();
		System.out.println("(3) after deregistration");
		Utils.sleepMillis(10);
		System.out.println("\n(4) after sleep");
		System.out.println("- ".repeat(50));
	}

	/**
	 * Uses the <B>Phaser</B> to await all other tasks.<br>
	 * The <B>Phaser</B> is a reusable synchronization barrier.
	 * 
	 */
	public static void usePhaserToAwaitOtherTasks() {

		final AtomicInteger index = new AtomicInteger();
		final ExecutorService pool = Executors.newFixedThreadPool(10);
		for (int i = 1; i <= 3; i++) {
			index.getAndIncrement();
			final Phaser phaser = new Phaser(4);
			final Runnable taskA = () -> taskAction("A", phaser, index);
			final Runnable taskB = () -> taskAction("B", phaser, index);
			final Runnable taskC = () -> taskAction("C", phaser, index);
			System.out.printf("▼▼▼      index[%d], before tasks executing ▼▼▼%n", index.get());
			pool.execute(taskA);
			pool.execute(taskB);
			pool.execute(taskC);

			System.out.printf("►                           index[%d], arriving at the phaser ◄%n", index.get());
			phaser.arriveAndAwaitAdvance();
			System.out.printf("▲▲▲      index[%d], after awaiting others ▲▲▲%n%n", index.get());
		}
		pool.shutdown();
		System.out.println("- ".repeat(50));
	}

	/**
	 * Executes task action with <B>Phaser</B>.
	 * 
	 * @param label  the label
	 * @param phaser the phaser
	 * @param index  the index
	 */
	private static void taskAction(String label, Phaser phaser, AtomicInteger index) {

		System.out.printf("task[%s], index[%d], START%n", label, index.get());
		Utils.sleepMillis((int) (30 * Math.random()));
		System.out.printf("task[%s], index[%d], FINISH%n", label, index.get());
		phaser.arriveAndDeregister();
	}
}