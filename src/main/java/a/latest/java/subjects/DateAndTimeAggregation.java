package a.latest.java.subjects;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import kp.utils.Utils;

/**
 * Date and time aggregation.
 *
 */
public interface DateAndTimeAggregation {

	/**
	 * Aggregates leap days.
	 * 
	 */
	public static void aggregateLeapDays() {

		System.out.println("Leap years by day of week:");
		final Stream<LocalDate> leapDayStream = LocalDate.of(1904, 2, 29)/*-*/
				.datesUntil(LocalDate.of(2104, 2, 29), Period.ofYears(4))/*-*/
				.filter(date -> 29 == date.getDayOfMonth());
		final Collector<LocalDate, ?, Map<DayOfWeek, List<Integer>>> leapYearCollector = Collectors
				.groupingBy(LocalDate::getDayOfWeek, Collectors.mapping(LocalDate::getYear, Collectors.toList()));

		final Map<DayOfWeek, List<Integer>> leapYearsMap = leapDayStream.collect(leapYearCollector);
		final Consumer<DayOfWeek> dayConsumer = day -> System.out.printf("Day[%9s], leap years%s%n", day,
				leapYearsMap.get(day));
		leapYearsMap.keySet().stream().forEach(dayConsumer);
		System.out.println("- ".repeat(50));
	}

	/**
	 * Aggregates seconds from one year.
	 * 
	 */
	public static void aggregateOneYearSeconds() {

		Instant start = Instant.now();
		final Stream<LocalDateTime> dateStream = createDateStream(1, ChronoUnit.YEARS);
		System.out.printf("Got list. %s%n", Utils.formatElapsed(start, Instant.now()));

		final Map<Integer, ? extends Map<Month, ? extends Map<Integer, ? extends Map<Integer, ? extends Map<Integer, ? extends Map<Integer, List<LocalDateTime>>>>>>>
		/*-*/ datesByFieldsMap;
		final Collector<LocalDateTime, ?, ? extends Map<Integer, ? extends Map<Month, ? extends Map<Integer, ? extends Map<Integer, ? extends Map<Integer, ? extends Map<Integer, List<LocalDateTime>>>>>>>>
		/*-*/ dateCollector;

		if (!true) {
			dateCollector = getDateCollector();
			start = Instant.now();
			datesByFieldsMap = dateStream.collect(dateCollector);
			System.out.printf("Stream process finished. %s%n", Utils.formatElapsed(start, Instant.now()));
		} else {
			dateCollector = getDateCollectorForConcurrent();
			start = Instant.now();
			datesByFieldsMap = dateStream.parallel().collect(dateCollector);
			System.out.printf("Parallel stream process finished. %s%n", Utils.formatElapsed(start, Instant.now()));
		}
		showDateTimeContent(datesByFieldsMap);
		System.out.println("- ".repeat(50));
	}

	/**
	 * Gets date collector.
	 * 
	 * @return the year collector
	 */
	private static Collector<LocalDateTime, ?, Map<Integer, Map<Month, Map<Integer, Map<Integer, Map<Integer, Map<Integer, List<LocalDateTime>>>>>>>> getDateCollector() {

		final Collector<LocalDateTime, ?, Map<Integer, List<LocalDateTime>>>
		/*-*/ secondCollector = Collectors.groupingBy(LocalDateTime::getSecond);
		final Collector<LocalDateTime, ?, Map<Integer, Map<Integer, List<LocalDateTime>>>>
		/*-*/ minuteCollector = Collectors.groupingBy(LocalDateTime::getMinute, TreeMap::new, secondCollector);
		final Collector<LocalDateTime, ?, Map<Integer, Map<Integer, Map<Integer, List<LocalDateTime>>>>>
		/*-*/ hourCollector = Collectors.groupingBy(LocalDateTime::getHour, TreeMap::new, minuteCollector);
		final Collector<LocalDateTime, ?, Map<Integer, Map<Integer, Map<Integer, Map<Integer, List<LocalDateTime>>>>>>
		/*-*/ dayCollector = Collectors.groupingBy(LocalDateTime::getDayOfMonth, TreeMap::new, hourCollector);
		final Collector<LocalDateTime, ?, Map<Month, Map<Integer, Map<Integer, Map<Integer, Map<Integer, List<LocalDateTime>>>>>>>
		/*-*/ monthCollector = Collectors.groupingBy(LocalDateTime::getMonth, TreeMap::new, dayCollector);
		final Collector<LocalDateTime, ?, Map<Integer, Map<Month, Map<Integer, Map<Integer, Map<Integer, Map<Integer, List<LocalDateTime>>>>>>>>
		/*-*/ yearCollector = Collectors.groupingBy(LocalDateTime::getYear, TreeMap::new, monthCollector);
		return yearCollector;
	}

