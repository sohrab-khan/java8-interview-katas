package com.mrkhan.katas.datetime.todo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;
import java.util.stream.*;

import static org.assertj.core.api.Assertions.*;

/**
 * 🎯 DATE-TIME API KATAS (20 katas)
 * ──────────────────────────────────
 * Master java.time (Java 8): LocalDate, LocalTime, LocalDateTime,
 * ZonedDateTime, Duration, Period, DateTimeFormatter, TemporalAdjusters.
 * Run: ./mvnw test -Dtest="DateTimeTodoTest"
 */
@DisplayName("🚨 Date-Time API - TODO (Fix me!)")
public class DateTimeTodoTest {

    // ─── LOCALDATE ───────────────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 1: Create LocalDate and extract fields")
    public void createLocalDateAndExtract() {
        // 🚨 TO DO: Create LocalDate for 2024-03-15
        LocalDate date = null; // ❌ BROKEN

        assertThat(date.getYear()).isEqualTo(2024);
        assertThat(date.getMonthValue()).isEqualTo(3);
        assertThat(date.getDayOfMonth()).isEqualTo(15);
        assertThat(date.getDayOfWeek()).isEqualTo(DayOfWeek.FRIDAY);

        // 💡 HINTS:
        // LocalDate.of(2024, 3, 15)
    }

    @Test
    @DisplayName("🚨 TODO 2: Parse LocalDate from string")
    public void parseLocalDate() {
        // 🚨 TO DO: Parse "2024-07-04" into a LocalDate, then parse "04/07/2024" with custom formatter
        LocalDate iso = null;   // ❌ BROKEN - LocalDate.parse("2024-07-04")
        LocalDate custom = null; // ❌ BROKEN - LocalDate.parse("04/07/2024", formatter)

        assertThat(iso).isEqualTo(LocalDate.of(2024, 7, 4));
        assertThat(custom).isEqualTo(LocalDate.of(2024, 7, 4));

        // 💡 HINTS:
        // LocalDate.parse("2024-07-04")
        // DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // LocalDate.parse("04/07/2024", fmt)
    }

    @Test
    @DisplayName("🚨 TODO 3: Date arithmetic - add and subtract")
    public void dateArithmetic() {
        LocalDate start = LocalDate.of(2024, 1, 15);

        // 🚨 TO DO: Calculate these dates
        LocalDate plusTwoWeeks  = null; // ❌ BROKEN - start + 2 weeks
        LocalDate minusOneMonth = null; // ❌ BROKEN - start - 1 month
        LocalDate nextYear      = null; // ❌ BROKEN - start + 1 year

        assertThat(plusTwoWeeks).isEqualTo(LocalDate.of(2024, 1, 29));
        assertThat(minusOneMonth).isEqualTo(LocalDate.of(2023, 12, 15));
        assertThat(nextYear).isEqualTo(LocalDate.of(2025, 1, 15));

        // 💡 HINTS:
        // start.plusWeeks(2), start.minusMonths(1), start.plusYears(1)
    }

    @Test
    @DisplayName("🚨 TODO 4: Compare dates and check order")
    public void compareDates() {
        LocalDate d1 = LocalDate.of(2024, 3, 10);
        LocalDate d2 = LocalDate.of(2024, 6, 20);

        // 🚨 TO DO: Fill in the comparison results
        boolean isBefore = false; // ❌ BROKEN - d1.isBefore(d2)
        boolean isAfter  = true;  // ❌ BROKEN - d1.isAfter(d2)
        boolean isLeap   = false; // ❌ BROKEN - is 2024 a leap year?

        assertThat(isBefore).isTrue();
        assertThat(isAfter).isFalse();
        assertThat(isLeap).isTrue();

        // 💡 HINTS:
        // d1.isBefore(d2), d1.isAfter(d2), d1.isLeapYear()
    }

    @Test
    @DisplayName("🚨 TODO 5: Period between two dates")
    public void periodBetweenDates() {
        LocalDate hired   = LocalDate.of(2020, 3, 1);
        LocalDate today   = LocalDate.of(2024, 9, 1);

        // 🚨 TO DO: Calculate the period between hired and today
        Period tenure = null; // ❌ BROKEN

        assertThat(tenure.getYears()).isEqualTo(4);
        assertThat(tenure.getMonths()).isEqualTo(6);
        assertThat(tenure.getDays()).isEqualTo(0);

        // 🚨 TO DO: Calculate total days using ChronoUnit
        long totalDays = 0L; // ❌ BROKEN

        assertThat(totalDays).isEqualTo(1645L);

        // 💡 HINTS:
        // Period.between(hired, today)
        // ChronoUnit.DAYS.between(hired, today)
    }

