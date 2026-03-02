# 08 · Advanced Streams

> **When to read:** After attempting Advanced Streams katas, before checking solutions.

---

## Core Idea

This doc covers stream patterns that go beyond the basics — custom collectors, spliterators, sliding windows, parallel tuning, and predicate composition. These are the topics that separate senior candidates from mid-level ones.

---

## Custom Collector with `Collector.of()`

A `Collector` has four components:

```
Supplier<A>          — create the mutable accumulation container
BiConsumer<A, T>     — fold each element into the container
BinaryOperator<A>    — combine two containers (for parallel)
Function<A, R>       — finish: transform container to final result
```

```java
// Collector that joins strings into "[a, b, c]"
Collector<String, StringBuilder, String> bracketJoin = Collector.of(
    StringBuilder::new,                              // supplier
    (sb, s) -> {                                     // accumulator
        if (sb.length() > 0) sb.append(", ");
        sb.append(s);
    },
    (sb1, sb2) -> sb1.append(", ").append(sb2),     // combiner (parallel)
    sb -> "[" + sb + "]"                             // finisher
);

String result = Stream.of("x", "y", "z").collect(bracketJoin);
// "[x, y, z]"
```

**Characteristics** (optional 5th arg):
- `CONCURRENT` — accumulator can be called concurrently
- `UNORDERED` — result doesn't depend on encounter order
- `IDENTITY_FINISH` — finisher is identity function (skip it)

---

## `Collectors.reducing`

Use inside `groupingBy` when `Stream.reduce` isn't available as a downstream:

```java
// Max salary per department
Map<String, Optional<Employee>> topByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDept,
        Collectors.reducing(BinaryOperator.maxBy(
            Comparator.comparing(Employee::getSalary)
        ))
    ));

// Sum of salaries per department
Map<String, Integer> sumByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDept,
        Collectors.reducing(0, Employee::getSalary, Integer::sum)
    ));
```

---

## Sliding Window

There is no built-in `window()` in Java streams. Use `IntStream.range` + `subList`:

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
int windowSize = 3;

List<List<Integer>> windows = IntStream.range(0, numbers.size() - windowSize + 1)
    .mapToObj(i -> numbers.subList(i, i + windowSize))
    .collect(Collectors.toList());
// [[1,2,3], [2,3,4], [3,4,5], [4,5,6]]
```

---

## Running Total (Prefix Sum)

```java
List<Integer> amounts = List.of(10, 20, 15, 30);

// Using AtomicInteger (stateful but pragmatic)
AtomicInteger running = new AtomicInteger(0);
List<Integer> runningTotal = amounts.stream()
    .map(n -> running.addAndGet(n))
    .collect(Collectors.toList());
// [10, 30, 45, 75]
```

> Avoid statefulness in parallel streams. For parallel-safe running totals, use `reduce` or a sequential stream explicitly.

---

## Zipping Two Lists

Java has no built-in `zip`. Use `IntStream.range`:

```java
List<String> keys   = List.of("a", "b", "c");
List<Integer> values = List.of(1, 2, 3);

List<String> zipped = IntStream.range(0, Math.min(keys.size(), values.size()))
    .mapToObj(i -> keys.get(i) + "=" + values.get(i))
    .collect(Collectors.toList());
// ["a=1", "b=2", "c=3"]
```

---

## Top N by Criteria

```java
// Top 3 most expensive orders
List<Order> top3 = orders.stream()
    .sorted(Comparator.comparing(Order::getAmount).reversed())
    .limit(3)
    .collect(Collectors.toList());

// Top N per group using groupingBy + downstream sort
Map<String, List<Order>> topByCategory = orders.stream()
    .collect(Collectors.groupingBy(
        Order::getCategory,
        Collectors.collectingAndThen(
            Collectors.toList(),
            list -> list.stream()
                .sorted(Comparator.comparing(Order::getAmount).reversed())
                .limit(3)
                .collect(Collectors.toList())
        )
    ));
```

---

## Custom Spliterator

A `Spliterator` defines how to traverse and partition elements. Use it to create streams from custom sources.

```java
// Spliterator that yields every other element
class EvenIndexSpliterator<T> implements Spliterator<T> {
    private final List<T> source;
    private int index;

    EvenIndexSpliterator(List<T> source) {
        this.source = source;
        this.index = 0;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (index >= source.size()) return false;
        action.accept(source.get(index));
        index += 2;   // skip odd indices
        return true;
    }

    @Override public Spliterator<T> trySplit() { return null; }
    @Override public long estimateSize() { return source.size() / 2; }
    @Override public int characteristics() { return ORDERED | SIZED; }
}

