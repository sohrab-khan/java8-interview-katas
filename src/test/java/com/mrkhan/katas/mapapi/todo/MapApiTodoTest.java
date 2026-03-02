package com.mrkhan.katas.mapapi.todo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static org.assertj.core.api.Assertions.*;

/**
 * 🎯 MAP API KATAS (15 katas)
 * ───────────────────────────
 * Master Java 8 Map new methods: getOrDefault, forEach, compute,
 * computeIfAbsent, computeIfPresent, merge, replaceAll, putIfAbsent.
 * Run: ./mvnw test -Dtest="MapApiTodoTest"
 */
@DisplayName("🚨 Map API (Java 8) - TODO (Fix me!)")
public class MapApiTodoTest {

    // ─── getOrDefault ────────────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 1: getOrDefault - safe lookup")
    public void getOrDefault() {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Alice", 95);
        scores.put("Bob", 82);

        // 🚨 TO DO: Get Alice's score, and "Charlie"'s score with default 0
        int aliceScore   = 0; // ❌ BROKEN - scores.getOrDefault("Alice", 0)
        int charlieScore = 0; // ❌ BROKEN - scores.getOrDefault("Charlie", 0)

        assertThat(aliceScore).isEqualTo(95);
        assertThat(charlieScore).isEqualTo(0);

        // 💡 HINTS:
        // scores.getOrDefault("Alice", 0)
        // scores.getOrDefault("Charlie", 0)
    }

    // ─── putIfAbsent ─────────────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 2: putIfAbsent - don't overwrite existing")
    public void putIfAbsent() {
        Map<String, String> config = new HashMap<>();
        config.put("host", "localhost");

        // 🚨 TO DO: Add "port"="8080" only if absent; try to add "host"="production" - should not change it
        config.putIfAbsent("port", null); // ❌ BROKEN - putIfAbsent("port", "8080")
        config.putIfAbsent("host", null); // ❌ BROKEN - putIfAbsent("host", "production")

        assertThat(config.get("port")).isEqualTo("8080");
        assertThat(config.get("host")).isEqualTo("localhost"); // unchanged

        // 💡 HINTS:
        // config.putIfAbsent("port", "8080")
        // config.putIfAbsent("host", "production")  // won't overwrite
    }

    // ─── forEach ─────────────────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 3: Map.forEach with BiConsumer")
    public void mapForEach() {
        Map<String, Integer> inventory = new LinkedHashMap<>();
        inventory.put("Apple", 50);
        inventory.put("Banana", 30);
        inventory.put("Cherry", 20);

        List<String> report = new ArrayList<>();

        // 🚨 TO DO: Use forEach to add "Apple: 50", "Banana: 30", "Cherry: 20" to report
        // ❌ BROKEN - inventory.forEach((item, qty) -> report.add(item + ": " + qty))

        assertThat(report).containsExactly("Apple: 50", "Banana: 30", "Cherry: 20");

        // 💡 HINTS:
        // inventory.forEach((item, qty) -> report.add(item + ": " + qty))
    }

    // ─── replaceAll ───────────────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 4: replaceAll - transform all values in place")
    public void replaceAll() {
        Map<String, String> names = new HashMap<>();
        names.put("first", "alice");
        names.put("last", "smith");

        // 🚨 TO DO: Uppercase all values in place using replaceAll
        // ❌ BROKEN - names.replaceAll((key, value) -> value.toUpperCase())

        assertThat(names.get("first")).isEqualTo("ALICE");
        assertThat(names.get("last")).isEqualTo("SMITH");

        // 💡 HINTS:
        // names.replaceAll((key, value) -> value.toUpperCase())
    }

    // ─── computeIfAbsent ─────────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 5: computeIfAbsent - lazy initialization pattern")
    public void computeIfAbsent() {
        // Classic use case: grouping into lists
        Map<String, List<String>> grouped = new HashMap<>();

        List<String[]> data = Arrays.asList(
                new String[]{"fruits", "Apple"},
                new String[]{"fruits", "Banana"},
                new String[]{"vegs",   "Carrot"},
                new String[]{"fruits", "Cherry"},
                new String[]{"vegs",   "Daikon"}
        );

        // 🚨 TO DO: Use computeIfAbsent to group items into lists by category
        for (String[] entry : data) {
            // ❌ BROKEN - grouped.computeIfAbsent(entry[0], k -> new ArrayList<>()).add(entry[1])
        }

        assertThat(grouped.get("fruits")).containsExactly("Apple", "Banana", "Cherry");
        assertThat(grouped.get("vegs")).containsExactly("Carrot", "Daikon");

        // 💡 HINTS:
        // grouped.computeIfAbsent(entry[0], k -> new ArrayList<>()).add(entry[1])
        // This is MORE EFFICIENT than: if (!map.containsKey(k)) map.put(k, new ArrayList<>())
    }

