package com.mrkhan.katas.datetime.solutions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;
import java.util.stream.*;

import static org.assertj.core.api.Assertions.*;

/**
 * ✅ DATE-TIME API - SOLUTIONS
 * Study AFTER attempting the todo tests.
 */
@DisplayName("✅ Date-Time API - Solutions")
public class DateTimeSolutionTest {

    @Test @DisplayName("✅ SOLUTION 1: Create LocalDate and extract fields")
    public void createLocalDateAndExtract_solution() {
        LocalDate date = LocalDate.of(2024, 3, 15);

        assertThat(date.getYear()).isEqualTo(2024);
        assertThat(date.getMonthValue()).isEqualTo(3);
        assertThat(date.getDayOfMonth()).isEqualTo(15);
        assertThat(date.getDayOfWeek()).isEqualTo(DayOfWeek.FRIDAY);
        // 💡 LocalDate is immutable, thread-safe - unlike java.util.Date
        // 💡 getDayOfWeek() returns enum DayOfWeek, not an int (unlike old Calendar)
    }

    @Test @DisplayName("✅ SOLUTION 2: Parse LocalDate from string")
    public void parseLocalDate_solution() {
        LocalDate iso    = LocalDate.parse("2024-07-04");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate custom = LocalDate.parse("04/07/2024", fmt);

        assertThat(iso).isEqualTo(LocalDate.of(2024, 7, 4));
        assertThat(custom).isEqualTo(LocalDate.of(2024, 7, 4));
        // 💡 DateTimeFormatter is THREAD-SAFE (unlike SimpleDateFormat!)
        // 💡 Default toString() uses ISO-8601: yyyy-MM-dd
    }

    @Test @DisplayName("✅ SOLUTION 3: Date arithmetic")
    public void dateArithmetic_solution() {
        LocalDate start = LocalDate.of(2024, 1, 15);

        LocalDate plusTwoWeeks  = start.plusWeeks(2);
        LocalDate minusOneMonth = start.minusMonths(1);
        LocalDate nextYear      = start.plusYears(1);

        assertThat(plusTwoWeeks).isEqualTo(LocalDate.of(2024, 1, 29));
        assertThat(minusOneMonth).isEqualTo(LocalDate.of(2023, 12, 15));
        assertThat(nextYear).isEqualTo(LocalDate.of(2025, 1, 15));
        // 💡 All plus/minus methods return NEW instances (immutable)
        // 💡 Available: plusDays, plusWeeks, plusMonths, plusYears (and minus equivalents)
    }

    @Test @DisplayName("✅ SOLUTION 4: Compare dates")
    public void compareDates_solution() {
        LocalDate d1 = LocalDate.of(2024, 3, 10);
        LocalDate d2 = LocalDate.of(2024, 6, 20);

        boolean isBefore = d1.isBefore(d2);
        boolean isAfter  = d1.isAfter(d2);
        boolean isLeap   = d1.isLeapYear();

        assertThat(isBefore).isTrue();
        assertThat(isAfter).isFalse();
        assertThat(isLeap).isTrue();
        // 💡 2024 is a leap year (divisible by 4, and not a century exception)
        // 💡 isEqual(other) also exists as semantic alternative to .equals()
    }

    @Test @DisplayName("✅ SOLUTION 5: Period between two dates")
    public void periodBetweenDates_solution() {
        LocalDate hired = LocalDate.of(2020, 3, 1);
        LocalDate today = LocalDate.of(2024, 9, 1);

        Period tenure    = Period.between(hired, today);
        long totalDays   = ChronoUnit.DAYS.between(hired, today);

        assertThat(tenure.getYears()).isEqualTo(4);
        assertThat(tenure.getMonths()).isEqualTo(6);
        assertThat(tenure.getDays()).isEqualTo(0);
        assertThat(totalDays).isEqualTo(1645L);
        // 💡 Period: date-based (years/months/days) — use for human-readable tenure
        // 💡 ChronoUnit.DAYS.between: total absolute days — use for SLA/deadline checks
        // 💡 Duration: time-based (hours/minutes/seconds) — use for LocalTime/Instant
    }