// Use it
List<String> list = List.of("a", "b", "c", "d", "e");
Stream<String> evens = StreamSupport.stream(
    new EvenIndexSpliterator<>(list), false
);
// a, c, e
```

---

## Parallel Streams — When and How

### When to use
- Dataset > ~10,000 elements
- CPU-intensive per-element work
- Stateless, order-independent operations
- No shared mutable state

### When NOT to use
- Small datasets (overhead > gain)
- I/O-bound work (use `CompletableFuture` instead)
- Ordered terminal ops like `findFirst`, `forEachOrdered` (negates parallel benefit)
- Collecting into non-thread-safe containers

```java
// ✅ Good parallel use — CPU heavy, stateless
List<Result> results = bigList.parallelStream()
    .filter(item -> expensivePredicate(item))
    .map(item -> heavyTransform(item))
    .collect(Collectors.toList());   // thread-safe collector

// ❌ Bad — shared mutable state
List<String> bag = new ArrayList<>();
stream.parallelStream().forEach(bag::add);  // race condition!

// ✅ Fix
List<String> bag = stream.parallelStream().collect(Collectors.toList());
```

### Custom ForkJoinPool

```java
// Default: ForkJoinPool.commonPool() — shared with rest of JVM
// Override for isolation:
ForkJoinPool pool = new ForkJoinPool(4);
List<Result> results = pool.submit(() ->
    bigList.parallelStream()
           .map(this::process)
           .collect(Collectors.toList())
).get();
pool.shutdown();
```

---

## Complex Predicate Chains

```java
Predicate<Order> isPaid     = o -> "PAID".equals(o.getStatus());
Predicate<Order> isRecent   = o -> o.getDate().isAfter(LocalDate.now().minusDays(30));
Predicate<Order> isLarge    = o -> o.getAmount() > 500;

// Compose
Predicate<Order> vipOrder = isPaid.and(isRecent).and(isLarge);

// Dynamic predicate from list of rules
List<Predicate<Order>> rules = List.of(isPaid, isRecent, isLarge);
Predicate<Order> all = rules.stream()
    .reduce(x -> true, Predicate::and);   // fold: true AND p1 AND p2 AND p3

Predicate<Order> any = rules.stream()
    .reduce(x -> false, Predicate::or);   // fold: false OR p1 OR p2 OR p3
```

---

## `Stream.iterate` with Two-Arg Form (Java 9+)

Java 9 added a bounded `iterate`:

```java
// Java 8 — must use limit()
Stream.iterate(1, n -> n * 2).limit(10)

// Java 9+ — built-in termination condition
Stream.iterate(1, n -> n < 1024, n -> n * 2)
//              ↑ seed  ↑ hasNext        ↑ next
```

In Java 8 katas, always use the two-arg form + `limit()`.

---

## `Stream.generate` vs `Stream.iterate`

```java
// generate — stateless, each call independent
Stream.generate(Math::random)    // infinite random doubles
Stream.generate(() -> "x")       // infinite "x"s

// iterate — stateful, each element derived from previous
Stream.iterate(0, n -> n + 1)   // 0, 1, 2, 3 ...
Stream.iterate(LocalDate.now(), d -> d.plusDays(1))  // date sequence
```

---

## Deep `flatMap` (Multi-Level)

```java
// 3-level deep flatten
List<List<List<Integer>>> deep = List.of(
    List.of(List.of(1, 2), List.of(3, 4)),
    List.of(List.of(5, 6), List.of(7, 8))
);

List<Integer> flat = deep.stream()
    .flatMap(List::stream)          // → List<List<Integer>>
    .flatMap(List::stream)          // → List<Integer>
    .collect(Collectors.toList());
// [1, 2, 3, 4, 5, 6, 7, 8]
```

---

## Common Interview Questions

| Question | Key point |
|----------|-----------|
| How to create a custom Collector? | `Collector.of(supplier, accumulator, combiner, finisher)` |
| How to implement sliding window? | `IntStream.range(0, size - w + 1).mapToObj(i -> list.subList(i, i+w))` |
| How to zip two lists? | `IntStream.range(0, min(a.size, b.size)).mapToObj(i -> ...)` |
| When is parallel stream harmful? | Small data, I/O work, shared mutable state, ordered operations. |
| How to build a dynamic predicate? | `predicates.stream().reduce(x -> true, Predicate::and)` |

---

## Gotchas

```java
// ❌ Stream.generate with mutable state — not safe for parallel
int[] counter = {0};
Stream.generate(() -> counter[0]++)   // unpredictable in parallel

// ✅ Use Stream.iterate for sequential state
Stream.iterate(0, n -> n + 1)

// ❌ Collecting to List inside Collector.of combiner (parallel issue)
// combiner must MERGE, not append to left
(list1, list2) -> { list1.addAll(list2); return list1; }  // mutates list1 ✅
(list1, list2) -> Stream.concat(list1.stream(), list2.stream())
                        .collect(Collectors.toList())  // creates new — ok but slower

// ❌ Running total in parallel stream
AtomicInteger sum = new AtomicInteger();
list.parallelStream().map(n -> sum.addAndGet(n))  // wrong order of accumulation
// Use sequential stream or reduce for correct running totals
```
