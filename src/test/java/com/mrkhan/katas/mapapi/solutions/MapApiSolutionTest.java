package com.mrkhan.katas.mapapi.solutions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static org.assertj.core.api.Assertions.*;

/**
 * ✅ MAP API (JAVA 8) - SOLUTIONS
 * Study AFTER attempting the todo tests.
 */
@DisplayName("✅ Map API (Java 8) - Solutions")
public class MapApiSolutionTest {

    @Test @DisplayName("✅ SOLUTION 1: getOrDefault")
    public void getOrDefault_solution() {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Alice", 95);
        scores.put("Bob", 82);

        int aliceScore   = scores.getOrDefault("Alice", 0);
        int charlieScore = scores.getOrDefault("Charlie", 0);

        assertThat(aliceScore).isEqualTo(95);
        assertThat(charlieScore).isEqualTo(0);
        // 💡 getOrDefault replaces: map.containsKey(k) ? map.get(k) : default
        // 💡 Does NOT modify the map - use computeIfAbsent if you want to also insert
    }

    @Test @DisplayName("✅ SOLUTION 2: putIfAbsent")
    public void putIfAbsent_solution() {
        Map<String, String> config = new HashMap<>();
        config.put("host", "localhost");

        config.putIfAbsent("port", "8080");
        config.putIfAbsent("host", "production");

        assertThat(config.get("port")).isEqualTo("8080");
        assertThat(config.get("host")).isEqualTo("localhost");
        // 💡 putIfAbsent: inserts only if key is not present (or mapped to null)
        // 💡 Returns the OLD value (or null if absent) - useful for detecting first insert
    }

    @Test @DisplayName("✅ SOLUTION 3: Map.forEach")
    public void mapForEach_solution() {
        Map<String, Integer> inventory = new LinkedHashMap<>();
        inventory.put("Apple", 50);
        inventory.put("Banana", 30);
        inventory.put("Cherry", 20);

        List<String> report = new ArrayList<>();
        inventory.forEach((item, qty) -> report.add(item + ": " + qty));

        assertThat(report).containsExactly("Apple: 50", "Banana: 30", "Cherry: 20");
        // 💡 Map.forEach(BiConsumer) - cleaner than for (Map.Entry<K,V> e : map.entrySet())
        // 💡 Order guaranteed only for LinkedHashMap/TreeMap; HashMap is unordered
    }

    @Test @DisplayName("✅ SOLUTION 4: replaceAll")
    public void replaceAll_solution() {
        Map<String, String> names = new HashMap<>();
        names.put("first", "alice");
        names.put("last", "smith");

        names.replaceAll((key, value) -> value.toUpperCase());

        assertThat(names.get("first")).isEqualTo("ALICE");
        assertThat(names.get("last")).isEqualTo("SMITH");
        // 💡 replaceAll(BiFunction<K,V,V>) mutates the map - no new map created
        // 💡 Can use key in computation: e.g. replaceAll((k, v) -> k + "=" + v)
    }

    @Test @DisplayName("✅ SOLUTION 5: computeIfAbsent - lazy initialization")
    public void computeIfAbsent_solution() {
        Map<String, List<String>> grouped = new HashMap<>();

        List<String[]> data = Arrays.asList(
                new String[]{"fruits", "Apple"},  new String[]{"fruits", "Banana"},
                new String[]{"vegs",   "Carrot"}, new String[]{"fruits", "Cherry"},
                new String[]{"vegs",   "Daikon"}
        );

        for (String[] entry : data) {
            grouped.computeIfAbsent(entry[0], k -> new ArrayList<>()).add(entry[1]);
        }

        assertThat(grouped.get("fruits")).containsExactly("Apple", "Banana", "Cherry");
        assertThat(grouped.get("vegs")).containsExactly("Carrot", "Daikon");
        // 💡 computeIfAbsent: only creates list if key absent; returns existing or new list
        // 💡 This replaces the common anti-pattern:
        //    if (!map.containsKey(k)) { map.put(k, new ArrayList<>()); }
        //    map.get(k).add(v);
    }

    @Test @DisplayName("✅ SOLUTION 6: computeIfPresent")
    public void computeIfPresent_solution() {
        Map<String, Integer> wordCount = new HashMap<>();
        wordCount.put("java", 3);
        wordCount.put("stream", 2);

        wordCount.computeIfPresent("java",   (k, v) -> v * 2);
        wordCount.computeIfPresent("lambda", (k, v) -> v * 2); // no-op

        assertThat(wordCount.get("java")).isEqualTo(6);
        assertThat(wordCount.containsKey("lambda")).isFalse();
        // 💡 computeIfPresent: only runs if key exists; returning null removes the key
        // 💡 Contrast: computeIfAbsent runs only when ABSENT, computeIfPresent only when PRESENT
    }

    @Test @DisplayName("✅ SOLUTION 7: compute - always compute")
    public void compute_solution() {
        Map<String, Integer> hits = new HashMap<>();

        List<String> pages = Arrays.asList("/home", "/about", "/home", "/home", "/about");
        for (String page : pages) {
            hits.compute(page, (k, v) -> v == null ? 1 : v + 1);
        }

        assertThat(hits.get("/home")).isEqualTo(3);
        assertThat(hits.get("/about")).isEqualTo(2);
        // 💡 compute runs unconditionally; v is null if key was absent
        // 💡 Returning null from the function REMOVES the key
        // 💡 For simple increment, prefer: hits.merge(page, 1, Integer::sum) — cleaner!
    }

