# 03 · Collectors

> **When to read:** After attempting Collectors katas, before checking solutions.

---

## Core Idea

`Collectors` are recipes for how to accumulate stream elements into a result container — a List, Map, String, or custom structure. They are passed to `stream.collect(collector)`.

---

## The Essentials

```java
// To List (most common)
stream.collect(Collectors.toList())

// To Set (deduplicates)
stream.collect(Collectors.toSet())

// To specific collection type
stream.collect(Collectors.toCollection(LinkedList::new))
stream.collect(Collectors.toCollection(TreeSet::new))
```

---

## Joining Strings

```java
List<String> names = List.of("Alice", "Bob", "Charlie");

names.stream().collect(Collectors.joining())           // "AliceBobCharlie"
names.stream().collect(Collectors.joining(", "))       // "Alice, Bob, Charlie"
names.stream().collect(Collectors.joining(", ", "[", "]")) // "[Alice, Bob, Charlie]"
```

---

## toMap

```java
// Basic: name → length
Map<String, Integer> nameToLen = names.stream()
    .collect(Collectors.toMap(
        name -> name,       // key extractor
        String::length      // value extractor
    ));

// Using Function.identity() for key = element itself
Map<String, Person> byName = people.stream()
    .collect(Collectors.toMap(Person::getName, p -> p));
// or cleaner:
    .collect(Collectors.toMap(Person::getName, Function.identity()));

// With merge function — handles duplicate keys
Map<String, Integer> merged = stream.collect(Collectors.toMap(
    Order::getCategory,
    o -> 1,
    Integer::sum            // if duplicate key: sum the values
));
```

> **Interview trap:** `toMap` without a merge function throws `IllegalStateException` on duplicate keys.

---

## groupingBy

The most powerful and most-asked collector.

```java
// Basic grouping — Map<K, List<V>>
Map<String, List<Order>> byStatus = orders.stream()
    .collect(Collectors.groupingBy(Order::getStatus));
// {"PAID": [...], "PENDING": [...]}

// With downstream collector
Map<String, Long> countByStatus = orders.stream()
    .collect(Collectors.groupingBy(
        Order::getStatus,
        Collectors.counting()
    ));

Map<String, Double> sumByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.summingDouble(Employee::getSalary)
    ));

Map<String, Double> avgByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.averagingDouble(Employee::getSalary)
    ));

// Mapping downstream — get names not objects
Map<String, List<String>> namesByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.mapping(Employee::getName, Collectors.toList())
    ));

// maxBy / minBy downstream
Map<String, Optional<Employee>> topEarnerByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.maxBy(Comparator.comparing(Employee::getSalary))
    ));

// Into sorted map (TreeMap)
Map<String, List<Order>> sorted = orders.stream()
    .collect(Collectors.groupingBy(
        Order::getCategory,
        TreeMap::new,           // map factory
        Collectors.toList()
    ));

// Multi-level grouping
Map<String, Map<String, List<Employee>>> byDeptThenRole = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.groupingBy(Employee::getRole)
    ));
```

---

## partitioningBy

Special case of `groupingBy` — splits into exactly two groups: `true` and `false`.

```java
Map<Boolean, List<Order>> paidVsNot = orders.stream()
    .collect(Collectors.partitioningBy(
        o -> "PAID".equals(o.getStatus())
    ));

List<Order> paid    = paidVsNot.get(true);
List<Order> notPaid = paidVsNot.get(false);

// With downstream
Map<Boolean, Long> countPaidVsNot = orders.stream()
    .collect(Collectors.partitioningBy(
        o -> "PAID".equals(o.getStatus()),
        Collectors.counting()
    ));
```

---

## Numeric Collectors

```java
// counting
long count = stream.collect(Collectors.counting());

// summing
int totalAge  = stream.collect(Collectors.summingInt(Person::getAge));
double total  = stream.collect(Collectors.summingDouble(Order::getAmount));

// averaging
double avg    = stream.collect(Collectors.averagingInt(Person::getAge));

// summarizing — all stats in one pass
IntSummaryStatistics stats = stream.collect(
    Collectors.summarizingInt(Person::getAge)
);
stats.getCount();
stats.getSum();
stats.getMin();
stats.getMax();
stats.getAverage();

// minBy / maxBy
Optional<Order> mostExpensive = stream.collect(
    Collectors.maxBy(Comparator.comparing(Order::getAmount))
);
```

