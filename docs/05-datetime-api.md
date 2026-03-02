# 05 · Date-Time API (`java.time`)

> **When to read:** After attempting Date-Time katas, before checking solutions.

---

## Why a New API?

`java.util.Date` and `Calendar` were broken by design:

| Problem | Old API | `java.time` |
|---------|---------|-------------|
| Thread safety | Mutable — not safe | Immutable — always safe |
| Month indexing | 0-based (January = 0) | 1-based (January = 1) |
| Time zone | Confusing, error-prone | Explicit, clear |
| Arithmetic | Manual Calendar math | `plusDays()`, `minusMonths()` |
| Formatting | `SimpleDateFormat` — not thread-safe | `DateTimeFormatter` — immutable |

---

## Class Overview

```
LocalDate        — date only, no time, no zone        (2024-03-15)
LocalTime        — time only, no date, no zone        (14:30:45)
LocalDateTime    — date + time, no zone               (2024-03-15T14:30:45)
ZonedDateTime    — date + time + zone                 (2024-03-15T14:30:45-05:00[America/New_York])
OffsetDateTime   — date + time + UTC offset           (2024-03-15T14:30:45-05:00)
Instant          — machine timestamp (epoch seconds)  (suitable for storage/comparison)
Duration         — time-based amount  (hours, minutes, seconds)
Period           — date-based amount  (years, months, days)
```

**Rule of thumb:**
- Store/compare → `Instant`
- Display/schedule with zone → `ZonedDateTime`
- Business logic without zone → `LocalDate` / `LocalDateTime`
- Human-readable span → `Period`
- Programmatic span → `Duration` or `ChronoUnit`

---

## LocalDate

```java
// Creation
LocalDate today  = LocalDate.now();
LocalDate date   = LocalDate.of(2024, 3, 15);          // year, month (1-based), day
LocalDate date   = LocalDate.of(2024, Month.MARCH, 15);
LocalDate parsed = LocalDate.parse("2024-03-15");       // ISO-8601 default

// Extracting fields
date.getYear()          // 2024
date.getMonthValue()    // 3  (1-based!)
date.getMonth()         // Month.MARCH
date.getDayOfMonth()    // 15
date.getDayOfWeek()     // DayOfWeek.FRIDAY
date.isLeapYear()       // true/false

// Arithmetic (all return NEW instance)
date.plusDays(10)
date.plusWeeks(2)
date.plusMonths(1)
date.plusYears(1)
date.minusDays(5)       // etc.

// Replace components
date.withYear(2025)
date.withMonth(12)
date.withDayOfMonth(1)

// Comparisons
d1.isBefore(d2)
d1.isAfter(d2)
d1.isEqual(d2)
```

---

## LocalTime

```java
LocalTime time = LocalTime.of(14, 30, 45);   // hour, minute, second
LocalTime time = LocalTime.parse("14:30:45");

time.getHour()     // 14
time.getMinute()   // 30
time.getSecond()   // 45

time.plusHours(2)
time.plusMinutes(90)
time.minusSeconds(30)
time.withHour(9)
```

---

## LocalDateTime

```java
LocalDateTime dt = LocalDateTime.of(2024, 3, 15, 14, 30);
LocalDateTime dt = LocalDateTime.of(localDate, localTime);
LocalDateTime dt = LocalDateTime.parse("2024-03-15T14:30:45");

// Supports all LocalDate and LocalTime methods
dt.plusDays(1).plusHours(2)
dt.toLocalDate()   // extract date part
dt.toLocalTime()   // extract time part
```

---

## Period and Duration

```java
// Period — date-based (years / months / days)
Period p = Period.between(startDate, endDate);
p.getYears()    // component years
p.getMonths()   // component months
p.getDays()     // component days

Period.of(1, 6, 15)   // 1 year, 6 months, 15 days
date.plus(period)

// Duration — time-based (seconds / nanoseconds)
Duration d = Duration.between(startTime, endTime);
d.toHours()     // total hours (not component!)
d.toMinutes()   // total minutes
d.getSeconds()

Duration.ofHours(8)
Duration.ofMinutes(30)

// ChronoUnit — total difference in one unit
long days   = ChronoUnit.DAYS.between(d1, d2);
long months = ChronoUnit.MONTHS.between(d1, d2);
long hours  = ChronoUnit.HOURS.between(dt1, dt2);
```

> **Interview trap:** `Period.getDays()` returns only the days component, not total days. Use `ChronoUnit.DAYS.between()` for total days.

---

## TemporalAdjusters

Pre-built complex adjustments:

```java
import java.time.temporal.TemporalAdjusters;

date.with(TemporalAdjusters.firstDayOfMonth())
date.with(TemporalAdjusters.lastDayOfMonth())
date.with(TemporalAdjusters.firstDayOfNextMonth())
date.with(TemporalAdjusters.firstDayOfYear())
date.with(TemporalAdjusters.lastDayOfYear())

date.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
date.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))  // includes today if already Monday
date.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY))

date.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY))  // first Monday of month
date.with(TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY))   // last Friday of month
```

**Custom TemporalAdjuster** — it's a `@FunctionalInterface`:

```java
TemporalAdjuster nextBusinessDay = temporal -> {
    DayOfWeek dow = DayOfWeek.from(temporal);
    int daysToAdd = switch (dow) {          // pre-Java 14: use if/else
        case FRIDAY   -> 3;
        case SATURDAY -> 2;
        default       -> 1;
    };
    return temporal.plus(daysToAdd, ChronoUnit.DAYS);
};

LocalDate next = date.with(nextBusinessDay);
```

---

