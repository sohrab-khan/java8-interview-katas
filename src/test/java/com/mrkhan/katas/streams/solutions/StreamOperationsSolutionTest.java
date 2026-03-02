package com.mrkhan.katas.streams.solutions;

import com.mrkhan.katas.OrderData;
import com.mrkhan.katas.OrderData.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.*;

import static org.assertj.core.api.Assertions.*;

/**
 * ✅ STREAM OPERATIONS - SOLUTIONS
 */
@DisplayName("✅ Stream Operations - Solutions")
public class StreamOperationsSolutionTest {

    @Test
    @DisplayName("✅ SOLUTION 1: Stream from array")
    public void streamFromArray_solution() {
        int[] numbers = {5, 3, 8, 1, 9, 2, 7};

        int sum = Arrays.stream(numbers).sum();

        assertThat(sum).isEqualTo(35);
        // 💡 Arrays.stream(int[]) returns IntStream (no boxing overhead)
    }

    @Test
    @DisplayName("✅ SOLUTION 2: Infinite stream + limit")
    public void infiniteStreamLimit_solution() {
        List<Integer> evens = Stream.iterate(0, n -> n + 2)
                .limit(5)
                .collect(Collectors.toList());

        assertThat(evens).containsExactly(0, 2, 4, 6, 8);
        // 💡 Stream.iterate generates: 0, 2, 4, 6, 8, 10, ... (must limit!)
        // 💡 Java 9: Stream.iterate(0, n -> n < 10, n -> n + 2) – built-in predicate
    }

    @Test
    @DisplayName("✅ SOLUTION 3: IntStream.range")
    public void intStreamRange_solution() {
        List<String> items = IntStream.range(0, 5)
                .mapToObj(i -> "Item-" + i)
                .collect(Collectors.toList());

        assertThat(items).containsExactly("Item-0", "Item-1", "Item-2", "Item-3", "Item-4");
        // 💡 range(0,5) = [0,1,2,3,4] (exclusive end)
        // 💡 rangeClosed(0,5) = [0,1,2,3,4,5] (inclusive end)
    }

    @Test
    @DisplayName("✅ SOLUTION 4: flatMap nested lists")
    public void flatMapNestedLists_solution() {
        List<Department> departments = OrderData.sampleDepartments();

        List<String> allNames = departments.stream()
                .flatMap(dept -> dept.getEmployees().stream())
                .map(User::getName)
                .collect(Collectors.toList());

        assertThat(allNames).hasSize(10);
        assertThat(allNames).contains("Alice", "Bob", "Eve", "Hank");
        // 💡 flatMap unwraps Stream<Stream<T>> to Stream<T>
        // 💡 Think: map gives you "depth" layers, flatMap flattens one layer
    }

    @Test
    @DisplayName("✅ SOLUTION 5: flatMap split sentences")
    public void flatMapSplitWords_solution() {
        List<String> sentences = Arrays.asList(
                "Java 8 streams", "are very powerful", "use them daily"
        );

        List<String> words = sentences.stream()
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .collect(Collectors.toList());

        assertThat(words).hasSize(9);
        assertThat(words).contains("Java", "streams", "powerful");
    }

    @Test
    @DisplayName("✅ SOLUTION 6: distinct + sorted")
    public void distinctSorted_solution() {
        List<Integer> numbers = Arrays.asList(5, 3, 1, 3, 2, 5, 4, 1, 2);

        List<Integer> result = numbers.stream()
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        assertThat(result).containsExactly(1, 2, 3, 4, 5);
        // 💡 Order matters: distinct() before sorted() is generally more efficient
        // 💡 distinct() uses equals() and hashCode()
    }

    @Test
    @DisplayName("✅ SOLUTION 7: peek debugging")
    public void peekDebugging_solution() {
        List<String> debugLog = new ArrayList<>();
        List<Order> orders = OrderData.sampleOrders();

        List<Order> paidOrders = orders.stream()
                .peek(o -> debugLog.add(o.getCustomerName()))
                .filter(o -> "PAID".equals(o.getStatus()))
                .collect(Collectors.toList());

        assertThat(debugLog).hasSize(10);
        assertThat(paidOrders).hasSize(5);
        // 💡 peek() is an intermediate op – it sees EVERY element BEFORE the next op
        // 💡 Never use peek() for real processing logic – use map/forEach
    }

    @Test
    @DisplayName("✅ SOLUTION 8: limit + skip pagination")
    public void limitSkipPagination_solution() {
        List<Order> orders = OrderData.sampleOrders();
        int pageSize = 3;
        int pageNumber = 1;

        List<Order> page = orders.stream()
                .skip((long) pageNumber * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());

        assertThat(page).hasSize(3);
        assertThat(page.get(0).getId()).isEqualTo(4);
        // 💡 Classic pagination formula: skip(page * size).limit(size)
        // 💡 ALWAYS skip() before limit() for pagination
    }

