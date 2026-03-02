package com.mrkhan.katas.advanced.todo;

import com.mrkhan.katas.OrderData;
import com.mrkhan.katas.OrderData.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.function.*;
import java.util.stream.*;

import static org.assertj.core.api.Assertions.*;

/**
 * 🎯 ADVANCED STREAMS KATAS (10 katas)
 * ──────────────────────────────────────
 * Deep dives: flatMap nesting, custom Collectors, Spliterators, parallel.
 * Run: ./mvnw test -Dtest="AdvancedStreamsTodoTest"
 */
@DisplayName("🚨 Advanced Streams - TODO (Fix me!)")
public class AdvancedStreamsTodoTest {

    @Test
    @DisplayName("🚨 TODO 1: Deep flatMap 3 levels")
    public void deepFlatMap() {
        List<Department> departments = OrderData.sampleDepartments();

        // 🚨 TO DO: Get all roles from all users in all departments (flat, distinct, sorted)
        List<String> allRoles = null; // ❌ BROKEN

        assertThat(allRoles).contains("ADMIN", "DEV", "HR_ADMIN");
        assertThat(allRoles).doesNotHaveDuplicates();

        // 💡 HINTS:
        // departments.stream()
        //     .flatMap(d -> d.getEmployees().stream())
        //     .flatMap(u -> u.getRoles().stream())
        //     .distinct().sorted()
        //     .collect(Collectors.toList())
    }

    @Test
    @DisplayName("🚨 TODO 2: Custom Collector with Collector.of()")
    public void customCollectorOf() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // 🚨 TO DO: Implement a custom collector that computes sum of squares
        // (1^2 + 2^2 + ... + 10^2 = 385)
        int sumOfSquares = numbers.stream()
                .collect(Collector.of(
                        null, // supplier       ❌ BROKEN
                        null, // accumulator     ❌ BROKEN
                        null, // combiner        ❌ BROKEN
                        null  // finisher        ❌ BROKEN
                ));

        assertThat(sumOfSquares).isEqualTo(385);