## ZonedDateTime and Time Zones

```java
ZoneId ny     = ZoneId.of("America/New_York");
ZoneId london = ZoneId.of("Europe/London");
ZoneId tokyo  = ZoneId.of("Asia/Tokyo");

// Create
ZonedDateTime zdt = ZonedDateTime.now(ny);
ZonedDateTime zdt = ZonedDateTime.of(localDateTime, ny);

// Convert zone — same instant, different representation
ZonedDateTime tokyoTime = nyTime.withZoneSameInstant(tokyo);

// Same local clock, different instant (e.g. scheduling recurring events)
ZonedDateTime sameLocalTime = nyTime.withZoneSameLocal(london);

// To/from Instant
Instant instant = zdt.toInstant();
ZonedDateTime back = instant.atZone(ZoneId.of("UTC"));
```

**`withZoneSameInstant` vs `withZoneSameLocal`:**
- `withZoneSameInstant` → "what time is it in Tokyo right now?" (keeps the moment)
- `withZoneSameLocal` → "schedule the same time in Tokyo" (keeps the clock reading)

---

## DateTimeFormatter

```java
// Predefined
DateTimeFormatter.ISO_LOCAL_DATE          // "2024-03-15"
DateTimeFormatter.ISO_LOCAL_DATE_TIME     // "2024-03-15T14:30:45"

// Custom pattern
DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");

// Format
String s = date.format(fmt);

// Parse
LocalDate d  = LocalDate.parse("15/03/2024", fmt);
LocalDateTime dt = LocalDateTime.parse("15-Mar-2024 14:30", fmt);

// Locale-aware (always specify for day/month names)
DateTimeFormatter fmt = DateTimeFormatter
    .ofPattern("EEEE, MMMM dd, yyyy")
    .withLocale(Locale.US);
```

**Pattern tokens:**

| Token | Meaning | Example |
|-------|---------|---------|
| `yyyy` | 4-digit year | `2024` |
| `MM` | 2-digit month | `03` |
| `MMM` | Short month name | `Mar` |
| `MMMM` | Full month name | `March` |
| `dd` | Day of month | `15` |
| `EEE` | Short day name | `Fri` |
| `EEEE` | Full day name | `Friday` |
| `HH` | 24-hour | `14` |
| `hh` | 12-hour | `02` |
| `mm` | Minutes | `30` |
| `ss` | Seconds | `45` |
| `a` | AM/PM | `PM` |

> **Interview trap:** `SimpleDateFormat` is NOT thread-safe. `DateTimeFormatter` IS thread-safe (immutable). Share `DateTimeFormatter` instances freely; never share `SimpleDateFormat`.

---

## Instant

```java
Instant now     = Instant.now();
Instant epoch   = Instant.ofEpochSecond(1704067200L);
Instant epochMs = Instant.ofEpochMilli(1704067200000L);

instant.getEpochSecond()   // seconds since 1970-01-01T00:00:00Z
instant.toEpochMilli()     // milliseconds

// Arithmetic
instant.plusSeconds(3600)
instant.plus(1, ChronoUnit.HOURS)

// Benchmarking pattern
Instant start   = Instant.now();
// ... work ...
Instant end     = Instant.now();
Duration elapsed = Duration.between(start, end);
System.out.println("Elapsed: " + elapsed.toMillis() + "ms");
```

---

## Streams with Dates

```java
// Generate a week of dates
List<LocalDate> week = Stream.iterate(startDate, d -> d.plusDays(1))
    .limit(7)
    .collect(Collectors.toList());

// Filter and sort
List<LocalDate> future = dates.stream()
    .filter(d -> d.isAfter(LocalDate.now()))
    .sorted()    // LocalDate implements Comparable
    .collect(Collectors.toList());

// Days until each date
dates.stream()
    .mapToLong(d -> ChronoUnit.DAYS.between(LocalDate.now(), d))
    .filter(days -> days >= 0)
    .min()
    .ifPresent(days -> System.out.println("Next event in " + days + " days"));
```

---

## Common Interview Questions

| Question | Key point |
|----------|-----------|
| `Period` vs `Duration`? | `Period` = date-based (years/months/days). `Duration` = time-based (seconds/nanos). |
| `Period.getDays()` vs `ChronoUnit.DAYS`? | `getDays()` is the days component only. `ChronoUnit.DAYS.between` gives total days. |
| Is `DateTimeFormatter` thread-safe? | Yes — immutable. `SimpleDateFormat` is NOT. |
| `withZoneSameInstant` vs `withZoneSameLocal`? | Same instant = convert. Same local = reschedule. |
| How to store dates in DB? | Use `Instant` or `LocalDate`/`LocalDateTime` + a DB column type. Never `java.util.Date`. |

---

## Gotchas

```java
// ❌ Month is 0-based in old API — muscle memory trap
new Date(2024, 2, 15)           // this is MARCH (month index 2!)
Calendar.MARCH                  // = 2

// ✅ java.time months are 1-based
LocalDate.of(2024, 3, 15)       // this is MARCH — no surprises

// ❌ getDays() is NOT total days
Period p = Period.between(d1, d2);
p.getDays()    // only the day component — could be 0 even if months apart

// ✅
ChronoUnit.DAYS.between(d1, d2)   // actual total day difference

// ❌ Forgetting locale for name-based patterns
DateTimeFormatter.ofPattern("EEEE, MMMM dd")
    .format(date);   // output depends on JVM default locale — CI failure risk

// ✅
DateTimeFormatter.ofPattern("EEEE, MMMM dd")
    .withLocale(Locale.ENGLISH)
    .format(date);
```