    @Test @DisplayName("✅ SOLUTION 6: LocalTime creation and manipulation")
    public void localTimeOperations_solution() {
        LocalTime meetingTime = LocalTime.of(14, 30, 45);
        LocalTime endTime     = meetingTime.plusMinutes(90);

        assertThat(meetingTime.getHour()).isEqualTo(14);
        assertThat(meetingTime.getMinute()).isEqualTo(30);
        assertThat(endTime).isEqualTo(LocalTime.of(16, 0, 45));
        // 💡 LocalTime wraps at midnight: LocalTime.of(23, 0).plusHours(2) → 01:00
    }

    @Test @DisplayName("✅ SOLUTION 7: Duration between two times")
    public void durationBetweenTimes_solution() {
        LocalTime start  = LocalTime.of(9, 0);
        LocalTime end    = LocalTime.of(17, 30);

        Duration workday = Duration.between(start, end);

        assertThat(workday.toHours()).isEqualTo(8L);
        assertThat(workday.toMinutes()).isEqualTo(510L);
        // 💡 Duration.toMinutes() returns TOTAL minutes (not just the minutes component)
        // 💡 For component-level: workday.toHoursPart() / toMinutesPart() are Java 9+
    }

    @Test @DisplayName("✅ SOLUTION 8: LocalDateTime format and parse")
    public void localDateTimeFormat_solution() {
        LocalDateTime dt = LocalDateTime.of(2024, 6, 15, 10, 30, 0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");

        String formatted = dt.format(fmt);
        LocalDateTime parsed = LocalDateTime.parse(formatted, fmt);

        assertThat(formatted).isEqualTo("15-Jun-2024 10:30");
        assertThat(parsed).isEqualTo(dt);
        // 💡 'MMM' = short month name (locale-dependent!) use .withLocale(Locale.ENGLISH) for stability
        // 💡 'HH' = 24-hour, 'hh' = 12-hour, 'a' = AM/PM
    }

    @Test @DisplayName("✅ SOLUTION 9: LocalDateTime arithmetic")
    public void localDateTimeArithmetic_solution() {
        LocalDateTime orderPlaced = LocalDateTime.of(2024, 1, 10, 14, 0);
        LocalDateTime delivery    = orderPlaced.plusDays(3).plusHours(6);

        assertThat(delivery).isEqualTo(LocalDateTime.of(2024, 1, 13, 20, 0));
        // 💡 Chain multiple adjustments - each returns a new instance
    }

    @Test @DisplayName("✅ SOLUTION 10: TemporalAdjusters first/last day of month")
    public void temporalAdjusterFirstLast_solution() {
        LocalDate date     = LocalDate.of(2024, 3, 15);
        LocalDate firstDay = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDay  = date.with(TemporalAdjusters.lastDayOfMonth());

        assertThat(firstDay).isEqualTo(LocalDate.of(2024, 3, 1));
        assertThat(lastDay).isEqualTo(LocalDate.of(2024, 3, 31));
        // 💡 TemporalAdjusters: firstDayOfYear, lastDayOfYear, firstDayOfNextMonth,
        //    firstDayOfNextYear, firstInMonth(DOW), lastInMonth(DOW), next(DOW), previous(DOW)
    }

    @Test @DisplayName("✅ SOLUTION 11: TemporalAdjusters next weekday")
    public void temporalAdjusterNextWeekday_solution() {
        LocalDate friday            = LocalDate.of(2024, 3, 15);
        LocalDate nextMonday        = friday.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDate lastFridayOfMonth = friday.with(TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY));

        assertThat(nextMonday).isEqualTo(LocalDate.of(2024, 3, 18));
        assertThat(lastFridayOfMonth).isEqualTo(LocalDate.of(2024, 3, 29));
        // 💡 next(DOW) skips the current day; nextOrSame(DOW) includes it
    }

