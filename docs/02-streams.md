# 02 · Stream API

> **When to read:** After attempting Stream Operations katas, before checking solutions.

---

## Core Idea

A Stream is a **pipeline** for processing data — not a data structure. It:
- Does **not** store elements
- Is **lazy** — intermediate operations run only when a terminal op is called
- Is **single-use** — consumed once, then done
- Supports both sequential and parallel execution

```
Source → [Intermediate ops...] → Terminal op
```

---

## Creating Streams

```java
// From Collection
list.stream()
list.parallelStream()

// From array
Arrays.stream(array)
Stream.of("a", "b", "c")

// Primitive streams (avoid boxing overhead)
IntStream.of(1, 2, 3)
IntStream.range(0, 10)          // 0..9
IntStream.rangeClosed(1, 10)    // 1..10

// Infinite streams
Stream.iterate(0, n -> n + 1)          // 0, 1, 2, 3 ...  (must limit!)
Stream.generate(Math::random)           // random doubles  (must limit!)

// From string chars
"hello".chars()                         // IntStream of char codes

// From file
Files.lines(Paths.get("data.txt"))      // must close!
```

---

## Intermediate Operations (lazy, return Stream)

```java
// filter — keep matching elements
stream.filter(n -> n > 0)
stream.filter(String::isEmpty)

// map — transform each element 1:1
stream.map(String::toUpperCase)
stream.map(s -> s.length())

// flatMap — flatten nested structures (1:many)
stream.flatMap(List::stream)
stream.flatMap(s -> Arrays.stream(s.split(" ")))

// distinct — remove duplicates (uses equals)
stream.distinct()

// sorted — natural order
stream.sorted()
stream.sorted(Comparator.reverseOrder())
stream.sorted(Comparator.comparing(Person::getAge))

// peek — inspect without consuming (debugging only)
stream.peek(e -> System.out.println("before: " + e))

// limit / skip — pagination
stream.limit(10)
stream.skip(20)
```

---

## `map` vs `flatMap`

```java
List<List<Integer>> nested = List.of(List.of(1,2), List.of(3,4));

// map gives Stream<List<Integer>> — not what you want
nested.stream().map(List::stream)       // Stream<Stream<Integer>>

// flatMap flattens to Stream<Integer>
nested.stream().flatMap(List::stream)   // Stream<Integer> → [1,2,3,4]
```

```java
// Real-world: split sentences into words
List<String> sentences = List.of("hello world", "foo bar");

List<String> words = sentences.stream()
    .flatMap(s -> Arrays.stream(s.split(" ")))
    .collect(Collectors.toList());
// ["hello", "world", "foo", "bar"]
```

---

## Terminal Operations (eager, consume the stream)

```java
// collect
stream.collect(Collectors.toList())
stream.collect(Collectors.toSet())
stream.collect(Collectors.joining(", "))

// forEach
stream.forEach(System.out::println)
stream.forEachOrdered(System.out::println)   // preserves order for parallel

// reduce
stream.reduce(0, Integer::sum)              // with identity → T
stream.reduce(Integer::max)                 // no identity → Optional<T>

// count, min, max
stream.count()
stream.min(Comparator.naturalOrder())       // Optional<T>
stream.max(Comparator.comparing(Person::getAge))

// match (short-circuit)
stream.anyMatch(n -> n < 0)   // true if ANY match
stream.allMatch(n -> n > 0)   // true if ALL match
stream.noneMatch(n -> n < 0)  // true if NONE match

// find (short-circuit)
stream.findFirst()   // Optional<T> — first element
stream.findAny()     // Optional<T> — any element (faster for parallel)

// toArray
stream.toArray(String[]::new)
```

---

## Primitive Streams — Use These for Performance

```java
// Boxing overhead avoided
int sum = numbers.stream()
    .mapToInt(Integer::intValue)   // Stream<Integer> → IntStream
    .sum();

// IntStream / LongStream / DoubleStream have shorthand terminals
IntStream.range(1, 6).sum()          // 15
IntStream.range(1, 6).average()      // OptionalDouble
IntStream.range(1, 6).max()          // OptionalInt
IntStream.range(1, 6).summaryStatistics()  // count, sum, min, max, avg

// Convert back to object stream
IntStream.range(1, 6).boxed()        // Stream<Integer>
IntStream.range(1, 6).mapToObj(i -> "item-" + i)
```

