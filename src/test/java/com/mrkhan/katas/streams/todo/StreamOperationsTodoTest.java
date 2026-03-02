package com.mrkhan.katas.streams.todo;

import com.mrkhan.katas.OrderData;
import com.mrkhan.katas.OrderData.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.*;

import static org.assertj.core.api.Assertions.*;

/**
 * 🎯 STREAM OPERATIONS KATAS (25 katas)
 * ─────────────────────────────────────
 * Fix each broken test using Java 8 Stream API.
 * Run: ./mvnw test -Dtest="StreamOperationsTodoTest"
 */
@DisplayName("🚨 Stream Operations - TODO (Fix me!)")
public class StreamOperationsTodoTest {

    // ─── CREATION ───────────────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 1: Create stream from array using Arrays.stream")
    public void streamFromArray() {
        int[] numbers = {5, 3, 8, 1, 9, 2, 7};

        // 🚨 TO DO: Create an IntStream from the array and find the sum
        int sum = 0; // ❌ BROKEN - use Arrays.stream(numbers).sum()

        assertThat(sum).isEqualTo(35);
    }

    @Test
    @DisplayName("🚨 TODO 2: Generate infinite stream and limit")
    public void infiniteStreamLimit() {
        // 🚨 TO DO: Generate infinite stream of even numbers starting at 0, take first 5
        List<Integer> evens = null; // ❌ BROKEN

        assertThat(evens).containsExactly(0, 2, 4, 6, 8);

        // 💡 HINTS:
        // Stream.iterate(0, n -> n + 2).limit(5).collect(Collectors.toList())
    }

    @Test
    @DisplayName("🚨 TODO 3: IntStream.range for index-based operations")
    public void intStreamRange() {
        // 🚨 TO DO: Use IntStream.range to create a list of "Item-0", "Item-1", ..., "Item-4"
        List<String> items = null; // ❌ BROKEN

        assertThat(items).containsExactly("Item-0", "Item-1", "Item-2", "Item-3", "Item-4");

        // 💡 HINTS:
        // IntStream.range(0, 5).mapToObj(i -> "Item-" + i).collect(Collectors.toList())
    }

    // ─── INTERMEDIATE OPERATIONS ────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 4: flatMap to flatten nested lists")
    public void flatMapNestedLists() {
        List<Department> departments = OrderData.sampleDepartments();

        // 🚨 TO DO: Get all employee names from all departments as a flat list
        List<String> allNames = null; // ❌ BROKEN

        assertThat(allNames).hasSize(10);
        assertThat(allNames).contains("Alice", "Bob", "Eve", "Hank");

        // 💡 HINTS:
        // departments.stream()
        //     .flatMap(dept -> dept.getEmployees().stream())
        //     .map(User::getName)
        //     .collect(Collectors.toList())
    }

    @Test
    @DisplayName("🚨 TODO 5: flatMap to split strings into words")
    public void flatMapSplitWords() {
        List<String> sentences = Arrays.asList(
                "Java 8 streams", "are very powerful", "use them daily"
        );

        // 🚨 TO DO: Split each sentence into words and flatten into a single list
        List<String> words = null; // ❌ BROKEN

        assertThat(words).hasSize(9);
        assertThat(words).contains("Java", "streams", "powerful");

        // 💡 HINTS:
        // sentences.stream()
        //     .flatMap(s -> Arrays.stream(s.split(" ")))
        //     .collect(Collectors.toList())
    }

    @Test
    @DisplayName("🚨 TODO 6: distinct + sorted combination")
    public void distinctSorted() {
        List<Integer> numbers = Arrays.asList(5, 3, 1, 3, 2, 5, 4, 1, 2);

        // 🚨 TO DO: Remove duplicates and sort ascending
        List<Integer> result = null; // ❌ BROKEN

        assertThat(result).containsExactly(1, 2, 3, 4, 5);

        // 💡 HINTS:
        // numbers.stream().distinct().sorted().collect(Collectors.toList())
    }

    @Test
    @DisplayName("🚨 TODO 7: peek for debugging (non-consuming)")
    public void peekDebugging() {
        List<String> debugLog = new ArrayList<>();
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Use peek() to log each order's customer name before filtering
        List<Order> paidOrders = orders.stream()
                // ADD: .peek(o -> debugLog.add(o.getCustomerName()))
                .filter(o -> "PAID".equals(o.getStatus()))
                .collect(Collectors.toList()); // ❌ BROKEN - missing peek

        // Verify peek logged ALL orders (before filter)
        assertThat(debugLog).hasSize(10);  // all 10 orders
        assertThat(paidOrders).hasSize(5);
    }