    @Test @DisplayName("✅ SOLUTION 12: Custom TemporalAdjuster")
    public void customTemporalAdjuster_solution() {
        TemporalAdjuster nextBusinessDay = temporal -> {
            DayOfWeek dow = DayOfWeek.from(temporal);
            int add = (dow == DayOfWeek.FRIDAY) ? 3 : (dow == DayOfWeek.SATURDAY) ? 2 : 1;
            return temporal.plus(add, ChronoUnit.DAYS);
        };

        LocalDate friday   = LocalDate.of(2024, 3, 15);
        LocalDate saturday = LocalDate.of(2024, 3, 16);
        LocalDate monday   = LocalDate.of(2024, 3, 18);

        assertThat(friday.with(nextBusinessDay)).isEqualTo(LocalDate.of(2024, 3, 18));
        assertThat(saturday.with(nextBusinessDay)).isEqualTo(LocalDate.of(2024, 3, 18));
        assertThat(monday.with(nextBusinessDay)).isEqualTo(LocalDate.of(2024, 3, 19));
        // 💡 TemporalAdjuster is a @FunctionalInterface - implement as lambda
        // 💡 Key interview pattern: custom business calendar logic
    }

    @Test @DisplayName("✅ SOLUTION 13: ZonedDateTime and time zone conversion")
    public void timeZoneConversion_solution() {
        ZonedDateTime nyMeeting = ZonedDateTime.of(
                LocalDateTime.of(2024, 3, 15, 9, 0),
                ZoneId.of("America/New_York")
        );
        ZonedDateTime londonTime = nyMeeting.withZoneSameInstant(ZoneId.of("Europe/London"));
        ZonedDateTime tokyoTime  = nyMeeting.withZoneSameInstant(ZoneId.of("Asia/Tokyo"));

        assertThat(londonTime.getHour()).isEqualTo(13);
        assertThat(tokyoTime.getHour()).isEqualTo(22);
        // 💡 withZoneSameInstant: same moment, different display (for conversions)
        // 💡 withZoneSameLocal: same clock time, different instant (for scheduling)
        // 💡 Use ZoneId.getAvailableZoneIds() to list all zones
    }

    @Test @DisplayName("✅ SOLUTION 14: Instant and epoch conversion")
    public void instantAndEpoch_solution() {
        Instant instant = Instant.ofEpochMilli(1704067200000L);
        ZonedDateTime utc = instant.atZone(ZoneId.of("UTC"));

        assertThat(instant.toEpochMilli()).isEqualTo(1704067200000L);
        assertThat(utc.getYear()).isEqualTo(2024);
        assertThat(utc.getMonthValue()).isEqualTo(1);
        assertThat(utc.getDayOfMonth()).isEqualTo(1);
        // 💡 Instant = machine-readable UTC point in time (for storage, logs, APIs)
        // 💡 ZonedDateTime = human-readable (for display, scheduling)
        // 💡 Convert: instant.atZone(zone) or zdt.toInstant()
    }

    @Test @DisplayName("✅ SOLUTION 15: DateTimeFormatter custom patterns")
    public void customFormatPatterns_solution() {
        LocalDate date = LocalDate.of(2024, 12, 25);

        String pattern1 = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String pattern2 = date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
        String pattern3 = date.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd"));

        assertThat(pattern1).isEqualTo("25/12/2024");
        assertThat(pattern2).isEqualTo("Dec 25, 2024");
        assertThat(pattern3).isEqualTo("Wednesday, December 25");
        // 💡 Pattern tokens: yyyy=year, MM=month#, MMM=short name, MMMM=full name
        //    dd=day, EEE=short weekday, EEEE=full weekday, HH=24h, hh=12h, mm=min, ss=sec
    }