	/**
	 * Gets date collector for concurrent.
	 * 
	 * @return the year collector
	 */
	private static Collector<LocalDateTime, ?, ConcurrentMap<Integer, ConcurrentMap<Month, ConcurrentMap<Integer, ConcurrentMap<Integer, ConcurrentMap<Integer, ConcurrentMap<Integer, List<LocalDateTime>>>>>>>> getDateCollectorForConcurrent() {

		final Collector<LocalDateTime, ?, ConcurrentMap<Integer, List<LocalDateTime>>>
		/*-*/ secondCollector = Collectors.groupingByConcurrent(LocalDateTime::getSecond, ConcurrentSkipListMap::new,
				Collectors.mapping(Function.identity(), Collectors.toList()));
		final Collector<LocalDateTime, ?, ConcurrentMap<Integer, ConcurrentMap<Integer, List<LocalDateTime>>>>
		/*-*/ minuteCollector = Collectors.groupingByConcurrent(LocalDateTime::getMinute, ConcurrentSkipListMap::new,
				secondCollector);
		final Collector<LocalDateTime, ?, ConcurrentMap<Integer, ConcurrentMap<Integer, ConcurrentMap<Integer, List<LocalDateTime>>>>>
		/*-*/ hourCollector = Collectors.groupingByConcurrent(LocalDateTime::getHour, ConcurrentSkipListMap::new,
				minuteCollector);
		final Collector<LocalDateTime, ?, ConcurrentMap<Integer, ConcurrentMap<Integer, ConcurrentMap<Integer, ConcurrentMap<Integer, List<LocalDateTime>>>>>>
		/*-*/ dayCollector = Collectors.groupingByConcurrent(LocalDateTime::getDayOfMonth, ConcurrentSkipListMap::new,
				hourCollector);
		final Collector<LocalDateTime, ?, ConcurrentMap<Month, ConcurrentMap<Integer, ConcurrentMap<Integer, ConcurrentMap<Integer, ConcurrentMap<Integer, List<LocalDateTime>>>>>>>
		/*-*/ monthCollector = Collectors.groupingByConcurrent(LocalDateTime::getMonth, ConcurrentSkipListMap::new,
				dayCollector);
		final Collector<LocalDateTime, ?, ConcurrentMap<Integer, ConcurrentMap<Month, ConcurrentMap<Integer, ConcurrentMap<Integer, ConcurrentMap<Integer, ConcurrentMap<Integer, List<LocalDateTime>>>>>>>>
		/*-*/ yearCollector = Collectors.groupingByConcurrent(LocalDateTime::getYear, ConcurrentSkipListMap::new,
				monthCollector);
		return yearCollector;
	}

	/**
	 * Creates date list.
	 * 
	 * @param amount the amount
	 * @param unit   the unit
	 * @return the stream
	 */
	private static Stream<LocalDateTime> createDateStream(long amount, ChronoUnit unit) {

		final List<LocalDateTime> datesList = new ArrayList<LocalDateTime>();
		final LocalDateTime endDate = LocalDateTime.now();
		for (LocalDateTime date = endDate.minus(amount, unit); date.isBefore(endDate); date = date.plusSeconds(1L)) {
			datesList.add(date);
		}
		return datesList.stream();
	}

	/**
	 * Shows date and time content.
	 * 
	 * @param datesByFieldsMap the dates by fields map
	 */
	private static void showDateTimeContent(
			Map<Integer, ? extends Map<Month, ? extends Map<Integer, ? extends Map<Integer, ? extends Map<Integer, ? extends Map<Integer, List<LocalDateTime>>>>>>> datesByFieldsMap) {

		long total = 0;
		for (Integer year : datesByFieldsMap.keySet()) {
			for (Month month : datesByFieldsMap.get(year).keySet()) {
				for (Integer day : datesByFieldsMap.get(year).get(month).keySet()) {
					for (Integer hour : datesByFieldsMap.get(year).get(month).get(day).keySet()) {
						for (Integer minute : datesByFieldsMap.get(year).get(month).get(day).get(hour).keySet()) {
							for (@SuppressWarnings("unused")
							Integer second : datesByFieldsMap.get(year).get(month).get(day).get(hour).get(minute)
									.keySet()) {
								total++;
							}
						}
					}
				}
			}
		}
		System.out.printf("In one year there are [%s]seconds%n", Utils.formatNumber(total));
		long index = 0;
		for (Integer year : datesByFieldsMap.keySet()) {
			for (Month month : datesByFieldsMap.get(year).keySet()) {
				for (Integer day : datesByFieldsMap.get(year).get(month).keySet()) {
					for (Integer hour : datesByFieldsMap.get(year).get(month).get(day).keySet()) {
						for (Integer minute : datesByFieldsMap.get(year).get(month).get(day).get(hour).keySet()) {
							for (Integer second : datesByFieldsMap.get(year).get(month).get(day).get(hour).get(minute)
									.keySet()) {
								String percent = null;
								if (index == 0) {
									percent = "  0";
								} else if (index == total / 4) {
									percent = " 25";
								} else if (index == total / 2) {
									percent = " 50";
								} else if (index == 3 * total / 4) {
									percent = " 75";
								} else if (index == total - 1) {
									percent = "100";
								}
								if (percent != null) {
									System.out.printf(
											"Date at %s%% boundary: year[%d], month[%2d], day[%2d], hour[%2d], minute[%2d], second[%2d]%n",
											percent, year, month.getValue(), day, hour, minute, second);
								}
								index++;
							}
						}
					}
				}
			}
		}
	}
}