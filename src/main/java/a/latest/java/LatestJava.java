package a.latest.java;

import a.latest.java.declarations.Declarations;
import a.latest.java.subjects.About;
import a.latest.java.subjects.ArrayAndCollection;
import a.latest.java.subjects.DateAndTimeAggregation;
import a.latest.java.subjects.DateAndTimeChanging;
import a.latest.java.subjects.ExecuteTasks;
import a.latest.java.subjects.Parallelism;
import a.latest.java.subjects.SortingObjects;
import a.latest.java.subjects.StreamCollecting;
import a.latest.java.subjects.StreamFragmentation;
import a.latest.java.subjects.StringProcessing;
import a.latest.java.subjects.Var;

/**
 * The research of a latest JDK<br>
 * (Java Platform Standard Edition Development Kit).
 */
public class LatestJava {
	/**
	 * The execution control value.
	 */
	private static final boolean ONLY_DECLARATIONS = false;
	/**
	 * The execution control value.
	 */
	private static final boolean ALL = true;
	/**
	 * The execution control value.
	 */
	private static final boolean ABOUT = false;
	/**
	 * The execution control value.
	 */
	private static final boolean ARRAYS_AND_COLLECTIONS = false;
	/**
	 * The execution control value.
	 */
	private static final boolean DATE_AND_TIME_AGGREGATION = false;
	/**
	 * The execution control value.
	 */
	private static final boolean DATE_AND_TIME_CHANGING = false;
	/**
	 * The execution control value.
	 */
	private static final boolean EXECUTE_TASKS = false;
	/**
	 * The execution control value.
	 */
	private static final boolean PARALLELISM = false;
	/**
	 * The execution control value.
	 */
	private static final boolean SORTING_OBJECTS = false;
	/**
	 * The execution control value.
	 */
	private static final boolean STREAM_COLLECTING = false;
	/**
	 * The execution control value.
	 */
	private static final boolean STREAM_FRAGMENTATION = false;
	/**
	 * The execution control value.
	 */
	private static final boolean STRING_PROCESSING = false;
	/**
	 * The execution control value.
	 */
	private static final boolean VAR = false;

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		if (ONLY_DECLARATIONS) {
			Declarations.declareLocalVariables();
		}
		if (ALL || ABOUT) {
			About.showElapsed();
		}
		if (ALL || ABOUT) {
			About.listSystemProperties();
		}
		if (ALL || ABOUT) {
			About.showEnvironment();
		}
		if (ALL || ABOUT) {
			About.showFileStores();
		}
		if (ALL || ARRAYS_AND_COLLECTIONS) {
			ArrayAndCollection.showArraysMismatch();
		}
		if (ALL || ARRAYS_AND_COLLECTIONS) {
			ArrayAndCollection.multidimensionalArrayToMultidimensionalList();
		}
		if (ALL || ARRAYS_AND_COLLECTIONS) {
			ArrayAndCollection.apportionSet();
		}
		if (ALL || ARRAYS_AND_COLLECTIONS) {
			ArrayAndCollection.apportionMapAndMerge();
		}
		if (ALL || ARRAYS_AND_COLLECTIONS) {
			ArrayAndCollection.iterateOverVector();
		}
		if (ALL || DATE_AND_TIME_AGGREGATION) {
			DateAndTimeAggregation.aggregateLeapDays();
		}
		if (ALL || DATE_AND_TIME_AGGREGATION) {
			DateAndTimeAggregation.aggregateOneYearSeconds();
		}
		if (ALL || DATE_AND_TIME_CHANGING) {
			DateAndTimeChanging.formatDate();
		}
		if (ALL || DATE_AND_TIME_CHANGING) {
			DateAndTimeChanging.convertDateToAndFro();
		}
		if (ALL || DATE_AND_TIME_CHANGING) {
			DateAndTimeChanging.adjustDate();
		}
		if (ALL || DATE_AND_TIME_CHANGING) {
			DateAndTimeChanging.queryTemporalObjects();
		}
		if (ALL || DATE_AND_TIME_CHANGING) {
			DateAndTimeChanging.calculateAmountOfTimeBetween();
		}
		if (ALL || DATE_AND_TIME_CHANGING) {
			DateAndTimeChanging.addToOrSubtractFromInstant();
		}
		if (ALL || DATE_AND_TIME_CHANGING) {
			DateAndTimeChanging.fragmentizeTime();
		}
		if (ALL || EXECUTE_TASKS) {
			ExecuteTasks.futureWithGivenExecutor();
		}
		if (ALL || EXECUTE_TASKS) {
			ExecuteTasks.launchCompletableFutures();
		}
		if (ALL || EXECUTE_TASKS) {
			ExecuteTasks.completeSingleStageWithDependencies();
		}
		if (ALL || EXECUTE_TASKS) {
			ExecuteTasks.completeEitherOfTwoStages();
		}
		if (ALL || EXECUTE_TASKS) {
			ExecuteTasks.completeBothOfTwoStages();
		}
		if (ALL || EXECUTE_TASKS) {
			ExecuteTasks.safeFuture();
		}
		if (ALL || EXECUTE_TASKS) {
			ExecuteTasks.invokeCallableTasksCollection();
		}
		if (ALL || EXECUTE_TASKS) {
			ExecuteTasks.executeFutureTasks();
		}
		if (ALL || EXECUTE_TASKS) {
			ExecuteTasks.executeRunnablesAndCallables();
		}
		if (ALL || EXECUTE_TASKS) {
			ExecuteTasks.usePhaserToOpenGateForTasks();
		}
		if (ALL || EXECUTE_TASKS) {
			ExecuteTasks.usePhaserToAwaitOtherTasks();
		}
		if (ALL || PARALLELISM) {
			Parallelism.joinedStreams();
		}
		if (ALL || SORTING_OBJECTS) {
			SortingObjects.sortStream();
		}
		if (ALL || SORTING_OBJECTS) {
			SortingObjects.sortStreamWithLocale();
		}
		if (ALL || SORTING_OBJECTS) {
			SortingObjects.sortArray();
		}
		if (ALL || SORTING_OBJECTS) {
			SortingObjects.sortMapByKeyOrByValue();
		}
		if (ALL || SORTING_OBJECTS) {
			SortingObjects.sortWithComparableAndComparator();
		}
		if (ALL || STREAM_COLLECTING) {
			StreamCollecting.peekInStreamAndCollect();
		}
		if (ALL || STREAM_COLLECTING) {
			StreamCollecting.groupByDifferenceFromExpected();
		}
		if (ALL || STREAM_COLLECTING) {
			StreamCollecting.sourceListModifiedBeforeTheTerminalCollect();
		}
		if (ALL || STREAM_COLLECTING) {
			StreamCollecting.streamOfOptionals();
		}
		if (ALL || STREAM_COLLECTING) {
			StreamCollecting.createStreamFromNull();
		}
		if (ALL || STREAM_COLLECTING) {
			StreamCollecting.iterativeStream();
		}
		if (ALL || STREAM_COLLECTING) {
			StreamCollecting.traverseList();
		}
		if (ALL || STREAM_COLLECTING) {
			StreamCollecting.traverseQueue();
		}
		if (ALL || STREAM_COLLECTING) {
			StreamCollecting.traverseDeque();
		}
		if (ALL || STREAM_COLLECTING) {
			StreamCollecting.multidimensionalToFlat();
		}
		if (ALL || STREAM_COLLECTING) {
			StreamCollecting.nullsInStreamWithoutOptionals();
		}
		if (ALL || STREAM_COLLECTING) {
			StreamCollecting.nullsInStreamWithOptionals();
		}
		if (ALL || STREAM_FRAGMENTATION) {
			StreamFragmentation.skipAndLimit();
		}
		if (ALL || STREAM_FRAGMENTATION) {
			StreamFragmentation.dropAndTake();
		}
		if (ALL || STREAM_FRAGMENTATION) {
			StreamFragmentation.filter();
		}
		if (ALL || STRING_PROCESSING) {
			StringProcessing.countLetterFrequency();
		}
		if (ALL || STRING_PROCESSING) {
			StringProcessing.useStringJoiners();
		}
		if (ALL || STRING_PROCESSING) {
			StringProcessing.getResultsWithStringProducer();
		}
		if (ALL || VAR) {
			Var.launch();
		}
	}
}