---

## collectingAndThen

Wrap a collector with a finishing function.

```java
// Collect to list, then make unmodifiable
List<String> immutable = stream.collect(
    Collectors.collectingAndThen(
        Collectors.toList(),
        Collections::unmodifiableList
    )
);

// Get the single result (when you know there's exactly one)
Optional<Order> best = stream.collect(
    Collectors.collectingAndThen(
        Collectors.maxBy(Comparator.comparing(Order::getAmount)),
        opt -> opt
    )
);
```

---

## reducing

```java
// Equivalent to Stream.reduce but usable as downstream collector
Optional<Integer> product = stream.collect(Collectors.reducing(Integer::max));

int sum = stream.collect(Collectors.reducing(0, Integer::sum));

// Useful inside groupingBy
Map<String, Integer> productByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.reducing(0, Employee::getSalary, Integer::sum)
    ));
```

---

## Flat-Collect (Java 8 way)

`Collectors.flatMapping` is Java 9+. In Java 8:

```java
// Collect all tags from all orders into a single Set
Set<String> allTags = orders.stream()
    .flatMap(o -> o.getTags().stream())
    .collect(Collectors.toSet());
```

---

## Custom Collector with `Collector.of()`

```java
// Collector that concatenates strings with a separator, wrapped in brackets
Collector<String, StringBuilder, String> bracketJoiner = Collector.of(
    StringBuilder::new,                          // supplier
    (sb, s) -> {                                 // accumulator
        if (sb.length() > 0) sb.append(", ");
        sb.append(s);
    },
    (sb1, sb2) -> sb1.append(sb2),               // combiner (for parallel)
    sb -> "[" + sb.toString() + "]"              // finisher
);

String result = Stream.of("a", "b", "c").collect(bracketJoiner);
// "[a, b, c]"
```

---

## Quick Reference: Downstream Collectors

| Downstream | Returns | Use with |
|------------|---------|----------|
| `counting()` | `Long` | count per group |
| `summingInt/Long/Double` | numeric | sum per group |
| `averagingInt/Long/Double` | `Double` | average per group |
| `summarizingInt/Long/Double` | `*SummaryStatistics` | all stats per group |
| `mapping(fn, downstream)` | depends | transform before collecting |
| `joining(...)` | `String` | concatenate strings per group |
| `toList()` | `List<T>` | collect group to list |
| `toSet()` | `Set<T>` | collect group to set |
| `maxBy(comparator)` | `Optional<T>` | max per group |
| `minBy(comparator)` | `Optional<T>` | min per group |
| `reducing(...)` | `T` or `Optional<T>` | custom reduction per group |

---

## Common Interview Questions

| Question | Key point |
|----------|-----------|
| `groupingBy` vs `partitioningBy`? | `groupingBy` = any key. `partitioningBy` = always true/false (Map<Boolean, ...>). |
| How to handle duplicate keys in `toMap`? | Pass a merge function as 3rd argument: `(v1, v2) -> v1`. |
| How to group and count? | `groupingBy(key, Collectors.counting())`. |
| How to get names per group, not objects? | `groupingBy(key, mapping(Person::getName, toList()))`. |
| What is `collectingAndThen`? | Applies a finishing transformation after collection (e.g. make unmodifiable). |

---

## Gotchas

```java
// ❌ Duplicate keys → exception
Map<String, Order> byId = orders.stream()
    .collect(Collectors.toMap(Order::getId, o -> o));
// Throws if two orders share the same id

// ✅ With merge
Map<String, Order> byId = orders.stream()
    .collect(Collectors.toMap(Order::getId, o -> o, (o1, o2) -> o1));

// ❌ groupingBy does NOT accept null keys
stream.collect(Collectors.groupingBy(o -> o.getCategory())); // NPE if category is null

// ✅ Handle null key
stream.collect(Collectors.groupingBy(
    o -> o.getCategory() != null ? o.getCategory() : "UNKNOWN"
));
```