    @Test @DisplayName("✅ SOLUTION 16: DateTimeFormatter with Locale")
    public void formatterWithLocale_solution() {
        LocalDate date = LocalDate.of(2024, 1, 15);

        DateTimeFormatter usFormatter = DateTimeFormatter
                .ofPattern("EEEE, MMMM dd, yyyy").withLocale(Locale.US);
        DateTimeFormatter frFormatter = DateTimeFormatter
                .ofPattern("EEEE dd MMMM yyyy").withLocale(Locale.FRANCE);

        assertThat(date.format(usFormatter)).isEqualTo("Monday, January 15, 2024");
        assertThat(date.format(frFormatter)).isEqualTo("lundi 15 janvier 2024");
        // 💡 ALWAYS specify Locale for patterns with EEE/EEEE/MMM/MMMM
        // 💡 Otherwise output depends on JVM default locale → test failures on CI
    }

    @Test @DisplayName("✅ SOLUTION 17: Stream of dates - generate a week")
    public void streamOfDates_solution() {
        LocalDate monday = LocalDate.of(2024, 3, 18);

        List<LocalDate> workweek = Stream.iterate(monday, d -> d.plusDays(1))
                .limit(5)
                .collect(Collectors.toList());

        assertThat(workweek).hasSize(5);
        assertThat(workweek.get(0)).isEqualTo(LocalDate.of(2024, 3, 18));
        assertThat(workweek.get(4)).isEqualTo(LocalDate.of(2024, 3, 22));
        // 💡 Stream.iterate(seed, unaryOp) is ideal for date sequences
        // 💡 Always combine with limit() to avoid infinite stream!
    }

    @Test @DisplayName("✅ SOLUTION 18: Filter and sort dates in a stream")
    public void filterAndSortDates_solution() {
        List<LocalDate> dates = Arrays.asList(
                LocalDate.of(2024, 6, 15), LocalDate.of(2023, 12, 1),
                LocalDate.of(2024, 1, 10), LocalDate.of(2025, 3, 5),
                LocalDate.of(2024, 9, 20)
        );
        LocalDate cutoff = LocalDate.of(2024, 1, 1);

        List<LocalDate> result = dates.stream()
                .filter(d -> d.isAfter(cutoff))
                .sorted()
                .collect(Collectors.toList());

        assertThat(result).hasSize(4);
        assertThat(result.get(0)).isEqualTo(LocalDate.of(2024, 1, 10));
        assertThat(result.get(3)).isEqualTo(LocalDate.of(2025, 3, 5));
        // 💡 LocalDate implements Comparable → sorted() works naturally
        // 💡 Use Comparator.comparing(LocalDate::getYear).thenComparing(LocalDate::getDayOfYear) for custom sort
    }

    @Test @DisplayName("✅ SOLUTION 19: Days until next birthday")
    public void daysUntilBirthday_solution() {
        LocalDate birthday = LocalDate.of(1990, 8, 15);
        LocalDate today    = LocalDate.of(2024, 3, 15);

        LocalDate candidate = birthday.withYear(today.getYear());
        if (!candidate.isAfter(today)) {
            candidate = candidate.plusYears(1);
        }
        long daysUntil = ChronoUnit.DAYS.between(today, candidate);

        assertThat(candidate).isEqualTo(LocalDate.of(2024, 8, 15));
        assertThat(daysUntil).isEqualTo(153L);
        // 💡 withYear() replaces just the year component - ideal for recurring dates
        // 💡 Common interview problem: "days until next event" pattern
    }

    @Test @DisplayName("✅ SOLUTION 20: Duration - measure execution time")
    public void measureDuration_solution() {
        Instant before = Instant.now();
        long sum = 0;
        for (int i = 0; i < 1_000_000; i++) sum += i;
        Instant after = Instant.now();

        Duration elapsed = Duration.between(before, after);

        assertThat(elapsed.toMillis()).isGreaterThanOrEqualTo(0L);
        assertThat(elapsed.isNegative()).isFalse();
        assertThat(sum).isEqualTo(499999500000L);
        // 💡 Instant + Duration.between = standard Java 8 benchmarking pattern
        // 💡 For production profiling, use Micrometer or JFR instead
        // 💡 Duration.ofSeconds(30).toMillis() == 30000L
    }
}