---

## `reduce` in Depth

```java
// With identity (always returns T)
int sum = numbers.stream().reduce(0, (a, b) -> a + b);
int sum = numbers.stream().reduce(0, Integer::sum);

// Without identity (returns Optional — stream might be empty)
Optional<Integer> max = numbers.stream().reduce(Integer::max);

// Three-arg reduce (for parallel / type change)
// (identity, accumulator, combiner)
int sumOfLengths = words.stream().reduce(
    0,
    (acc, word) -> acc + word.length(),
    Integer::sum                          // combiner for parallel
);
```

---

## Parallel Streams

```java
// Use when: large dataset, CPU-intensive, stateless, order-independent
list.parallelStream()
    .filter(this::isExpensive)
    .collect(Collectors.toList());

// Custom thread pool (default uses common ForkJoinPool)
ForkJoinPool pool = new ForkJoinPool(4);
pool.submit(() ->
    list.parallelStream().forEach(this::process)
).get();
```

**Do NOT use parallel when:**
- Dataset is small (overhead exceeds benefit)
- Operations are I/O-bound
- Operations are stateful (e.g. modifying shared list)
- Order matters and you can't use `forEachOrdered`

---

## Lazy Evaluation in Practice

```java
// Only processes elements until the first match
Optional<String> first = hugeList.stream()
    .filter(s -> s.startsWith("A"))
    .map(String::toUpperCase)
    .findFirst();      // stops after first match found — O(n) worst case, O(1) best

// Without findFirst, ALL elements would be processed
```

---

## Common Patterns

```java
// Group by field
Map<String, List<Order>> byStatus = orders.stream()
    .collect(Collectors.groupingBy(Order::getStatus));

// Sum a field
double total = orders.stream()
    .mapToDouble(Order::getAmount)
    .sum();

// Top N
List<Employee> top3 = employees.stream()
    .sorted(Comparator.comparing(Employee::getSalary).reversed())
    .limit(3)
    .collect(Collectors.toList());

// Flat-collect all tags
Set<String> allTags = orders.stream()
    .flatMap(o -> o.getTags().stream())
    .collect(Collectors.toSet());

// Partition into two groups
Map<Boolean, List<Order>> partitioned = orders.stream()
    .collect(Collectors.partitioningBy(o -> "PAID".equals(o.getStatus())));
```

---

## Common Interview Questions

| Question | Key point |
|----------|-----------|
| What is lazy evaluation? | Intermediate ops build a pipeline; nothing runs until a terminal op is called. |
| `map` vs `flatMap`? | `map` is 1:1. `flatMap` flattens — use when each element maps to multiple elements. |
| When to use parallel streams? | Large, CPU-bound, stateless operations. Not for small data or I/O. |
| Can you reuse a stream? | No — `IllegalStateException` if consumed twice. |
| `findFirst` vs `findAny`? | `findAny` may return any element; faster for parallel. `findFirst` respects encounter order. |
| `reduce` vs `collect`? | Use `collect` for mutable containers (lists, maps). `reduce` for immutable combination. |

---

## Gotchas

```java
// ❌ Stream already consumed
Stream<String> s = list.stream();
s.forEach(System.out::println);
s.count();   // IllegalStateException: stream has already been operated upon

// ❌ Infinite stream without limit → hangs forever
Stream.iterate(0, n -> n + 1).forEach(System.out::println);

// ✅
Stream.iterate(0, n -> n + 1).limit(100).forEach(System.out::println);

// ❌ Side effects in parallel stream — race condition
List<String> result = new ArrayList<>();
list.parallelStream().forEach(result::add);  // NOT thread-safe!

// ✅
List<String> result = list.parallelStream().collect(Collectors.toList());

// ❌ Forgetting to close file stream
Stream<String> lines = Files.lines(path);
lines.forEach(System.out::println);
// resource leak!

// ✅
try (Stream<String> lines = Files.lines(path)) {
    lines.forEach(System.out::println);
}
```
