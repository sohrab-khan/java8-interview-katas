package com.mrkhan.katas.advanced.solutions;

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
 * ✅ ADVANCED STREAMS - SOLUTIONS
 */
@DisplayName("✅ Advanced Streams - Solutions")
public class AdvancedStreamsSolutionTest {

    @Test @DisplayName("✅ SOLUTION 1: Deep flatMap 3 levels")
    public void deepFlatMap_solution() {
        List<Department> departments = OrderData.sampleDepartments();

        List<String> allRoles = departments.stream()
                .flatMap(d -> d.getEmployees().stream())
                .flatMap(u -> u.getRoles().stream())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        assertThat(allRoles).contains("ADMIN", "DEV", "HR_ADMIN");
        assertThat(allRoles).doesNotHaveDuplicates();
        // 💡 Multiple flatMap levels "peel" layers of nesting
    }

    @Test @DisplayName("✅ SOLUTION 2: Custom Collector with Collector.of()")
    public void customCollectorOf_solution() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        int sumOfSquares = numbers.stream()
                .collect(Collector.of(
                        () -> new int[1],                    // supplier: int[0] as accumulator
                        (acc, n) -> acc[0] += n * n,         // accumulator
                        (left, right) -> { left[0] += right[0]; return left; }, // combiner
                        acc -> acc[0]                         // finisher
                ));

        assertThat(sumOfSquares).isEqualTo(385);
        // 💡 Collector.of(supplier, accumulator, combiner, finisher)
        // 💡 combiner needed for parallel streams
        // 💡 int[1] as mutable box is the Java 8 trick (no lambdas can modify primitives)
    }

    @Test @DisplayName("✅ SOLUTION 3: Sliding window")
    public void slidingWindow_solution() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        int windowSize = 3;

        List<List<Integer>> windows = IntStream.range(0, numbers.size() - windowSize + 1)
                .mapToObj(i -> numbers.subList(i, i + windowSize))
                .collect(Collectors.toList());

        assertThat(windows).hasSize(8);
        assertThat(windows.get(0)).containsExactly(1, 2, 3);
        assertThat(windows.get(7)).containsExactly(8, 9, 10);
        // 💡 subList returns a view, not a copy – efficient!
    }

    @Test @DisplayName("✅ SOLUTION 4: Running total")
    public void runningTotal_solution() {
        List<Double> amounts = Arrays.asList(100.0, 50.0, 200.0, 75.0);

        List<Double> runningTotals = IntStream.range(0, amounts.size())
                .mapToObj(i -> amounts.subList(0, i + 1).stream().mapToDouble(d -> d).sum())
                .collect(Collectors.toList());

        assertThat(runningTotals).containsExactly(100.0, 150.0, 350.0, 425.0);
        // 💡 O(n²) but readable. For O(n), use imperative loop or AtomicReference
    }

    @Test @DisplayName("✅ SOLUTION 5: Zip two lists")
    public void zipTwoLists_solution() {
        List<String> names   = Arrays.asList("Alice", "Bob", "Charlie");
        List<Integer> scores = Arrays.asList(95, 87, 92);

        List<String> zipped = IntStream.range(0, names.size())
                .mapToObj(i -> names.get(i) + ":" + scores.get(i))
                .collect(Collectors.toList());

        assertThat(zipped).containsExactly("Alice:95", "Bob:87", "Charlie:92");
        // 💡 Java 8 has no built-in zip – IntStream.range trick is the standard
        // 💡 Guava: Streams.zip(s1, s2, combiner)
    }

    @Test @DisplayName("✅ SOLUTION 6: Top N by criteria")
    public void topNByAmount_solution() {
        List<Order> orders = OrderData.sampleOrders();

        List<String> top3 = orders.stream()
                .filter(o -> "PAID".equals(o.getStatus()))
                .sorted(Comparator.comparing(Order::getAmount).reversed())
                .limit(3)
                .map(Order::getCustomerName)
                .collect(Collectors.toList());

        assertThat(top3.get(0)).isEqualTo("Hank");
        assertThat(top3.get(1)).isEqualTo("Bob");
        assertThat(top3.get(2)).isEqualTo("Diana");
        // 💡 sorted + limit is cleaner than collecting then sorting outside
    }

    @Test @DisplayName("✅ SOLUTION 7: Stream from Spliterator")
    public void streamFromSpliterator_solution() {
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

        List<Long> fibs = StreamSupport.stream(fibSpliterator, false)
                .limit(8)
                .collect(Collectors.toList());

        assertThat(fibs).containsExactly(0L, 1L, 1L, 2L, 3L, 5L, 8L, 13L);
        // 💡 StreamSupport.stream(spliterator, parallel) = low-level stream creation
        // 💡 Used when integrating with non-Collection data sources
    }

    @Test @DisplayName("✅ SOLUTION 8: Collectors.reducing")
    public void collectorsReducingVsStreamReduce_solution() {
        List<Order> orders = OrderData.sampleOrders();

        Optional<Order> maxOrder = orders.stream()
                .collect(Collectors.maxBy(Comparator.comparing(Order::getAmount)));

        assertThat(maxOrder).isPresent();
        assertThat(maxOrder.get().getAmount()).isEqualTo(1200.0);
        // 💡 Collectors.reducing is useful when you need to reduce inside groupingBy
    }

    @Test @DisplayName("✅ SOLUTION 9: Parallel with custom ForkJoinPool")
    public void parallelWithCustomPool_solution() throws Exception {
        List<Integer> numbers = IntStream.rangeClosed(1, 100).boxed().collect(Collectors.toList());

        ForkJoinPool customPool = new ForkJoinPool(2);
        long sum = customPool.submit(() ->
                numbers.parallelStream().mapToLong(Integer::longValue).sum()
        ).get();

        customPool.shutdown();
        assertThat(sum).isEqualTo(5050L);
        // 💡 Trick: submit lambda to ForkJoinPool makes parallelStream use that pool
        // 💡 Java 21: VirtualThread executors replace this pattern
    }

    @Test @DisplayName("✅ SOLUTION 10: Complex predicate chain")
    public void complexPredicateChain_solution() {
        List<User> users = OrderData.sampleUsers();

        Predicate<User> isActive   = User::isActive;
        Predicate<User> isEngineer = u -> "Engineering".equals(u.getDepartment());
        Predicate<User> isYoung    = u -> u.getAge() < 30;

        List<String> names = users.stream()
                .filter(isActive.and(isEngineer).and(isYoung))
                .sorted(Comparator.comparing(User::getAge))
                .map(User::getName)
                .collect(Collectors.toList());

        assertThat(names).contains("Frank", "Ivy", "Alice");
        // 💡 Predicate.and() composes predicates cleanly
        // 💡 Reusable predicates → single responsibility, testable
    }
}