    // ─── LOCALTIME ───────────────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 6: LocalTime creation and manipulation")
    public void localTimeOperations() {
        // 🚨 TO DO: Create 14:30:45 and add 90 minutes
        LocalTime meetingTime = null; // ❌ BROKEN - LocalTime.of(14, 30, 45)
        LocalTime endTime     = null; // ❌ BROKEN - meetingTime + 90 minutes

        assertThat(meetingTime.getHour()).isEqualTo(14);
        assertThat(meetingTime.getMinute()).isEqualTo(30);
        assertThat(endTime).isEqualTo(LocalTime.of(16, 0, 45));

        // 💡 HINTS:
        // LocalTime.of(14, 30, 45)
        // meetingTime.plusMinutes(90)
    }

    @Test
    @DisplayName("🚨 TODO 7: Duration between two times")
    public void durationBetweenTimes() {
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end   = LocalTime.of(17, 30);

        // 🚨 TO DO: Calculate duration of a workday
        Duration workday = null; // ❌ BROKEN

        assertThat(workday.toHours()).isEqualTo(8L);
        assertThat(workday.toMinutes()).isEqualTo(510L);

        // 💡 HINTS:
        // Duration.between(start, end)
    }

    // ─── LOCALDATETIME ───────────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 8: LocalDateTime combine and format")
    public void localDateTimeFormat() {
        LocalDateTime dt = LocalDateTime.of(2024, 6, 15, 10, 30, 0);

        // 🚨 TO DO: Format using pattern "dd-MMM-yyyy HH:mm"
        String formatted = null; // ❌ BROKEN

        assertThat(formatted).isEqualTo("15-Jun-2024 10:30");

        // 🚨 TO DO: Parse it back
        LocalDateTime parsed = null; // ❌ BROKEN

        assertThat(parsed).isEqualTo(dt);

        // 💡 HINTS:
        // DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm")
        // dt.format(formatter), LocalDateTime.parse(formatted, formatter)
    }

    @Test
    @DisplayName("🚨 TODO 9: LocalDateTime arithmetic")
    public void localDateTimeArithmetic() {
        LocalDateTime orderPlaced = LocalDateTime.of(2024, 1, 10, 14, 0);

        // 🚨 TO DO: Delivery = orderPlaced + 3 days + 6 hours
        LocalDateTime delivery = null; // ❌ BROKEN

        assertThat(delivery).isEqualTo(LocalDateTime.of(2024, 1, 13, 20, 0));

        // 💡 HINTS:
        // orderPlaced.plusDays(3).plusHours(6)
    }

    // ─── TEMPORAL ADJUSTERS ──────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 10: TemporalAdjusters - first/last day of month")
    public void temporalAdjusterFirstLast() {
        LocalDate date = LocalDate.of(2024, 3, 15);

        // 🚨 TO DO: Get first and last day of the month
        LocalDate firstDay = null; // ❌ BROKEN
        LocalDate lastDay  = null; // ❌ BROKEN

        assertThat(firstDay).isEqualTo(LocalDate.of(2024, 3, 1));
        assertThat(lastDay).isEqualTo(LocalDate.of(2024, 3, 31));

        // 💡 HINTS:
        // date.with(TemporalAdjusters.firstDayOfMonth())
        // date.with(TemporalAdjusters.lastDayOfMonth())
    }

    @Test
    @DisplayName("🚨 TODO 11: TemporalAdjusters - next weekday")
    public void temporalAdjusterNextWeekday() {
        LocalDate friday = LocalDate.of(2024, 3, 15); // Friday

        // 🚨 TO DO: Get next Monday and last Friday of month
        LocalDate nextMonday       = null; // ❌ BROKEN
        LocalDate lastFridayOfMonth = null; // ❌ BROKEN

        assertThat(nextMonday).isEqualTo(LocalDate.of(2024, 3, 18));
        assertThat(lastFridayOfMonth).isEqualTo(LocalDate.of(2024, 3, 29));

        // 💡 HINTS:
        // friday.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
        // friday.with(TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY))
    }

