package a.latest.java.subjects;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalQuery;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kp.utils.Utils;

/**
 * Date and time changing.
 *
 */
public interface DateAndTimeChanging {

	/**
	 * Formats date.
	 * 
	 */
	public static void formatDate() {

		final Date date = new Date();
		final Calendar calendar = Calendar.getInstance();
		final LocalDateTime localDateTime = LocalDateTime.now();

		System.out.printf("date[%s]%n", date);
		System.out.printf("    [%s]◄-calendar%n", calendar.getTime());
		System.out.printf("    [%s]◄-localDateTime%n", localDateTime);

		System.out.printf("date[%1$tF %1$tT], calendar[%2$tF %2$tT], localDateTime[%2$tF %2$tT]%n", date, calendar,
				localDateTime);

		System.out.printf("date[%s %s] (with DateFormat)%n", DateFormat.getDateInstance().format(date),
				DateFormat.getTimeInstance().format(date));
		final String patternString = "date[{0,date} {0,time}] (with MessageFormat)";
		System.out.println(MessageFormat.format(patternString, date));

		final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		System.out.printf("    [%s]◄-localDateTime (with DateTimeFormatter)%n",
				dateTimeFormatter.format(localDateTime));
		// SimpleDateFormat is not thread-safe
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.printf("date[%s] (with SimpleDateFormat)%n", dateFormat.format(date));

		final DateFormat dateFormatUS = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.US);
		System.out.printf("date[%s] (with US locale)%n", dateFormatUS.format(date));
		final DateFormat dateFormatFR = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.FRANCE);
		System.out.printf("date[%s] (with FR locale)%n", dateFormatFR.format(date));
		System.out.println("- ".repeat(50));
	}

	/**
	 * Converts date.
	 * 
	 */
	public static void convertDateToAndFro() {

		java.util.Date utilDate = new Date();
		System.out.printf("utilDate\t[%1$s]%n", utilDate);
		// >>>
		java.time.LocalDate localDate = LocalDate.ofInstant(utilDate.toInstant(), ZoneId.systemDefault());
		java.time.LocalTime localTime = LocalTime.ofInstant(utilDate.toInstant(), ZoneId.systemDefault());
		java.time.LocalDateTime localDateTime = LocalDateTime.ofInstant(utilDate.toInstant(), ZoneId.systemDefault());
		java.time.ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(utilDate.toInstant(), ZoneId.systemDefault());
		System.out.printf("localDate\t[%s]%nlocalTime\t\t   [%s]%nlocalDateTime\t[%s]%nzonedDateTime\t[%s]%n",
				localDate, localTime, localDateTime, zonedDateTime);

		// <<<
		utilDate = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		utilDate = Date.from(localDate.atTime(localTime).atZone(ZoneId.systemDefault()).toInstant());
		utilDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		utilDate = Date.from(zonedDateTime.toInstant());

		// >>> SQL
		java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
		java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(utilDate.getTime());
		System.out.printf("sqlDate\t\t[%s]%nsqlTimestamp\t[%s]%n", sqlDate, sqlTimestamp);

		// <<< SQL
		utilDate = java.sql.Date.valueOf(localDate);
		utilDate = java.sql.Timestamp.valueOf(localDateTime);

		// SQL >>>
		localDate = sqlDate.toLocalDate();
		localDateTime = sqlTimestamp.toLocalDateTime();
		System.out.println("- ".repeat(50));
	}

	/**
	 * Adjusts date.
	 * 
	 */
	public static void adjustDate() {

		final List<String> labelList = /*-*/
				List.of("two days later", /*-*/
						"first day of month", /*-*/
						"first Monday in month", /*-*/
						"last day of month", /*-*/
						"first day of next month" /*-*/
				);
		final List<TemporalAdjuster> temporalAdjusterList = /*-*/
				List.of(TemporalAdjusters.ofDateAdjuster(date -> date.plusDays(2)), /*-*/
						TemporalAdjusters.firstDayOfMonth(), /*-*/
						TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY), /*-*/
						TemporalAdjusters.lastDayOfMonth(), /*-*/
						TemporalAdjusters.firstDayOfNextMonth() /*-*/
				);
		for (int i = 0; i < labelList.size(); i++) {
			final LocalDate localDate = LocalDate.now().with(temporalAdjusterList.get(i));
			System.out.printf("Adjusted as %-23s: day of week[%9s], day of month[%2d], month[%9s]%n", labelList.get(i),
					localDate.getDayOfWeek(), localDate.getDayOfMonth(), localDate.getMonth());
		}
		System.out.println("- ".repeat(50));
	}

	/**
	 * Query temporal objects.
	 * 
	 */
	public static void queryTemporalObjects() {

		final LocalDateTime now = LocalDateTime.now();
		System.out.printf("Time[%tT], hour is even[%B], minute is even[%B], second is even[%s]%n%n", now,
				now.query(DateAndTimeChanging::isHourEven), now.query(DateAndTimeChanging::isMinuteEven),
				now.query(DateAndTimeChanging::isSecondEven));

		final List<LocalDateTime> localDateTimeList = List.of(/*-*/
				LocalDateTime.now().truncatedTo(ChronoUnit.HOURS)/*-*/
						.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)).withHour(9).minusMinutes(1), /*-*/
				LocalDateTime.now().truncatedTo(ChronoUnit.HOURS)/*-*/
						.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)).withHour(9), /*-*/
				LocalDateTime.now().truncatedTo(ChronoUnit.HOURS)/*-*/
						.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)).withHour(17).minusMinutes(1), /*-*/
				LocalDateTime.now().truncatedTo(ChronoUnit.HOURS)/*-*/
						.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)).withHour(17), /*-*/
				LocalDateTime.now().truncatedTo(ChronoUnit.HOURS)/*-*/
						.with(TemporalAdjusters.firstInMonth(DayOfWeek.SATURDAY)).withHour(9)/*-*/
		);
		final TemporalQuery<Boolean> workingHoursQuery = temporal -> {
			final LocalTime localTime = LocalTime.from(temporal);
			boolean flag = DayOfWeek.SATURDAY != DayOfWeek.from(temporal);
			flag = flag && DayOfWeek.SUNDAY != DayOfWeek.from(temporal);
			flag = flag && LocalTime.of(9, 0).compareTo(localTime) < 1;
			flag = flag && LocalTime.of(17, 0).compareTo(localTime) > 0;
			return flag;
		};
		localDateTimeList.stream().forEach(temporal -> {
			System.out.printf("Date[%1$9s %2$tF %2$tT] is within limits of working time[%3$B]%n",
					DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK)), temporal, temporal.query(workingHoursQuery));
		});
		System.out.println("- ".repeat(50));
	}

	/**
	 * Checks if the hour is even.
	 * 
	 * @param temporal the temporal
	 * @return the result flag
	 */
	private static boolean isHourEven(TemporalAccessor temporal) {
		return temporal.get(ChronoField.HOUR_OF_DAY) % 2 == 0;
	}

	/**
	 * Checks if the minute is even.
	 * 
	 * @param temporal the temporal
	 * @return the result flag
	 */
	private static boolean isMinuteEven(TemporalAccessor temporal) {
		return temporal.get(ChronoField.MINUTE_OF_HOUR) % 2 == 0;
	}

	/**
	 * Checks if the second is even.
	 * 
	 * @param temporal the temporal
	 * @return the result string
	 */
	private static String isSecondEven(TemporalAccessor temporal) {
		return temporal.get(ChronoField.SECOND_OF_MINUTE) % 2 == 0 ? "YES" : "NO";
	}

	/**
	 * Calculates the amount of time between start and end.
	 * 
	 */
	public static void calculateAmountOfTimeBetween() {

		final LocalDateTime startDateTime1 = LocalDateTime.now();
		final LocalDateTime endDateTime1 = startDateTime1.plusHours(2).minusMinutes(58).minusSeconds(59);
		System.out.printf("Chrono units between start time[%1$tF %1$tT] and end time[%2$tF %2$tT]:%n"/*-*/
				+ "\thours[%3$d] or minutes[%4$d] or seconds[%5$s] or millis[%6$s]%n", startDateTime1, endDateTime1,
				ChronoUnit.HOURS.between(startDateTime1, endDateTime1),
				ChronoUnit.MINUTES.between(startDateTime1, endDateTime1),
				Utils.formatNumber(ChronoUnit.SECONDS.between(startDateTime1, endDateTime1)),
				Utils.formatNumber(ChronoUnit.MILLIS.between(startDateTime1, endDateTime1)));

		final LocalDateTime startDateTime2 = LocalDateTime.now();
		final LocalDateTime endDateTime2 = startDateTime2.plusMonths(1).plusWeeks(1).minusDays(1);
		System.out.printf("Chrono units between start time[%1$tF %1$tT] and end time[%2$tF %2$tT]:%n"/*-*/
				+ "\tmonths[%3$d] or weeks[%4$d] or days[%5$d]%n", startDateTime2, endDateTime2,
				ChronoUnit.MONTHS.between(startDateTime2, endDateTime2),
				ChronoUnit.WEEKS.between(startDateTime2, endDateTime2),
				ChronoUnit.DAYS.between(startDateTime2, endDateTime2));

		final LocalTime startTime = LocalTime.now();
		final LocalTime endTime = startTime.plusSeconds(2).minusNanos(999_999_999);
		final Duration duration1 = Duration.between(startTime, endTime);
		System.out.printf("Duration between start time[%s] and end time[%s]:%n"/*-*/
				+ "\tduration[%s], seconds[%s] and nanos[%s]%n", startTime, endTime, duration1, duration1.getSeconds(),
				duration1.getNano());

		final Duration duration2 = Duration.parse("P1DT2H3M4.005054321S");
		System.out.printf("Duration[%s]: seconds(total)[%s]%n"/*-*/
				+ "\tdays[%s], hours[%s], minutes[%s], seconds[%s], millis[%s], nanos[%s]%n", duration2,
				Utils.formatNumber(duration2.toSeconds()), duration2.toDaysPart(), duration2.toHoursPart(),
				duration2.toMinutesPart(), duration2.toSecondsPart(), duration2.toMillisPart(),
				Utils.formatNumber(duration2.toNanosPart()));

		final LocalDate startDate = LocalDate.now();
		final LocalDate endDate = startDate.plusMonths(1).plusWeeks(1).minusDays(1);
		final Period period1 = Period.between(startDate, endDate);
		System.out.printf("Period between start date[%s] and end date[%s]: period[%s] or months[%s] and days[%s]%n",
				startDate, endDate, period1, period1.getMonths(), period1.getDays());

		final Period period2 = Period.of(1, 2, 3);
		System.out.printf("Period[%s]: months(total)[%d]%n\tyears[%d], months[%d], days[%d]%n", period2,
				period2.toTotalMonths(), period2.getYears(), period2.getMonths(), period2.getDays());
		System.out.println("- ".repeat(50));
	}

	/**
	 * Adds to instant or subtract from instant.
	 * 
	 */
	public static void addToOrSubtractFromInstant() {

		final Instant base = LocalDateTime.of(2000, 1, 1, 0, 0).toInstant(ZoneOffset.UTC);
		final Duration days = Duration.ofDays(1);
		final Duration hours = Duration.ofHours(2);
		final Duration minutes = Duration.ofMinutes(3);
		final Duration seconds = Duration.ofSeconds(4);
		final Duration millis = Duration.ofMillis(5);
		final Duration micros = Duration.of(6, ChronoUnit.MICROS);
		final Duration nanos = Duration.ofNanos(7);
		final Instant earlier = base.minus(days).minus(hours).minus(minutes).minus(seconds).minus(millis).minus(micros)
				.minus(nanos);
		final Instant later = base.plus(days).plus(hours).plus(minutes).plus(seconds).plus(millis).plus(micros)
				.plus(nanos);
		System.out.printf("▲▲▲ earlier[%s] ▲▲▲%n", earlier);
		System.out.printf("       base[%s]%n", base.plus(Duration.ofNanos(1)));
		System.out.printf("▼▼▼   later[%s] ▼▼▼%n", later);

		final LocalDate baseDate = LocalDate.ofInstant(base, ZoneId.systemDefault());
		final LocalDate earlierDate = LocalDate.ofInstant(earlier, ZoneId.systemDefault());
		final LocalDate laterDate = LocalDate.ofInstant(later, ZoneId.systemDefault());
		System.out.printf("Duration between    base and later[%s], period between    base and later[%s]%n",
				Duration.between(base, later), Period.between(baseDate, laterDate));
		System.out.printf("Duration between earlier and later[%s], period between earlier and later[%s]%n",
				Duration.between(earlier, later), Period.between(earlierDate, laterDate));

		System.out.printf("%nFar future [%s]%n", /*-*/
				ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)/*-*/
						.plus(1, ChronoUnit.MILLENNIA)/*-*/
						.plus(2, ChronoUnit.CENTURIES)/*-*/
						.plus(3, ChronoUnit.DECADES)/*-*/
						.plusYears(4)/*-*/
						.plusMonths(5 - 1)/*-*/
						.plusDays(6 - 1)/*-*/
						.plusHours(7)/*-*/
						.plusMinutes(8)/*-*/
						.plusSeconds(9)/*-*/
						.plus(1, ChronoUnit.MILLIS)/*-*/
						.plus(2, ChronoUnit.MICROS)/*-*/
						.plus(3, ChronoUnit.NANOS));
		System.out.println("- ".repeat(50));
	}

	/**
	 * Fragmentizes the time.
	 * 
	 */
	public static void fragmentizeTime() {

		System.out.printf("Number of 12 minute periods in 4 hours[%d]%n%n",
				Duration.ofHours(4).dividedBy(Duration.ofMinutes(12)));

		final Instant before = Instant.now();
		final Clock clock3 = Clock.tick(Clock.systemDefaultZone(), Duration.ofSeconds(3));
		final Instant instant3 = clock3.instant();
		final Clock clock5 = Clock.tick(Clock.systemDefaultZone(), Duration.ofSeconds(5));
		final Instant instant5 = clock5.instant();
		final Instant after = Instant.now();
		System.out.printf("Time before\t[%s]%nClock tick 3 sec[%s]%nClock tick 5 sec[%s]%nTime after\t[%s]%n", before,
				instant3, instant5, after);
		System.out.println("- ".repeat(50));
	}
}