    @Test
    @DisplayName("✅ SOLUTION 9: sorted with reversed Comparator")
    public void sortedCustomComparator_solution() {
        List<User> users = OrderData.sampleUsers();

        List<String> names = users.stream()
                .sorted(Comparator.comparing(User::getAge).reversed())
                .map(User::getName)
                .collect(Collectors.toList());

        assertThat(names.get(0)).isEqualTo("Hank");
        assertThat(names.get(1)).isEqualTo("Diana");
        // 💡 .reversed() creates a new Comparator with reversed order
        // 💡 Comparator.comparingInt(User::getAge) avoids Integer boxing
    }

    @Test
    @DisplayName("✅ SOLUTION 10: map to different type")
    public void mapToDifferentType_solution() {
        List<Order> orders = OrderData.sampleOrders();

        List<Double> amounts = orders.stream()
                .map(Order::getAmount)
                .collect(Collectors.toList());

        assertThat(amounts).hasSize(10);
        assertThat(amounts).contains(250.0, 120.50, 890.0);
    }

    @Test
    @DisplayName("✅ SOLUTION 11: reduce sum")
    public void reduceSum_solution() {
        List<Order> orders = OrderData.sampleOrders();

        double total = orders.stream()
                .mapToDouble(Order::getAmount)
                .sum();

        assertThat(total).isEqualTo(4061.25);
        // 💡 mapToDouble().sum() is faster than map(Double).reduce(0.0, Double::sum)
        // 💡 No boxing/unboxing with primitive streams
    }

    @Test
    @DisplayName("✅ SOLUTION 12: min finding")
    public void minMaxFinding_solution() {
        List<Order> orders = OrderData.sampleOrders();

        Optional<Order> cheapest = orders.stream()
                .min(Comparator.comparing(Order::getAmount));

        assertThat(cheapest).isPresent();
        assertThat(cheapest.get().getAmount()).isEqualTo(45.0);
        // 💡 min/max return Optional because stream might be empty
    }

    @Test
    @DisplayName("✅ SOLUTION 13: anyMatch / allMatch / noneMatch")
    public void matchOperations_solution() {
        List<Transaction> transactions = OrderData.sampleTransactions();

        boolean hasFailedTransaction  = transactions.stream().anyMatch(t -> !t.isSuccessful());
        boolean allHavePositiveAmount = transactions.stream().allMatch(t -> t.getAmount() > 0);
        boolean noNegativeAmounts     = transactions.stream().noneMatch(t -> t.getAmount() < 0);

        assertThat(hasFailedTransaction).isTrue();
        assertThat(allHavePositiveAmount).isTrue();
        assertThat(noNegativeAmounts).isTrue();
        // 💡 Short-circuit: anyMatch stops at first true, allMatch stops at first false
        // 💡 More efficient than filter().count() > 0 for existence check
    }

    @Test
    @DisplayName("✅ SOLUTION 14: findFirst with filter")
    public void findFirstWithFilter_solution() {
        List<User> users = OrderData.sampleUsers();

        Optional<User> firstEngineer = users.stream()
                .filter(User::isActive)
                .filter(u -> "Engineering".equals(u.getDepartment()))
                .findFirst();

        assertThat(firstEngineer).isPresent();
        assertThat(firstEngineer.get().getDepartment()).isEqualTo("Engineering");
        // 💡 findFirst() is short-circuit – stops after finding first match
        // 💡 findAny() is faster for parallel streams (no ordering guarantee)
    }

    @Test
    @DisplayName("✅ SOLUTION 15: count matching")
    public void countMatching_solution() {
        List<Order> orders = OrderData.sampleOrders();

        long count = orders.stream()
                .filter(o -> o.getAmount() > 300)
                .count();

        assertThat(count).isEqualTo(5);
    }

    @Test
    @DisplayName("✅ SOLUTION 16: toArray")
    public void toArrayConversion_solution() {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        String[] array = names.stream().toArray(String[]::new);

        assertThat(array).containsExactly("Alice", "Bob", "Charlie");
        assertThat(array).isInstanceOf(String[].class);
        // 💡 String[]::new is a constructor reference for array creation
        // 💡 stream.toArray() without arg returns Object[] (not String[])
    }

