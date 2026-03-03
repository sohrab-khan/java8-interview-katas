# ☕ Java 8 Interview Katas

> **155 production-ready JUnit 5 katas** to master Java 8 features.  
> Fix broken tests → See them turn GREEN → Ace your interviews. 🎯

[![Java 8](https://img.shields.io/badge/Java-8-orange?logo=java)](https://www.java.com)
[![JUnit5](https://img.shields.io/badge/JUnit-5.10-green?logo=junit5)](https://junit.org/junit5/)
[![AssertJ](https://img.shields.io/badge/AssertJ-3.26-blue)](https://assertj.github.io/doc/)
[![Maven CI](https://github.com/sohrab-khan/java8-interview-katas/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/sohrab-khan/java8-interview-katas/actions/workflows/maven.yml)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](CONTRIBUTING.md)

---

## 🚀 60-Second Quickstart

```bash
git clone https://github.com/sohrab-khan/java8-interview-katas
cd java8-interview-katas

# See the RED failing todo tests
mvn test -Dtest="**Todo*"

# Fix one test at a time, run it
mvn test -Dtest="LambdaBasicsTodoTest#filterPaidOrders"

# Verify all solutions (reference answers)
mvn test -Dtest="**Solution*"
```

No setup needed beyond Java 8+ and Maven.

---

## 📖 Reference Docs

Each module has a companion reference doc in the [`/docs`](docs/) folder.

> **Recommended flow:** attempt katas → stuck? read the doc → check solutions

| Doc | Covers |
|-----|--------|
| [01 · Lambda & Functional Interfaces](docs/01-lambda-and-functional-interfaces.md) | Predicate/Function composition, method references, variable capture |
| [02 · Stream API](docs/02-streams.md) | Pipeline, flatMap, reduce, primitive streams, parallel |
| [03 · Collectors](docs/03-collectors.md) | groupingBy, toMap, partitioningBy, custom Collector |
| [04 · Optional](docs/04-optional.md) | orElse vs orElseGet, map/flatMap, when NOT to use it |
| [05 · Date-Time API](docs/05-datetime-api.md) | LocalDate/Time/DateTime, ZonedDateTime, Period vs Duration, TemporalAdjusters |
| [06 · Map API](docs/06-map-api.md) | computeIfAbsent, merge, getOrDefault, replaceAll |
| [07 · CompletableFuture](docs/07-completablefuture.md) | Async pipeline, thenCompose, exceptionally, allOf/anyOf |
| [08 · Advanced Streams](docs/08-advanced-streams.md) | Custom Collector, Spliterator, sliding window, parallel tuning |

See [`docs/README.md`](docs/README.md) for the full index, a quick-lookup table, and the **top 10 interview gotchas**.

---

## 📊 155 Katas Coverage

| Module | Katas | Topics |
|--------|-------|--------|
| 🔵 Lambda Basics | 20 | Predicates, Functions, Consumers, Comparators, Method References |
| 🟢 Stream Operations | 25 | filter, map, flatMap, reduce, peek, limit, skip, parallel |
| 🟡 Collectors | 25 | groupingBy, toMap, partitioningBy, joining, custom, summarizing |
| 🔴 Optional | 15 | of, map, flatMap, filter, orElse, orElseGet, null-safe chains |
| 🟣 CompletableFuture | 20 | supplyAsync, thenApply, thenCompose, allOf, anyOf, exceptionally |
| ⚫ Advanced Streams | 10 | Spliterator, custom Collector, sliding window, zip, deep flatMap |
| 📅 Date-Time API | 20 | LocalDate/Time/DateTime, ZonedDateTime, Period, Duration, TemporalAdjusters |
| 🗺️ Map API | 15 | computeIfAbsent, merge, getOrDefault, forEach, replaceAll, entrySet |
| 🟤 Production Patterns | 5 | Pagination, validation chains, batching, indexing, reports |
| **Total** | **155** | **Pure Java 8 — zero newer syntax** |

---

## 📁 Project Structure

```
java8-interview-katas/
├── docs/                              ← 📖 Reference docs (one per module)
│   ├── README.md                      ← Index + top 10 gotchas
│   ├── 01-lambda-and-functional-interfaces.md
│   ├── 02-streams.md
│   ├── 03-collectors.md
│   ├── 04-optional.md
│   ├── 05-datetime-api.md
│   ├── 06-map-api.md
│   ├── 07-completablefuture.md
│   └── 08-advanced-streams.md
└── src/test/java/com/mrkhan/katas/
    ├── OrderData.java                 ← Shared test data (Orders, Users, Transactions, Products)
    ├── basics/
    │   ├── todo/LambdaBasicsTodoTest.java           ← 🚨 FIX THESE
    │   └── solutions/LambdaBasicsSolutionTest.java  ← ✅ REFERENCE
    ├── streams/
    │   ├── todo/StreamOperationsTodoTest.java
    │   └── solutions/StreamOperationsSolutionTest.java
    ├── collectors/
    │   ├── todo/CollectorsTodoTest.java
    │   └── solutions/CollectorsSolutionTest.java
    ├── optional/
    │   ├── todo/OptionalTodoTest.java
    │   └── solutions/OptionalSolutionTest.java
    ├── completablefuture/
    │   ├── todo/CompletableFutureTodoTest.java
    │   └── solutions/CompletableFutureSolutionTest.java
    ├── advanced/
    │   ├── todo/AdvancedStreamsTodoTest.java
    │   └── solutions/AdvancedStreamsSolutionTest.java
    ├── datetime/
    │   ├── todo/DateTimeTodoTest.java
    │   └── solutions/DateTimeSolutionTest.java
    ├── mapapi/
    │   ├── todo/MapApiTodoTest.java
    │   └── solutions/MapApiSolutionTest.java
    └── production/
        ├── todo/ProductionPatternsTodoTest.java
        └── solutions/ProductionPatternsSolutionTest.java
```

---

## 🎓 How Each Kata Works

Every broken `todo` test looks like this:

```java
@Test
@DisplayName("🚨 TODO 1: Filter paid orders using Predicate")
public void filterPaidOrders() {
    List<Order> orders = OrderData.sampleOrders();

    // 🚨 TO DO: Create a Predicate<Order> that returns true for "PAID" orders
    Predicate<Order> isPaid = null; // ❌ BROKEN - fix this

    List<String> result = orders.stream()
            .filter(isPaid)
            .map(Order::getCustomerName)
            .collect(Collectors.toList());

    assertThat(result).containsExactlyInAnyOrder("John", "Bob", "Diana", "Frank", "Hank");

    // 💡 HINTS:
    // 1. isPaid = order -> "PAID".equals(order.getStatus());
}
```

The matching solution includes **interview insights**:

```java
@Test
@DisplayName("✅ SOLUTION 1: Filter paid orders using Predicate")
public void filterPaidOrders_solution() {
    Predicate<Order> isPaid = order -> "PAID".equals(order.getStatus());
    // ...
    // 💡 INTERVIEW INSIGHTS:
    // 1. "PAID".equals(...) prevents NPE if status is null
    // 2. Predicate<T> is a functional interface: T -> boolean
    // 3. Prefer method reference Order::getCustomerName over o -> o.getCustomerName()
}
```

---

## 🗓️ 30-Day Mastery Tracker

| Day | Module | Katas | Read First | Done |
|-----|--------|-------|-----------|------|
| 1-2 | Lambda Basics | 1-10 | [doc 01](docs/01-lambda-and-functional-interfaces.md) | ⬜ |
| 3-4 | Lambda Basics | 11-20 | [doc 01](docs/01-lambda-and-functional-interfaces.md) | ⬜ |
| 5-7 | Stream Operations | 1-15 | [doc 02](docs/02-streams.md) | ⬜ |
| 8-9 | Stream Operations | 16-25 | [doc 02](docs/02-streams.md) | ⬜ |
| 10-12 | Collectors | 1-12 | [doc 03](docs/03-collectors.md) | ⬜ |
| 13-14 | Collectors | 13-25 | [doc 03](docs/03-collectors.md) | ⬜ |
| 15-16 | Optional | 1-15 | [doc 04](docs/04-optional.md) | ⬜ |
| 17-18 | Date-Time API | 1-10 | [doc 05](docs/05-datetime-api.md) | ⬜ |
| 19-20 | Date-Time API | 11-20 | [doc 05](docs/05-datetime-api.md) | ⬜ |
| 21-22 | Map API | 1-15 | [doc 06](docs/06-map-api.md) | ⬜ |
| 23-25 | CompletableFuture | 1-12 | [doc 07](docs/07-completablefuture.md) | ⬜ |
| 26-27 | CompletableFuture | 13-20 | [doc 07](docs/07-completablefuture.md) | ⬜ |
| 28-29 | Advanced Streams | 1-10 | [doc 08](docs/08-advanced-streams.md) | ⬜ |
| 30 | Production Patterns | 1-5 | review all docs | ⬜ |

---

## 💡 Java 8 Features Covered (Strictly)

✅ Lambda expressions (all syntax forms)  
✅ Functional interfaces (`Predicate`, `Function`, `Consumer`, `Supplier`, `BiFunction`, etc.)  
✅ Method references (static, bound, unbound, constructor)  
✅ Stream API (`filter`, `map`, `flatMap`, `collect`, `reduce`, `peek`, `sorted`, etc.)  
✅ `Collectors` (`groupingBy`, `toMap`, `joining`, `partitioningBy`, `summarizing`, custom)  
✅ `Optional<T>` (all methods)  
✅ `CompletableFuture<T>` (all Java 8 async patterns)  
✅ `Comparator` factory methods  
✅ Default and static methods in interfaces  
✅ Primitive streams (`IntStream`, `LongStream`, `DoubleStream`)  
✅ `java.time` API (`LocalDate`, `LocalDateTime`, `ZonedDateTime`, `Duration`, `Period`, `DateTimeFormatter`)  
✅ `Map` new methods (`computeIfAbsent`, `merge`, `getOrDefault`, `forEach`, `replaceAll`)  

❌ NO records, NO var, NO text blocks, NO pattern matching, NO sealed classes  
❌ NO Java 9-21 features (takeWhile, dropWhile, Optional.or, etc.)

---

## 🏆 Interview Questions Mapped to Katas

| Interview Question | Kata |
|-------------------|------|
| What is a Predicate? Show Predicate.and/or/negate | Lambda-1, 2, 3 |
| Explain Function composition (andThen vs compose) | Lambda-4, 5 |
| What is the difference between map and flatMap? | Streams-4, 5 |
| When use parallel streams? | Streams-25, Advanced-9 |
| What is Optional and orElse vs orElseGet? | Optional-2, 3 |
| Explain groupingBy with downstream collectors | Collectors-1 to 8 |
| What is CompletableFuture thenCompose vs thenApply? | CF-4, 7 |
| How to handle exceptions in CompletableFuture? | CF-9, 10, 11 |
| LocalDate vs LocalDateTime vs ZonedDateTime? | DateTime-1, 8, 13 |
| Period vs Duration? ChronoUnit? | DateTime-5, 7 |
| What is computeIfAbsent? When to use merge? | MapAPI-5, 8, 9 |
| How to sort a Map by value? | MapAPI-11 |

---

## 🤝 Contributing

Contributions welcome! To add a new kata:

1. Add a `TODO` test in the appropriate `todo/` folder
2. Add the matching `Solution` test in `solutions/`
3. Follow the existing format (hints + interview insights)
4. Submit a PR — merged same day!

---

## 📚 Resources

- [Java 8 in Action (Manning)](https://www.manning.com/books/java-8-in-action)
- [Effective Java 3rd Ed. - Bloch](https://www.effectivejava.com/)
- [Baeldung Java Streams Guide](https://www.baeldung.com/java-8-streams)
- [Oracle Java 8 Docs](https://docs.oracle.com/javase/8/docs/)

---

## ⭐ Star History

If this repo helped you land a job or ace an interview, please ⭐ star it!

> **"Solved 50/120 Java 8 katas! My stream API skills went from 0 to production-ready in 2 weeks."**

---

Made with ☕ for Java engineers preparing for senior/principal interviews.