    @Test @DisplayName("✅ SOLUTION 8: merge - combine Q1 and Q2 sales")
    public void merge_solution() {
        Map<String, Integer> salesQ1 = new HashMap<>();
        salesQ1.put("Alice", 1000);
        salesQ1.put("Bob", 800);

        Map<String, Integer> salesQ2 = new HashMap<>();
        salesQ2.put("Alice", 1200);
        salesQ2.put("Charlie", 950);

        salesQ2.forEach((name, amount) -> salesQ1.merge(name, amount, Integer::sum));

        assertThat(salesQ1.get("Alice")).isEqualTo(2200);
        assertThat(salesQ1.get("Bob")).isEqualTo(800);
        assertThat(salesQ1.get("Charlie")).isEqualTo(950);
        // 💡 merge(key, value, BiFunction): if absent → put(key, value); if present → apply BiFunction(old, new)
        // 💡 Most concise way to "upsert with accumulation" in Java 8
    }

    @Test @DisplayName("✅ SOLUTION 9: merge for word frequency")
    public void mergeWordCount_solution() {
        String text = "to be or not to be that is the question to";
        Map<String, Integer> freq = new HashMap<>();

        Arrays.stream(text.split(" "))
                .forEach(word -> freq.merge(word, 1, Integer::sum));

        assertThat(freq.get("to")).isEqualTo(3);
        assertThat(freq.get("be")).isEqualTo(2);
        assertThat(freq.get("question")).isEqualTo(1);
        // 💡 merge(word, 1, Integer::sum) is THE idiomatic word-count pattern in Java 8
        // 💡 Equivalent with streams: Collectors.groupingBy(w -> w, Collectors.counting())
    }

    @Test @DisplayName("✅ SOLUTION 10: Iterate entries with stream")
    public void iterateEntries_solution() {
        Map<String, Integer> prices = new LinkedHashMap<>();
        prices.put("Coffee", 3);
        prices.put("Tea", 2);
        prices.put("Juice", 4);

        List<String> expensive = prices.entrySet().stream()
                .filter(e -> e.getValue() > 2)
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=$" + e.getValue())
                .collect(Collectors.toList());

        assertThat(expensive).containsExactly("Coffee=$3", "Juice=$4");
        // 💡 Map.Entry.comparingByKey() / comparingByValue() - static factory Comparators
        // 💡 entrySet().stream() is the entry point for all stream-based map operations
    }

    @Test @DisplayName("✅ SOLUTION 11: Sort map by value")
    public void sortMapByValue_solution() {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Alice", 85);
        scores.put("Bob", 92);
        scores.put("Charlie", 78);
        scores.put("Diana", 95);

        List<String> ranked = scores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        assertThat(ranked).containsExactly("Diana", "Bob", "Alice", "Charlie");
        // 💡 Maps have no natural order by value - stream + sort is the only clean way
        // 💡 Note the explicit type parameter on comparingByValue() - required for .reversed()
    }

    @Test @DisplayName("✅ SOLUTION 12: Frequency map with groupingBy")
    public void buildFrequencyMap_solution() {
        List<String> orders = Arrays.asList(
                "Electronics", "Books", "Electronics", "Clothing",
                "Books", "Electronics", "Books"
        );

        Map<String, Long> freq = orders.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        assertThat(freq.get("Electronics")).isEqualTo(3L);
        assertThat(freq.get("Books")).isEqualTo(3L);
        assertThat(freq.get("Clothing")).isEqualTo(1L);
        // 💡 Function.identity() returns the element itself as the key
        // 💡 This is the canonical streams frequency map pattern - cleaner than merge loop
    }

    @Test @DisplayName("✅ SOLUTION 13: Invert a map")
    public void invertMap_solution() {
        Map<String, Integer> nameToId = new HashMap<>();
        nameToId.put("Alice", 101);
        nameToId.put("Bob",   102);
        nameToId.put("Charlie", 103);

        Map<Integer, String> idToName = nameToId.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        assertThat(idToName.get(101)).isEqualTo("Alice");
        assertThat(idToName.get(102)).isEqualTo("Bob");
        assertThat(idToName.get(103)).isEqualTo("Charlie");
        // 💡 Only works if values are unique - duplicate values cause IllegalStateException
        // 💡 Add merge function as 3rd arg to handle duplicates: (v1, v2) -> v1
    }

    @Test @DisplayName("✅ SOLUTION 14: computeIfAbsent memoization")
    public void computeIfAbsentMemoization_solution() {
        Map<Integer, Long> cache = new HashMap<>();
        cache.put(0, 0L);
        cache.put(1, 1L);

        for (int i = 2; i <= 10; i++) {
            cache.computeIfAbsent(i, k -> cache.get(k - 1) + cache.get(k - 2));
        }

        assertThat(cache.get(10)).isEqualTo(55L);
        assertThat(cache.get(7)).isEqualTo(13L);
        // 💡 computeIfAbsent is the building block of cache-aside pattern
        // 💡 Note: recursive computeIfAbsent for fib causes ConcurrentModificationException in Java 8
        //    Use iterative (as above) or explicit recursion with cache.get fallback
    }

    @Test @DisplayName("✅ SOLUTION 15: replaceAll for bulk discount")
    public void replaceAllDiscount_solution() {
        Map<String, Double> prices = new HashMap<>();
        prices.put("Laptop",  1200.0);
        prices.put("Phone",    800.0);
        prices.put("Tablet",   450.0);

        double discountRate = 0.10;
        prices.replaceAll((product, price) -> price * (1 - discountRate));

        assertThat(prices.get("Laptop")).isEqualTo(1080.0);
        assertThat(prices.get("Phone")).isEqualTo(720.0);
        assertThat(prices.get("Tablet")).isEqualTo(405.0);
        // 💡 replaceAll mutates the existing map (no new map allocated)
        // 💡 Can capture effectively-final variables from outer scope (discountRate here)
    }
}