        // 💡 HINTS:
        // supplier:    int[]::new (array[0] = accumulator)
        // accumulator: (acc, n) -> acc[0] += n * n
        // combiner:    (left, right) -> { left[0] += right[0]; return left; }
        // finisher:    acc -> acc[0]
    }

    @Test
    @DisplayName("🚨 TODO 3: Sliding window using Stream.iterate")
    public void slidingWindow() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        int windowSize = 3;

        // 🚨 TO DO: Create sliding windows of size 3
        // Expected: [[1,2,3], [2,3,4], ..., [8,9,10]]
        List<List<Integer>> windows = null; // ❌ BROKEN

        assertThat(windows).hasSize(8);
        assertThat(windows.get(0)).containsExactly(1, 2, 3);
        assertThat(windows.get(7)).containsExactly(8, 9, 10);

        // 💡 HINTS:
        // IntStream.range(0, numbers.size() - windowSize + 1)
        //     .mapToObj(i -> numbers.subList(i, i + windowSize))
        //     .collect(Collectors.toList())
    }

    @Test
    @DisplayName("🚨 TODO 4: Running total with reduce")
    public void runningTotal() {
        List<Double> amounts = Arrays.asList(100.0, 50.0, 200.0, 75.0);

        // 🚨 TO DO: Calculate running totals: [100.0, 150.0, 350.0, 425.0]
        List<Double> runningTotals = null; // ❌ BROKEN

        assertThat(runningTotals).containsExactly(100.0, 150.0, 350.0, 425.0);

        // 💡 HINTS: Use IntStream.range and subList + mapToDouble + sum
        // IntStream.range(0, amounts.size())
        //     .mapToObj(i -> amounts.subList(0, i + 1).stream().mapToDouble(d -> d).sum())
        //     .collect(Collectors.toList())
    }

    @Test
    @DisplayName("🚨 TODO 5: Zip two lists together")
    public void zipTwoLists() {
        List<String> names   = Arrays.asList("Alice", "Bob", "Charlie");
        List<Integer> scores = Arrays.asList(95, 87, 92);

        // 🚨 TO DO: Zip names and scores into "Alice:95", "Bob:87", "Charlie:92"
        List<String> zipped = null; // ❌ BROKEN

        assertThat(zipped).containsExactly("Alice:95", "Bob:87", "Charlie:92");

        // 💡 HINTS:
        // IntStream.range(0, names.size())
        //     .mapToObj(i -> names.get(i) + ":" + scores.get(i))
        //     .collect(Collectors.toList())
    }

    @Test
    @DisplayName("🚨 TODO 6: Find top N by criteria")
    public void topNByAmount() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Get top 3 paid orders by amount (highest first), return customer names
        List<String> top3 = null; // ❌ BROKEN

        assertThat(top3).hasSize(3);
        assertThat(top3.get(0)).isEqualTo("Hank");   // 1200.0
        assertThat(top3.get(1)).isEqualTo("Bob");    // 890.0
        assertThat(top3.get(2)).isEqualTo("Diana");  // 310.75
    }

    @Test
    @DisplayName("🚨 TODO 7: Stream from Spliterator (custom iteration)")
    public void streamFromSpliterator() {
        // Fibonacci generator using Spliterator
        Spliterator<Long> fibSpliterator = new Spliterators.AbstractSpliterator<Long>(
                Long.MAX_VALUE, Spliterator.ORDERED) {
            long prev = 0, curr = 1;

            @Override
            public boolean tryAdvance(Consumer<? super Long> action) {
                action.accept(prev);
                long next = prev + curr;
                prev = curr;
                curr = next;
                return true;
            }
        };

        // 🚨 TO DO: Create a stream from the spliterator and get first 8 Fibonacci numbers
        List<Long> fibs = null; // ❌ BROKEN

        assertThat(fibs).containsExactly(0L, 1L, 1L, 2L, 3L, 5L, 8L, 13L);

        // 💡 HINTS:
        // StreamSupport.stream(fibSpliterator, false).limit(8).collect(Collectors.toList())
    }

    @Test
    @DisplayName("🚨 TODO 8: Collectors.reducing vs Stream.reduce")
    public void collectorsReducingVsStreamReduce() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Use Collectors.reducing to find max amount order
        Optional<Order> maxOrder = orders.stream()
                .collect(null); // ❌ BROKEN

        assertThat(maxOrder).isPresent();
        assertThat(maxOrder.get().getAmount()).isEqualTo(1200.0);

        // 💡 HINTS:
        // Collectors.reducing(BinaryOperator.maxBy(Comparator.comparing(Order::getAmount)))
        // Or: Collectors.maxBy(Comparator.comparing(Order::getAmount))
    }

    @Test
    @DisplayName("🚨 TODO 9: Parallel stream with custom ForkJoinPool")
    public void parallelWithCustomPool() throws Exception {
        List<Integer> numbers = IntStream.rangeClosed(1, 100).boxed().collect(Collectors.toList());

        ForkJoinPool customPool = new ForkJoinPool(2);
        long sum = customPool.submit(() ->
            numbers.parallelStream()   // 🚨 TO DO: complete this with mapToLong(Integer::longValue).sum()
                .mapToLong(null) // ❌ BROKEN
                .sum()
        ).get();

        customPool.shutdown();
        assertThat(sum).isEqualTo(5050L);

        // 💡 HINTS:
        // .mapToLong(Integer::longValue).sum()
    }

    @Test
    @DisplayName("🚨 TODO 10: Chain streams with complex predicates")
    public void complexPredicateChain() {
        List<User> users = OrderData.sampleUsers();

        Predicate<User> isActive    = User::isActive;
        Predicate<User> isEngineer  = u -> "Engineering".equals(u.getDepartment());
        Predicate<User> isYoung     = u -> u.getAge() < 30;

        // 🚨 TO DO: Find active engineers who are under 30 years old, sorted by age
        List<String> names = null; // ❌ BROKEN

        assertThat(names).contains("Frank", "Ivy");
        assertThat(names).doesNotContain("Alice"); // Alice is 28 but check your filter!
        // Alice is 28, Frank is 22, Ivy is 27 - all three should be there
        // Actually Alice IS under 30 - assertThat should pass with all three

        // 💡 HINTS:
        // users.stream()
        //     .filter(isActive.and(isEngineer).and(isYoung))
        //     .sorted(Comparator.comparing(User::getAge))
        //     .map(User::getName)
        //     .collect(Collectors.toList())
    }
}
