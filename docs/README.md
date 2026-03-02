# Java 8 Interview Prep — Reference Docs

> **How to use these docs**
>
> 1. Attempt the katas in the corresponding `todo` test class first
> 2. If stuck, open the doc for a hint on the concept (not the kata answer)
> 3. After finishing, check the `solutions` test class
> 4. Re-read the doc's "Gotchas" section — that's what interviewers test
>
> Do **not** read the docs before attempting the katas. You learn faster by failing first.

---

## Index

| # | Doc | Covers | Katas |
|---|-----|--------|-------|
| 01 | [Lambda & Functional Interfaces](01-lambda-and-functional-interfaces.md) | Lambda syntax, Predicate/Function/Consumer/Supplier, method references, composition | 20 |
| 02 | [Stream API](02-streams.md) | Pipeline, filter/map/flatMap/reduce, terminal ops, primitive streams, parallel | 25 + 10 |
| 03 | [Collectors](03-collectors.md) | groupingBy, toMap, partitioningBy, joining, custom Collector | 25 |
| 04 | [Optional](04-optional.md) | of/ofNullable, orElse/orElseGet, map/flatMap/filter, null-safe chains | 15 |
| 05 | [Date-Time API](05-datetime-api.md) | LocalDate/Time/DateTime, ZonedDateTime, Period/Duration, TemporalAdjusters, DateTimeFormatter | 20 |
| 06 | [Map API](06-map-api.md) | getOrDefault, computeIfAbsent, merge, forEach, replaceAll, entrySet stream | 15 |
| 07 | [CompletableFuture](07-completablefuture.md) | supplyAsync, thenApply/Compose/Combine, exceptionally, allOf/anyOf | 20 |
| 08 | [Advanced Streams](08-advanced-streams.md) | Custom Collector, Spliterator, sliding window, zip, parallel tuning, predicate chains | 10 |

**Total: 155 katas**

---

## Quick Lookup: Which doc for which concept?

| Concept | Doc |
|---------|-----|
| `Predicate.and() / .or() / .negate()` | 01 |
| `Function.andThen() / .compose()` | 01 |
| `@FunctionalInterface` | 01 |
| Method references (`::`) | 01 |
| `Stream.filter / map / flatMap` | 02 |
| `Stream.reduce` | 02 |
| `IntStream / LongStream / DoubleStream` | 02 |
| `parallelStream` | 02, 08 |
| `Collectors.groupingBy` | 03 |
| `Collectors.toMap` | 03 |
| `Collectors.partitioningBy` | 03 |
| `Collectors.joining` | 03 |
| `Collector.of()` custom collector | 03, 08 |
| `Optional.orElse vs orElseGet` | 04 |
| `Optional.map / flatMap / filter` | 04 |
| `LocalDate / LocalTime / LocalDateTime` | 05 |
| `ZonedDateTime` / time zones | 05 |
| `Period vs Duration` | 05 |
| `TemporalAdjusters` | 05 |
| `DateTimeFormatter` | 05 |
| `Map.computeIfAbsent` | 06 |
| `Map.merge` | 06 |
| `Map.getOrDefault` | 06 |
| `CompletableFuture.supplyAsync` | 07 |
| `thenApply vs thenCompose` | 07 |
| `exceptionally / handle / whenComplete` | 07 |
| `allOf / anyOf` | 07 |
| Sliding window | 08 |
| Zip two lists | 08 |
| Custom `Spliterator` | 08 |

---

## Top 10 Gotchas That Come Up in Interviews

1. **`orElse` always evaluates** — use `orElseGet(() -> ...)` for expensive defaults
2. **`Period.getDays()` ≠ total days** — use `ChronoUnit.DAYS.between(d1, d2)`
3. **`toMap` throws on duplicate keys** — always pass a merge function when duplicates are possible
4. **`SimpleDateFormat` is not thread-safe** — `DateTimeFormatter` is; share it freely
5. **`thenApply` vs `thenCompose`** — if the next step returns a `CompletableFuture`, use `thenCompose`
6. **Parallel stream + mutable list = race condition** — always `collect()` into a thread-safe result
7. **`computeIfAbsent` recursive in Java 8** — throws `ConcurrentModificationException`; use iterative
8. **`putIfAbsent` evaluates the value eagerly** — use `computeIfAbsent` for lazy construction
9. **`Optional` as a field** — not serializable reliably; keep as return type only
10. **`Stream.generate` with mutable state** — unpredictable in parallel; use `Stream.iterate` for sequential state

---

## Recommended Reading Order

**For a senior/principal interview:**
```
01 → 02 → 03 → 04 → 05 → 06 → 07 → 08
```

**For a 48-hour cram:**
```
02 (streams) → 03 (collectors) → 07 (CompletableFuture) → 04 (Optional) → 05 (DateTime)
```

**For a refresher on the most-asked topics:**
```
03 (groupingBy / toMap) → 06 (Map.merge / computeIfAbsent) → 07 (CF pipeline)
```