    // ─── computeIfPresent ────────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 6: computeIfPresent - update only if key exists")
    public void computeIfPresent() {
        Map<String, Integer> wordCount = new HashMap<>();
        wordCount.put("java", 3);
        wordCount.put("stream", 2);

        // 🚨 TO DO: Double the count for "java" (key exists), try to update "lambda" (key absent - no change)
        wordCount.computeIfPresent("java", null);   // ❌ BROKEN - (k, v) -> v * 2
        wordCount.computeIfPresent("lambda", null); // ❌ BROKEN - (k, v) -> v * 2 (won't run)

        assertThat(wordCount.get("java")).isEqualTo(6);
        assertThat(wordCount.containsKey("lambda")).isFalse();

        // 💡 HINTS:
        // wordCount.computeIfPresent("java", (k, v) -> v * 2)
    }

    // ─── compute ─────────────────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 7: compute - always compute new value")
    public void compute() {
        Map<String, Integer> hits = new HashMap<>();

        // 🚨 TO DO: Count page hits - use compute to increment (or start at 1 if absent)
        List<String> pages = Arrays.asList("/home", "/about", "/home", "/home", "/about");
        for (String page : pages) {
            // ❌ BROKEN - hits.compute(page, (k, v) -> v == null ? 1 : v + 1)
        }

        assertThat(hits.get("/home")).isEqualTo(3);
        assertThat(hits.get("/about")).isEqualTo(2);

        // 💡 HINTS:
        // hits.compute(page, (k, v) -> v == null ? 1 : v + 1)
        // Cleaner alternative: hits.merge(page, 1, Integer::sum)
    }

    // ─── merge ───────────────────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 8: merge - combine or insert")
    public void merge() {
        Map<String, Integer> salesQ1 = new HashMap<>();
        salesQ1.put("Alice", 1000);
        salesQ1.put("Bob", 800);

        Map<String, Integer> salesQ2 = new HashMap<>();
        salesQ2.put("Alice", 1200);
        salesQ2.put("Charlie", 950);

        // 🚨 TO DO: Merge Q2 into Q1, summing values for duplicate keys
        salesQ2.forEach((name, amount) -> {
            // ❌ BROKEN - salesQ1.merge(name, amount, Integer::sum)
        });

        assertThat(salesQ1.get("Alice")).isEqualTo(2200);   // 1000 + 1200
        assertThat(salesQ1.get("Bob")).isEqualTo(800);      // unchanged
        assertThat(salesQ1.get("Charlie")).isEqualTo(950);  // new entry

        // 💡 HINTS:
        // salesQ1.merge(name, amount, Integer::sum)
        // merge: if absent → put; if present → apply BiFunction(oldVal, newVal)
    }

    @Test
    @DisplayName("🚨 TODO 9: merge for word frequency count")
    public void mergeWordCount() {
        String text = "to be or not to be that is the question to";
        Map<String, Integer> freq = new HashMap<>();

        // 🚨 TO DO: Count word frequency using merge
        Arrays.stream(text.split(" ")).forEach(word -> {
            // ❌ BROKEN - freq.merge(word, 1, Integer::sum)
        });

        assertThat(freq.get("to")).isEqualTo(3);
        assertThat(freq.get("be")).isEqualTo(2);
        assertThat(freq.get("question")).isEqualTo(1);

        // 💡 HINTS:
        // freq.merge(word, 1, Integer::sum)
        // This is the idiomatic Java 8 word count pattern
    }

    // ─── Map.Entry iteration ─────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 10: Iterate entries with stream and Map.Entry")
    public void iterateEntries() {
        Map<String, Integer> prices = new LinkedHashMap<>();
        prices.put("Coffee", 3);
        prices.put("Tea", 2);
        prices.put("Juice", 4);

        // 🚨 TO DO: Find all items with price > 2, return as "item=$price" strings sorted
        List<String> expensive = null; // ❌ BROKEN

        assertThat(expensive).containsExactly("Coffee=$3", "Juice=$4");

        // 💡 HINTS:
        // prices.entrySet().stream()
        //     .filter(e -> e.getValue() > 2)
        //     .sorted(Map.Entry.comparingByKey())
        //     .map(e -> e.getKey() + "=$" + e.getValue())
        //     .collect(Collectors.toList())
    }