    @Test
    @DisplayName("🚨 TODO 12: Custom TemporalAdjuster - next business day")
    public void customTemporalAdjuster() {
        // 🚨 TO DO: Build a custom adjuster that advances to next business day
        // (skip Saturday → Monday, skip Sunday → Monday, weekdays → next day)
        TemporalAdjuster nextBusinessDay = null; // ❌ BROKEN

        LocalDate friday   = LocalDate.of(2024, 3, 15); // Friday → next biz = Monday
        LocalDate saturday = LocalDate.of(2024, 3, 16); // Saturday → next biz = Monday
        LocalDate monday   = LocalDate.of(2024, 3, 18); // Monday → next biz = Tuesday

        assertThat(friday.with(nextBusinessDay)).isEqualTo(LocalDate.of(2024, 3, 18));
        assertThat(saturday.with(nextBusinessDay)).isEqualTo(LocalDate.of(2024, 3, 18));
        assertThat(monday.with(nextBusinessDay)).isEqualTo(LocalDate.of(2024, 3, 19));

        // 💡 HINTS:
        // temporal -> {
        //     DayOfWeek dow = DayOfWeek.from(temporal);
        //     int add = (dow == DayOfWeek.FRIDAY) ? 3 : (dow == DayOfWeek.SATURDAY) ? 2 : 1;
        //     return temporal.plus(add, ChronoUnit.DAYS);
        // }
    }

    // ─── ZONEDDATETIME ────────────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 13: ZonedDateTime and time zone conversion")
    public void timeZoneConversion() {
        // 🚨 TO DO: Create a meeting at 9:00 AM New York time on 2024-03-15
        ZonedDateTime nyMeeting = null; // ❌ BROKEN

        // 🚨 TO DO: Convert it to London and Tokyo time
        ZonedDateTime londonTime = null; // ❌ BROKEN - withZoneSameInstant
        ZonedDateTime tokyoTime  = null; // ❌ BROKEN - withZoneSameInstant

        assertThat(londonTime.getHour()).isEqualTo(13); // UTC+0 → NY EST = UTC-5, so 9+4 = 13 (GMT)
        assertThat(tokyoTime.getHour()).isEqualTo(22);  // JST = UTC+9, so 9+14 = 23... (13 + 9 = 22)

        // 💡 HINTS:
        // ZonedDateTime.of(LocalDateTime.of(2024, 3, 15, 9, 0), ZoneId.of("America/New_York"))
        // nyMeeting.withZoneSameInstant(ZoneId.of("Europe/London"))
        // nyMeeting.withZoneSameInstant(ZoneId.of("Asia/Tokyo"))
    }

    @Test
    @DisplayName("🚨 TODO 14: Instant and epoch conversion")
    public void instantAndEpoch() {
        // 🚨 TO DO: Create Instant from epoch millis 1704067200000L (2024-01-01T00:00:00Z)
        Instant instant = null; // ❌ BROKEN

        assertThat(instant.toEpochMilli()).isEqualTo(1704067200000L);

        // 🚨 TO DO: Convert to ZonedDateTime in UTC
        ZonedDateTime utc = null; // ❌ BROKEN

        assertThat(utc.getYear()).isEqualTo(2024);
        assertThat(utc.getMonthValue()).isEqualTo(1);
        assertThat(utc.getDayOfMonth()).isEqualTo(1);

        // 💡 HINTS:
        // Instant.ofEpochMilli(1704067200000L)
        // instant.atZone(ZoneId.of("UTC"))
    }

    // ─── FORMATTING ──────────────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 15: DateTimeFormatter custom patterns")
    public void customFormatPatterns() {
        LocalDate date = LocalDate.of(2024, 12, 25);

        // 🚨 TO DO: Format using three different patterns
        String pattern1 = null; // ❌ BROKEN - "25/12/2024"  pattern: dd/MM/yyyy
        String pattern2 = null; // ❌ BROKEN - "Dec 25, 2024" pattern: MMM dd, yyyy
        String pattern3 = null; // ❌ BROKEN - "Wednesday, December 25" pattern: EEEE, MMMM dd

        assertThat(pattern1).isEqualTo("25/12/2024");
        assertThat(pattern2).isEqualTo("Dec 25, 2024");
        assertThat(pattern3).isEqualTo("Wednesday, December 25");

        // 💡 HINTS:
        // date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        // date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
        // date.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd"))
    }