    @Test
    @DisplayName("🚨 TODO 8: limit + skip for pagination")
    public void limitSkipPagination() {
        List<Order> orders = OrderData.sampleOrders();
        int pageSize = 3;
        int pageNumber = 1; // 0-indexed, so page 1 = items 3,4,5

        // 🚨 TO DO: Implement pagination - get page 1 (0-indexed) with pageSize=3
        List<Order> page = null; // ❌ BROKEN

        assertThat(page).hasSize(3);
        assertThat(page.get(0).getId()).isEqualTo(4); // 4th order (0-indexed page 1)

        // 💡 HINTS:
        // orders.stream()
        //     .skip((long) pageNumber * pageSize)
        //     .limit(pageSize)
        //     .collect(Collectors.toList())
    }

    @Test
    @DisplayName("🚨 TODO 9: sorted with custom Comparator")
    public void sortedCustomComparator() {
        List<User> users = OrderData.sampleUsers();

        // 🚨 TO DO: Sort users by age descending
        List<String> names = null; // ❌ BROKEN - sorted by age desc, collect names

        assertThat(names.get(0)).isEqualTo("Hank");   // age 45
        assertThat(names.get(1)).isEqualTo("Diana");  // age 41

        // 💡 HINTS:
        // users.stream()
        //     .sorted(Comparator.comparing(User::getAge).reversed())
        //     .map(User::getName)
        //     .collect(Collectors.toList())
    }

    @Test
    @DisplayName("🚨 TODO 10: map to different type")
    public void mapToDifferentType() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Map orders to their amounts as a list of doubles
        List<Double> amounts = null; // ❌ BROKEN

        assertThat(amounts).hasSize(10);
        assertThat(amounts).contains(250.0, 120.50, 890.0);