    @Test
    @DisplayName("✅ SOLUTION 17: mapToInt + statistics")
    public void mapToIntStatistics_solution() {
        List<User> users = OrderData.sampleUsers();

        IntSummaryStatistics stats = users.stream()
                .mapToInt(User::getAge)
                .summaryStatistics();

        assertThat(stats.getMin()).isEqualTo(22);
        assertThat(stats.getMax()).isEqualTo(45);
        assertThat(stats.getCount()).isEqualTo(10);
        // 💡 summaryStatistics() gives count, sum, min, max, average in one pass
        // 💡 Also: DoubleSummaryStatistics, LongSummaryStatistics
    }

    @Test
    @DisplayName("✅ SOLUTION 18: mapToDouble average")
    public void mapToDoubleAverage_solution() {
        List<Order> orders = OrderData.sampleOrders();

        OptionalDouble avg = orders.stream()
                .mapToDouble(Order::getAmount)
                .average();

        assertThat(avg).isPresent();
        assertThat(avg.getAsDouble()).isGreaterThan(400.0);
    }

    @Test
    @DisplayName("✅ SOLUTION 19: LongStream squares")
    public void longStreamRange_solution() {
        List<Long> squares = LongStream.rangeClosed(1, 5)
                .map(n -> n * n)
                .boxed()
                .collect(Collectors.toList());

        assertThat(squares).containsExactly(1L, 4L, 9L, 16L, 25L);
        // 💡 .boxed() converts LongStream -> Stream<Long> for collection
    }

    @Test
    @DisplayName("✅ SOLUTION 20: String joining")
    public void stringJoining_solution() {
        List<Order> orders = OrderData.sampleOrders();

        String result = orders.stream()
                .filter(o -> "PAID".equals(o.getStatus()))
                .map(Order::getCustomerName)
                .collect(Collectors.joining(", ", "[", "]"));

        assertThat(result).startsWith("[");
        assertThat(result).endsWith("]");
        assertThat(result).contains("John").contains("Bob");
        // 💡 joining(delimiter, prefix, suffix)
        // 💡 joining() alone = concatenation
        // 💡 joining(", ") = comma-separated
    }

    @Test
    @DisplayName("✅ SOLUTION 21: Unique categories")
    public void uniqueCategories_solution() {
        List<Order> orders = OrderData.sampleOrders();

        List<String> categories = orders.stream()
                .map(Order::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        assertThat(categories).containsExactly("Books", "Clothing", "Electronics");
    }

    @Test
    @DisplayName("✅ SOLUTION 22: Chained pipeline")
    public void chainedPipelines_solution() {
        List<Product> products = OrderData.sampleProducts();

        List<String> result = products.stream()
                .filter(p -> "Electronics".equals(p.getCategory()))
                .filter(p -> p.getPrice() < 200.0)
                .filter(p -> p.getStock() > 0)
                .sorted(Comparator.comparing(Product::getPrice))
                .map(Product::getName)
                .collect(Collectors.toList());

        assertThat(result).containsExactly("Mouse", "Keyboard", "Headphones");
        // 💡 Stream laziness: elements flow through ALL ops before next element
        // 💡 Pipeline is optimized by the JVM (filter fusion, etc.)
    }

    @Test
    @DisplayName("✅ SOLUTION 23: Unmodifiable list (Java 8)")
    public void collectUnmodifiable_solution() {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        List<String> immutable = names.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        Collections::unmodifiableList
                ));

        assertThat(immutable).containsExactly("Alice", "Bob", "Charlie");
        assertThatThrownBy(() -> immutable.add("Dave"))
                .isInstanceOf(UnsupportedOperationException.class);
        // 💡 collectingAndThen(downstream, finisher) applies finisher after collecting
        // 💡 Java 10+: Collectors.toUnmodifiableList() is cleaner
    }

    @Test
    @DisplayName("✅ SOLUTION 24: Filter null values")
    public void streamWithNullableValues_solution() {
        List<String> mixedList = Arrays.asList("Alice", null, "Bob", null, "Charlie");

        List<String> result = mixedList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        assertThat(result).containsExactly("Alice", "Bob", "Charlie");
        // 💡 Objects::nonNull is cleaner than s -> s != null
        // 💡 Objects::isNull for the inverse
    }

    @Test
    @DisplayName("✅ SOLUTION 25: Parallel stream sum")
    public void parallelStreamSum_solution() {
        long sum = LongStream.rangeClosed(1, 1_000_000)
                .parallel()
                .sum();

        assertThat(sum).isEqualTo(500_000_500_000L);
        // 💡 .parallel() uses ForkJoinPool.commonPool() by default
        // 💡 sum() is associative - safe for parallel (unlike string concat)
        // 💡 For small ranges, sequential is faster (parallelism overhead)
    }
}