    @Test
    @DisplayName("🚨 TODO 16: DateTimeFormatter with Locale")
    public void formatterWithLocale() {
        LocalDate date = LocalDate.of(2024, 1, 15);

        // 🚨 TO DO: Format the same date with US and French locale
        DateTimeFormatter usFormatter = null;  // ❌ BROKEN - "EEEE, MMMM dd, yyyy" with Locale.US
        DateTimeFormatter frFormatter = null;  // ❌ BROKEN - "EEEE dd MMMM yyyy" with Locale.FRANCE

        String usFormatted = date.format(usFormatter);
        String frFormatted = date.format(frFormatter);

        assertThat(usFormatted).isEqualTo("Monday, January 15, 2024");
        assertThat(frFormatted).isEqualTo("lundi 15 janvier 2024");

        // 💡 HINTS:
        // DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy").withLocale(Locale.US)
        // DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy").withLocale(Locale.FRANCE)
    }

    // ─── STREAMS WITH DATES ───────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 17: Stream of dates - generate a week")
    public void streamOfDates() {
        LocalDate monday = LocalDate.of(2024, 3, 18); // Monday

        // 🚨 TO DO: Generate stream of 5 working days (Mon-Fri) starting from monday
        List<LocalDate> workweek = null; // ❌ BROKEN

        assertThat(workweek).hasSize(5);
        assertThat(workweek.get(0)).isEqualTo(LocalDate.of(2024, 3, 18));
        assertThat(workweek.get(4)).isEqualTo(LocalDate.of(2024, 3, 22));

        // 💡 HINTS:
        // Stream.iterate(monday, d -> d.plusDays(1)).limit(5).collect(Collectors.toList())
    }

    @Test
    @DisplayName("🚨 TODO 18: Filter and sort dates in a stream")
    public void filterAndSortDates() {
        List<LocalDate> dates = Arrays.asList(
                LocalDate.of(2024, 6, 15),
                LocalDate.of(2023, 12, 1),
                LocalDate.of(2024, 1, 10),
                LocalDate.of(2025, 3, 5),
                LocalDate.of(2024, 9, 20)
        );

        LocalDate cutoff = LocalDate.of(2024, 1, 1);

        // 🚨 TO DO: Get all dates after cutoff, sorted ascending
        List<LocalDate> result = null; // ❌ BROKEN

        assertThat(result).hasSize(4);
        assertThat(result.get(0)).isEqualTo(LocalDate.of(2024, 1, 10));
        assertThat(result.get(3)).isEqualTo(LocalDate.of(2025, 3, 5));

        // 💡 HINTS:
        // dates.stream().filter(d -> d.isAfter(cutoff)).sorted().collect(Collectors.toList())
    }

    @Test
    @DisplayName("🚨 TODO 19: Calculate days until next birthday")
    public void daysUntilBirthday() {
        LocalDate birthday    = LocalDate.of(1990, 8, 15); // Birth date
        LocalDate today       = LocalDate.of(2024, 3, 15);

        // 🚨 TO DO: Find next occurrence of this birthday from 'today'
        LocalDate nextBirthday = null; // ❌ BROKEN

        // 🚨 TO DO: Days until next birthday
        long daysUntil = 0L; // ❌ BROKEN

        assertThat(nextBirthday).isEqualTo(LocalDate.of(2024, 8, 15));
        assertThat(daysUntil).isEqualTo(153L);

        // 💡 HINTS:
        // LocalDate candidate = birthday.withYear(today.getYear());
        // if (!candidate.isAfter(today)) candidate = candidate.plusYears(1);
        // ChronoUnit.DAYS.between(today, candidate)
    }

    @Test
    @DisplayName("🚨 TODO 20: Duration - measure execution time pattern")
    public void measureDuration() {
        Instant before = Instant.now();

        // Simulate some work
        long sum = 0;
        for (int i = 0; i < 1_000_000; i++) sum += i;

        Instant after = Instant.now();

        // 🚨 TO DO: Calculate duration between before and after
        Duration elapsed = null; // ❌ BROKEN

        assertThat(elapsed.toMillis()).isGreaterThanOrEqualTo(0L);
        assertThat(elapsed.isNegative()).isFalse();
        assertThat(sum).isEqualTo(499999500000L); // just to use sum

        // 💡 HINTS:
        // Duration.between(before, after)
        // This pattern is used to benchmark/profile code sections
    }
}