        // 💡 HINTS:
        // orders.stream().map(Order::getAmount).collect(Collectors.toList())
    }

    // ─── TERMINAL OPERATIONS ────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 11: reduce to sum amounts")
    public void reduceSum() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Sum all order amounts using reduce()
        double total = 0; // ❌ BROKEN - use mapToDouble + sum OR reduce

        assertThat(total).isEqualTo(4061.25);

        // 💡 HINTS:
        // orders.stream().mapToDouble(Order::getAmount).sum()
        // OR
        // orders.stream().map(Order::getAmount).reduce(0.0, Double::sum)
    }

    @Test
    @DisplayName("🚨 TODO 12: min and max finding")
    public void minMaxFinding() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Find the cheapest order
        Optional<Order> cheapest = Optional.empty(); // ❌ BROKEN

        assertThat(cheapest).isPresent();
        assertThat(cheapest.get().getAmount()).isEqualTo(45.0);

        // 💡 HINTS:
        // orders.stream().min(Comparator.comparing(Order::getAmount))
    }

    @Test
    @DisplayName("🚨 TODO 13: anyMatch / allMatch / noneMatch")
    public void matchOperations() {
        List<Transaction> transactions = OrderData.sampleTransactions();

        // 🚨 TO DO: Check these 3 conditions
        boolean hasFailedTransaction   = false; // ❌ use anyMatch
        boolean allHavePositiveAmount  = false; // ❌ use allMatch
        boolean noNegativeAmounts      = false; // ❌ use noneMatch

        assertThat(hasFailedTransaction).isTrue();
        assertThat(allHavePositiveAmount).isTrue();
        assertThat(noNegativeAmounts).isTrue();

        // 💡 HINTS:
        // hasFailedTransaction = transactions.stream().anyMatch(t -> !t.isSuccessful())
        // allHavePositiveAmount = transactions.stream().allMatch(t -> t.getAmount() > 0)
        // noNegativeAmounts = transactions.stream().noneMatch(t -> t.getAmount() < 0)
    }

    @Test
    @DisplayName("🚨 TODO 14: findFirst with filter")
    public void findFirstWithFilter() {
        List<User> users = OrderData.sampleUsers();

        // 🚨 TO DO: Find first active Engineering user
        Optional<User> firstEngineer = Optional.empty(); // ❌ BROKEN

        assertThat(firstEngineer).isPresent();
        assertThat(firstEngineer.get().getDepartment()).isEqualTo("Engineering");
        assertThat(firstEngineer.get().isActive()).isTrue();

        // 💡 HINTS:
        // users.stream()
        //     .filter(User::isActive)
        //     .filter(u -> "Engineering".equals(u.getDepartment()))
        //     .findFirst()
    }

    @Test
    @DisplayName("🚨 TODO 15: count elements matching criteria")
    public void countMatching() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Count orders with amount > 300
        long count = 0; // ❌ BROKEN

        assertThat(count).isEqualTo(5);

        // 💡 HINTS:
        // orders.stream().filter(o -> o.getAmount() > 300).count()
    }

    @Test
    @DisplayName("🚨 TODO 16: toArray conversion")
    public void toArrayConversion() {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        // 🚨 TO DO: Convert stream to String array using toArray
        String[] array = null; // ❌ BROKEN

        assertThat(array).containsExactly("Alice", "Bob", "Charlie");
        assertThat(array).isInstanceOf(String[].class);

        // 💡 HINTS:
        // names.stream().toArray(String[]::new)
    }

    // ─── PRIMITIVE STREAMS ──────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 17: mapToInt + statistics")
    public void mapToIntStatistics() {
        List<User> users = OrderData.sampleUsers();

        // 🚨 TO DO: Get IntSummaryStatistics for user ages
        IntSummaryStatistics stats = null; // ❌ BROKEN

        assertThat(stats.getMin()).isEqualTo(22);
        assertThat(stats.getMax()).isEqualTo(45);
        assertThat(stats.getCount()).isEqualTo(10);

        // 💡 HINTS:
        // users.stream().mapToInt(User::getAge).summaryStatistics()
    }

    @Test
    @DisplayName("🚨 TODO 18: mapToDouble average")
    public void mapToDoubleAverage() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Calculate average order amount
        OptionalDouble avg = OptionalDouble.empty(); // ❌ BROKEN

        assertThat(avg).isPresent();
        assertThat(avg.getAsDouble()).isGreaterThan(400.0);

        // 💡 HINTS:
        // orders.stream().mapToDouble(Order::getAmount).average()
    }

    @Test
    @DisplayName("🚨 TODO 19: LongStream.range with complex mapping")
    public void longStreamRange() {
        // 🚨 TO DO: Create a list of squares for numbers 1 to 5 using LongStream
        List<Long> squares = null; // ❌ BROKEN

        assertThat(squares).containsExactly(1L, 4L, 9L, 16L, 25L);

        // 💡 HINTS:
        // LongStream.rangeClosed(1, 5).map(n -> n * n).boxed().collect(Collectors.toList())
    }

    // ─── ADVANCED PATTERNS ──────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 20: String joining with delimiter")
    public void stringJoining() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Join paid order customer names with ", " prefix "[" suffix "]"
        String result = ""; // ❌ BROKEN

        assertThat(result).startsWith("[");
        assertThat(result).endsWith("]");
        assertThat(result).contains("John").contains("Bob");

        // 💡 HINTS:
        // orders.stream()
        //     .filter(o -> "PAID".equals(o.getStatus()))
        //     .map(Order::getCustomerName)
        //     .collect(Collectors.joining(", ", "[", "]"))
    }

    @Test
    @DisplayName("🚨 TODO 21: Filter unique categories")
    public void uniqueCategories() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Get unique order categories, sorted alphabetically
        List<String> categories = null; // ❌ BROKEN

        assertThat(categories).containsExactly("Books", "Clothing", "Electronics");

        // 💡 HINTS:
        // orders.stream()
        //     .map(Order::getCategory)
        //     .distinct()
        //     .sorted()
        //     .collect(Collectors.toList())
    }

    @Test
    @DisplayName("🚨 TODO 22: Multiple stream pipelines - chain results")
    public void chainedPipelines() {
        List<Product> products = OrderData.sampleProducts();

        // 🚨 TO DO: Find names of in-stock Electronics products costing < 200, sorted by price
        List<String> result = null; // ❌ BROKEN

        assertThat(result).containsExactly("Mouse", "Keyboard", "Headphones");

        // 💡 HINTS:
        // products.stream()
        //     .filter(p -> "Electronics".equals(p.getCategory()))
        //     .filter(p -> p.getPrice() < 200.0)
        //     .filter(p -> p.getStock() > 0)
        //     .sorted(Comparator.comparing(Product::getPrice))
        //     .map(Product::getName)
        //     .collect(Collectors.toList())
    }

    @Test
    @DisplayName("🚨 TODO 23: Collect to unmodifiable list (Java 8 way)")
    public void collectUnmodifiable() {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        // 🚨 TO DO: Collect stream to unmodifiable list (Java 8 way using collectingAndThen)
        List<String> immutable = null; // ❌ BROKEN

        assertThat(immutable).containsExactly("Alice", "Bob", "Charlie");
        assertThatThrownBy(() -> immutable.add("Dave"))
                .isInstanceOf(UnsupportedOperationException.class);

        // 💡 HINTS:
        // Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList)
    }

    @Test
    @DisplayName("🚨 TODO 24: Stream with Optional.ofNullable")
    public void streamWithNullableValues() {
        List<String> mixedList = Arrays.asList("Alice", null, "Bob", null, "Charlie");

        // 🚨 TO DO: Filter out nulls and collect valid names
        List<String> result = null; // ❌ BROKEN

        assertThat(result).containsExactly("Alice", "Bob", "Charlie");

        // 💡 HINTS:
        // mixedList.stream()
        //     .filter(Objects::nonNull)
        //     .collect(Collectors.toList())
    }

    @Test
    @DisplayName("🚨 TODO 25: Parallel stream for large data")
    public void parallelStreamSum() {
        // 🚨 TO DO: Sum numbers from 1 to 1_000_000 using parallel stream
        long sum = 0; // ❌ BROKEN

        assertThat(sum).isEqualTo(500_000_500_000L);

        // 💡 HINTS:
        // LongStream.rangeClosed(1, 1_000_000).parallel().sum()
        // OR
        // IntStream.rangeClosed(1, 1_000_000).asLongStream().parallel().sum()
    }
}
