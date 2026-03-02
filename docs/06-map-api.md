# 06 · Map API (Java 8 New Methods)

> **When to read:** After attempting Map API katas, before checking solutions.

---

## Core Idea

Java 8 added a powerful set of methods to `Map` that eliminate the verbose null-check boilerplate that plagued pre-Java-8 code. These are among the most commonly tested Java 8 interview topics.

---

## `getOrDefault`

```java
// Before Java 8
Integer score = map.get("Alice");
if (score == null) score = 0;

// Java 8
int score = map.getOrDefault("Alice", 0);
```

> Does **not** modify the map. Just returns a fallback if the key is absent.

---

## `putIfAbsent`

```java
// Insert only if key is not present (or mapped to null)
map.putIfAbsent("timeout", "30s");    // inserts
map.putIfAbsent("timeout", "60s");    // does nothing — key already exists

// Returns: the existing value, or null if key was absent (and insertion happened)
String prev = map.putIfAbsent("host", "localhost");
```

**vs `put`:** `put` always overwrites. `putIfAbsent` preserves existing values.

---

## `forEach`

```java
// Before Java 8
for (Map.Entry<String, Integer> e : map.entrySet()) {
    System.out.println(e.getKey() + " = " + e.getValue());
}

// Java 8
map.forEach((key, value) -> System.out.println(key + " = " + value));
```

Takes a `BiConsumer<K, V>`. Order is guaranteed only for `LinkedHashMap` / `TreeMap`.

---

## `replaceAll`

```java
// Transform all values in place — no new map created
map.replaceAll((key, value) -> value.toUpperCase());

// Can use key in the transformation
map.replaceAll((product, price) -> price * 0.9);   // 10% discount on all

// Before Java 8 equivalent
for (Map.Entry<String, Double> e : map.entrySet()) {
    e.setValue(e.getValue() * 0.9);
}
```

---

## `computeIfAbsent`

The most useful of the compute family. Inserts a value only if the key is absent, and returns the (new or existing) value — perfect for **lazy initialisation**.

```java
// Classic grouping pattern — Java 8 idiomatic
Map<String, List<String>> groups = new HashMap<>();

for (String item : items) {
    groups.computeIfAbsent(item.getCategory(), k -> new ArrayList<>())
          .add(item.getName());
}

// Before Java 8 — the old verbose way
if (!groups.containsKey(category)) {
    groups.put(category, new ArrayList<>());
}
groups.get(category).add(name);
```

Also used for **cache-aside / memoization**:

```java
Map<Integer, Long> cache = new HashMap<>();
cache.put(0, 0L);
cache.put(1, 1L);

for (int i = 2; i <= 50; i++) {
    cache.computeIfAbsent(i, k -> cache.get(k - 1) + cache.get(k - 2));
}
```

---

## `computeIfPresent`

Updates a value only if the key already exists. Returning `null` from the function **removes** the key.

```java
// Double the score only if Alice is in the map
map.computeIfPresent("Alice", (k, v) -> v * 2);

// Remove Alice if she's in the map
map.computeIfPresent("Alice", (k, v) -> null);   // removes the key
```

---

## `compute`

Runs unconditionally — regardless of whether the key is present. Value `v` is `null` when key is absent.

```java
// Page hit counter
map.compute(page, (k, v) -> v == null ? 1 : v + 1);
```

Prefer `merge` for simple accumulation — it's cleaner (see below).

---

## `merge` ← Most Important

**The most useful method.** Behaviour:
- Key **absent** → insert `(key, value)`
- Key **present** → apply `BiFunction(oldValue, newValue)`
- BiFunction returns `null` → removes the key