    @Test
    @DisplayName("🚨 TODO 11: Sort map by value using stream")
    public void sortMapByValue() {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Alice", 85);
        scores.put("Bob", 92);
        scores.put("Charlie", 78);
        scores.put("Diana", 95);

        // 🚨 TO DO: Get names sorted by score descending
        List<String> ranked = null; // ❌ BROKEN

        assertThat(ranked).containsExactly("Diana", "Bob", "Alice", "Charlie");

        // 💡 HINTS:
        // scores.entrySet().stream()
        //     .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
        //     .map(Map.Entry::getKey)
        //     .collect(Collectors.toList())
    }

    // ─── Combining Map APIs ───────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 12: Build frequency map with computeIfAbsent + grouping")
    public void buildFrequencyMap() {
        List<String> orders = Arrays.asList(
                "Electronics", "Books", "Electronics", "Clothing",
                "Books", "Electronics", "Books"
        );

        // 🚨 TO DO: Count occurrences of each category using merge
        Map<String, Long> freq = null; // ❌ BROKEN

        assertThat(freq.get("Electronics")).isEqualTo(3L);
        assertThat(freq.get("Books")).isEqualTo(3L);
        assertThat(freq.get("Clothing")).isEqualTo(1L);

        // 💡 HINTS:
        // orders.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        // OR: Map<String, Long> with forEach + merge(cat, 1L, Long::sum)
    }

    @Test
    @DisplayName("🚨 TODO 13: Invert a map (value → key)")
    public void invertMap() {
        Map<String, Integer> nameToId = new HashMap<>();
        nameToId.put("Alice", 101);
        nameToId.put("Bob",   102);
        nameToId.put("Charlie", 103);

        // 🚨 TO DO: Invert to Map<Integer, String>: id → name
        Map<Integer, String> idToName = null; // ❌ BROKEN

        assertThat(idToName.get(101)).isEqualTo("Alice");
        assertThat(idToName.get(102)).isEqualTo("Bob");
        assertThat(idToName.get(103)).isEqualTo("Charlie");

        // 💡 HINTS:
        // nameToId.entrySet().stream()
        //     .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey))
    }

    @Test
    @DisplayName("🚨 TODO 14: computeIfAbsent for memoization / cache")
    public void computeIfAbsentMemoization() {
        Map<Integer, Long> cache = new HashMap<>();

        // Simple fibonacci with memoization using computeIfAbsent (iterative)
        // Pre-fill base cases
        cache.put(0, 0L);
        cache.put(1, 1L);

        // 🚨 TO DO: Fill cache for fib(2) through fib(10) using a loop
        // Use cache.computeIfAbsent(n, k -> cache.get(k-1) + cache.get(k-2))
        for (int i = 2; i <= 10; i++) {
            // ❌ BROKEN - cache.computeIfAbsent(i, k -> cache.get(k - 1) + cache.get(k - 2))
        }

        assertThat(cache.get(10)).isEqualTo(55L);
        assertThat(cache.get(7)).isEqualTo(13L);

        // 💡 HINTS:
        // cache.computeIfAbsent(i, k -> cache.get(k - 1) + cache.get(k - 2))
        // computeIfAbsent: only computes if key is missing - perfect for cache-aside pattern
    }

    @Test
    @DisplayName("🚨 TODO 15: replaceAll to apply discount pricing")
    public void replaceAllDiscount() {
        Map<String, Double> prices = new HashMap<>();
        prices.put("Laptop",  1200.0);
        prices.put("Phone",    800.0);
        prices.put("Tablet",   450.0);

        double discountRate = 0.10;

        // 🚨 TO DO: Apply 10% discount to ALL prices using replaceAll
        // ❌ BROKEN - prices.replaceAll((product, price) -> price * (1 - discountRate))

        assertThat(prices.get("Laptop")).isEqualTo(1080.0);
        assertThat(prices.get("Phone")).isEqualTo(720.0);
        assertThat(prices.get("Tablet")).isEqualTo(405.0);

        // 💡 HINTS:
        // prices.replaceAll((product, price) -> price * (1 - discountRate))
        // replaceAll mutates the map in place - more efficient than building a new map
    }
}