```java
// Word frequency counter — THE idiomatic Java 8 pattern
map.merge(word, 1, Integer::sum);
//                ↑     ↑
//          new value  combiner when key exists

// Merge two sales maps
salesQ2.forEach((name, amount) ->
    salesQ1.merge(name, amount, Integer::sum)
);

// String concatenation
map.merge("log", newEntry, (old, next) -> old + "\n" + next);

// Remove if result is zero
map.merge("stock", -qty, (old, delta) -> {
    int newVal = old + delta;
    return newVal <= 0 ? null : newVal;   // null = remove key
});
```

---

## Comparison: `compute` vs `merge` vs `computeIfAbsent`

| Method | Runs when | Null v means | Best for |
|--------|-----------|--------------|----------|
| `computeIfAbsent` | Key absent | — | Lazy init, grouping |
| `computeIfPresent` | Key present | — | Update existing |
| `compute` | Always | Key was absent | Custom counter |
| `merge` | Always | Key was absent | Accumulate / combine |

---

## Stream Operations on Maps

```java
// Iterate entries as a stream
map.entrySet().stream()
   .filter(e -> e.getValue() > 100)
   .sorted(Map.Entry.comparingByKey())
   .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));

// Sort by value
map.entrySet().stream()
   .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
   .map(Map.Entry::getKey)
   .collect(Collectors.toList());

// Invert map: value → key
Map<Integer, String> inverted = original.entrySet().stream()
    .collect(Collectors.toMap(
        Map.Entry::getValue,
        Map.Entry::getKey
    ));

// Filter to new map
Map<String, Integer> highScorers = scores.entrySet().stream()
    .filter(e -> e.getValue() >= 90)
    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
```

---

## Common Patterns

```java
// Frequency map (two approaches)

// 1. merge
words.forEach(w -> freq.merge(w, 1, Integer::sum));

// 2. groupingBy + counting (streams)
Map<String, Long> freq = words.stream()
    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

// Building an index
Map<String, User> byEmail = users.stream()
    .collect(Collectors.toMap(User::getEmail, Function.identity()));

// Aggregate per group
Map<String, Double> totalByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDept,
        Collectors.summingDouble(Employee::getSalary)
    ));
```

---

## Common Interview Questions

| Question | Key point |
|----------|-----------|
| `getOrDefault` vs `computeIfAbsent`? | `getOrDefault` only reads, never inserts. `computeIfAbsent` inserts and returns the value. |
| `putIfAbsent` vs `computeIfAbsent`? | `putIfAbsent` takes a pre-computed value (always evaluated). `computeIfAbsent` is lazy. |
| When to use `merge`? | Any "upsert with accumulation" — counters, combining maps, concatenation. |
| `compute` vs `merge`? | `merge` is cleaner for accumulation. `compute` when you need conditional logic on both key and value. |
| How to remove a key in `merge`/`compute`? | Return `null` from the function — the key is removed. |

---

## Gotchas

```java
// ❌ putIfAbsent evaluates the value eagerly — even if key exists
map.putIfAbsent("key", expensiveOperation());   // expensiveOperation() ALWAYS runs

// ✅ computeIfAbsent is lazy
map.computeIfAbsent("key", k -> expensiveOperation());  // only runs if absent

// ❌ Recursive computeIfAbsent in Java 8 causes ConcurrentModificationException
Map<Integer, Long> fib = new HashMap<>();
fib.computeIfAbsent(5, k -> fib.computeIfAbsent(k-1, ...) + ...);  // BOOM

// ✅ Use iterative approach for memoization in Java 8
for (int i = 2; i <= n; i++) {
    fib.computeIfAbsent(i, k -> fib.get(k-1) + fib.get(k-2));
}

// ❌ Inverting a map with duplicate values
Map<Integer, String> inverted = map.entrySet().stream()
    .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
// IllegalStateException if two keys map to the same value!

// ✅ Handle with merge function
Map<Integer, String> inverted = map.entrySet().stream()
    .collect(Collectors.toMap(
        Map.Entry::getValue,
        Map.Entry::getKey,
        (k1, k2) -> k1   // keep first on collision
    ));
```
