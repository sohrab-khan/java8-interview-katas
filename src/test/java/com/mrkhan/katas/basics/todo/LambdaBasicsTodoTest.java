package com.mrkhan.katas.basics.todo;

import com.mrkhan.katas.OrderData;
import com.mrkhan.katas.OrderData.Order;
import com.mrkhan.katas.OrderData.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

/**
 * 🎯 LAMBDA BASICS KATAS (20 katas)
 * ──────────────────────────────────
 * Fix each broken test using Java 8 lambda syntax.
 * Run: ./mvnw test -Dtest="LambdaBasicsTodoTest"
 */
@DisplayName("🚨 Lambda Basics - TODO (Fix me!)")
public class LambdaBasicsTodoTest {

    // ─── Static nested interface (local interfaces not allowed in Java 8) ───
    @FunctionalInterface
    interface Formatter {
        String format(String value, int maxLength);
    }

    // ═══════════════════════════════════════════════════════════
    //  KATA 1: Basic Predicate
    // ═══════════════════════════════════════════════════════════
    @Test
    @DisplayName("🚨 TODO 1: Filter paid orders using Predicate")
    public void filterPaidOrders() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Create a Predicate<Order> that returns true for "PAID" orders
        Predicate<Order> isPaid = null; // ❌ BROKEN - fix this

        List<String> result = orders.stream()
                .filter(isPaid)
                .map(Order::getCustomerName)
                .collect(Collectors.toList());

        assertThat(result).containsExactlyInAnyOrder("John", "Bob", "Diana", "Frank", "Hank");

        // 💡 HINTS:
        // 1. isPaid = order -> "PAID".equals(order.getStatus());
    }

    // ═══════════════════════════════════════════════════════════
    //  KATA 2: Predicate Composition - AND
    // ═══════════════════════════════════════════════════════════
    @Test
    @DisplayName("🚨 TODO 2: Combine predicates with .and()")
    public void combinedPredicateAnd() {
        List<Order> orders = OrderData.sampleOrders();

        Predicate<Order> isPaid     = order -> "PAID".equals(order.getStatus());
        Predicate<Order> isExpensive = null; // 🚨 TO DO: amount > 200.0

        // 🚨 TO DO: Combine isPaid AND isExpensive
        Predicate<Order> isPaidAndExpensive = null; // ❌ BROKEN

        List<String> result = orders.stream()
                .filter(isPaidAndExpensive)
                .map(Order::getCustomerName)
                .collect(Collectors.toList());

        assertThat(result).containsExactlyInAnyOrder("John", "Bob", "Diana", "Hank");

        // 💡 HINTS:
        // isExpensive = order -> order.getAmount() > 200.0;
        // isPaidAndExpensive = isPaid.and(isExpensive);
    }

    // ═══════════════════════════════════════════════════════════
    //  KATA 3: Predicate Composition - OR / NEGATE
    // ═══════════════════════════════════════════════════════════
    @Test
    @DisplayName("🚨 TODO 3: Predicate negate and or - find non-paid or high-value orders")
    public void predicateNegateAndOr() {
        List<Order> orders = OrderData.sampleOrders();

        Predicate<Order> isPaid      = order -> "PAID".equals(order.getStatus());
        Predicate<Order> isHighValue = order -> order.getAmount() >= 800.0;

        // 🚨 TO DO: Negate isPaid to get non-paid orders
        Predicate<Order> isNotPaid = null; // ❌ BROKEN - isPaid.negate()

        // 🚨 TO DO: Combine with OR: orders that are either not paid OR high-value
        Predicate<Order> notPaidOrHighValue = null; // ❌ BROKEN - isNotPaid.or(isHighValue)

        long notPaidCount = orders.stream().filter(isNotPaid).count();
        long notPaidOrHighValueCount = orders.stream().filter(notPaidOrHighValue).count();

        assertThat(notPaidCount).isEqualTo(5);
        assertThat(notPaidOrHighValueCount).isEqualTo(7);

        // 💡 HINTS:
        // isNotPaid = isPaid.negate()
        // notPaidOrHighValue = isNotPaid.or(isHighValue)
        // Predicate composition: .and() = &&,  .or() = ||,  .negate() = !
    }

    // ═══════════════════════════════════════════════════════════
    //  KATA 4: Function<T, R>
    // ═══════════════════════════════════════════════════════════
    @Test
    @DisplayName("🚨 TODO 4: Function to extract and transform data")
    public void functionTransform() {
        // 🚨 TO DO: Create a Function<String, String> that trims and uppercases a string
        Function<String, String> trimAndUpper = null; // ❌ BROKEN

        assertThat(trimAndUpper.apply("  hello  ")).isEqualTo("HELLO");
        assertThat(trimAndUpper.apply(" world ")).isEqualTo("WORLD");

        // 💡 HINTS:
        // trimAndUpper = s -> s.trim().toUpperCase();
    }

    // ═══════════════════════════════════════════════════════════
    //  KATA 5: Function Composition - andThen / compose
    // ═══════════════════════════════════════════════════════════
    @Test
    @DisplayName("🚨 TODO 5: Compose Functions with andThen and compose")
    public void functionAndThenAndCompose() {
        Function<String, String> trim    = String::trim;
        Function<String, Integer> length = String::length;

        // 🚨 TO DO: Compose trim then length using andThen (trim first, then length)
        Function<String, Integer> trimThenLength = null; // ❌ BROKEN - trim.andThen(length)

        // 🚨 TO DO: Same result using compose on 'length' (length composed with trim = trim first)
        Function<String, Integer> composed = null; // ❌ BROKEN - length.compose(trim)

        assertThat(trimThenLength.apply("  hello  ")).isEqualTo(5);
        assertThat(composed.apply("  hello  ")).isEqualTo(5);

        // 💡 HINTS:
        // trimThenLength = trim.andThen(length)  →  length(trim(input))
        // composed       = length.compose(trim)  →  length(trim(input))  ← same result, reversed call order
        // f.andThen(g) = g after f  |  f.compose(g) = f after g
    }

    // ═══════════════════════════════════════════════════════════
    //  KATA 6: Consumer<T>
    // ═══════════════════════════════════════════════════════════
    @Test
    @DisplayName("🚨 TODO 6: Consumer to collect results as side effect")
    public void consumerSideEffect() {
        List<Order> orders = OrderData.sampleOrders();
        List<String> collected = new ArrayList<>();

        // 🚨 TO DO: Create a Consumer<Order> that adds customerName to 'collected'
        Consumer<Order> collectName = null; // ❌ BROKEN

        orders.stream()
                .filter(o -> "PAID".equals(o.getStatus()))
                .forEach(collectName);

        assertThat(collected).hasSize(5);
        assertThat(collected).contains("John", "Bob");

        // 💡 HINTS:
        // collectName = order -> collected.add(order.getCustomerName());
    }

    // ═══════════════════════════════════════════════════════════
    //  KATA 7: BiFunction<T, U, R>
    // ═══════════════════════════════════════════════════════════
    @Test
    @DisplayName("🚨 TODO 7: BiFunction to combine two inputs")
    public void biFunctionCombine() {
        // 🚨 TO DO: BiFunction that formats "name (status)"
        BiFunction<String, String, String> formatOrder = null; // ❌ BROKEN

        assertThat(formatOrder.apply("John", "PAID")).isEqualTo("John (PAID)");
        assertThat(formatOrder.apply("Alice", "PENDING")).isEqualTo("Alice (PENDING)");

        // 💡 HINTS:
        // formatOrder = (name, status) -> name + " (" + status + ")";
    }

    // ═══════════════════════════════════════════════════════════
    //  KATA 8: Supplier<T>
    // ═══════════════════════════════════════════════════════════
    @Test
    @DisplayName("🚨 TODO 8: Supplier for lazy initialization")
    public void supplierLazyInit() {
        // 🚨 TO DO: Create a Supplier<List<String>> that returns a new empty ArrayList each time
        Supplier<List<String>> listSupplier = null; // ❌ BROKEN

        List<String> list1 = listSupplier.get();
        List<String> list2 = listSupplier.get();

        assertThat(list1).isNotNull().isEmpty();
        assertThat(list2).isNotNull().isEmpty();
        assertThat(list1).isNotSameAs(list2); // Different instances!

        // 💡 HINTS:
        // listSupplier = ArrayList::new;
    }

    // ═══════════════════════════════════════════════════════════
    //  KATA 9: UnaryOperator<T>
    // ═══════════════════════════════════════════════════════════
    @Test
    @DisplayName("🚨 TODO 9: UnaryOperator to transform in place")
    public void unaryOperator() {
        List<String> names = new ArrayList<>(Arrays.asList("alice", "bob", "charlie"));

        // 🚨 TO DO: Create a UnaryOperator<String> that capitalizes the first letter
        UnaryOperator<String> capitalize = null; // ❌ BROKEN

        names.replaceAll(capitalize);

        assertThat(names).containsExactly("Alice", "Bob", "Charlie");

        // 💡 HINTS:
        // capitalize = s -> s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    // ═══════════════════════════════════════════════════════════
    //  KATA 10: BinaryOperator<T>
    // ═══════════════════════════════════════════════════════════
    @Test
    @DisplayName("🚨 TODO 10: BinaryOperator with reduce")
    public void binaryOperatorReduce() {
        List<Double> amounts = Arrays.asList(100.0, 250.0, 75.5, 430.0);

        // 🚨 TO DO: Create a BinaryOperator<Double> that returns the larger of two doubles
        BinaryOperator<Double> maxOp = null; // ❌ BROKEN

        Optional<Double> max = amounts.stream().reduce(maxOp);

        assertThat(max).isPresent();
        assertThat(max.get()).isEqualTo(430.0);

        // 💡 HINTS:
        // maxOp = (a, b) -> a > b ? a : b;
        // Or: BinaryOperator<Double> maxOp = Double::max;
    }

    // ═══════════════════════════════════════════════════════════
    //  KATA 11: Method Reference - Static
    // ═══════════════════════════════════════════════════════════
    @Test
    @DisplayName("🚨 TODO 11: Static method reference Integer::parseInt")
    public void staticMethodReference() {
        List<String> numbers = Arrays.asList("1", "2", "3", "4", "5");

        // 🚨 TO DO: Use a static method reference to convert strings to integers
        Function<String, Integer> parser = null; // ❌ BROKEN - use method reference

        List<Integer> result = numbers.stream()
                .map(parser)
                .collect(Collectors.toList());

        assertThat(result).containsExactly(1, 2, 3, 4, 5);

        // 💡 HINTS:
        // parser = Integer::parseInt;  (NOT s -> Integer.parseInt(s))
    }

    // ═══════════════════════════════════════════════════════════
    //  KATA 12: Method Reference - Instance on Arbitrary Object
    // ═══════════════════════════════════════════════════════════
    @Test
    @DisplayName("🚨 TODO 12: Instance method reference on arbitrary object")
    public void instanceMethodReferenceArbitrary() {
        List<String> words = Arrays.asList("Hello", "WORLD", "java", "Lambda");

        // 🚨 TO DO: Map using String::toUpperCase (method reference, not lambda)
        List<String> result = words.stream()
                .map((Function<String, String>) null) // ❌ BROKEN - replace null with method reference
                .collect(Collectors.toList());

        assertThat(result).containsExactly("HELLO", "WORLD", "JAVA", "LAMBDA");

        // 💡 HINTS:
        // .map(String::toUpperCase)
    }

    // ═══════════════════════════════════════════════════════════
    //  KATA 13: Method Reference - Constructor
    // ═══════════════════════════════════════════════════════════
    @Test
    @DisplayName("🚨 TODO 13: Constructor reference ArrayList::new")
    public void constructorMethodReference() {
        // 🚨 TO DO: Use constructor reference to create a Supplier<List<String>>
        Supplier<List<String>> listFactory = null; // ❌ BROKEN - use ArrayList::new

        List<String> list = listFactory.get();
        list.add("test");

        assertThat(list).hasSize(1).contains("test");

        // 💡 HINTS:
        // listFactory = ArrayList::new;
    }

    // ═══════════════════════════════════════════════════════════
    //  KATA 14: Effectively Final
    // ═══════════════════════════════════════════════════════════
    @Test
    @DisplayName("🚨 TODO 14: Lambda capturing effectively final variable")
    public void effectivelyFinalCapture() {
        String prefix = "ORDER-"; // effectively final

        List<Integer> ids = Arrays.asList(1, 2, 3, 4, 5);

        // 🚨 TO DO: Map each id to "ORDER-1", "ORDER-2" etc. using the captured prefix
        List<String> result = ids.stream()
                .map((Function<Integer, String>) null) // ❌ BROKEN - use lambda that captures prefix
                .collect(Collectors.toList());

        assertThat(result).containsExactly("ORDER-1", "ORDER-2", "ORDER-3", "ORDER-4", "ORDER-5");

        // 💡 HINTS:
        // .map(id -> prefix + id)
    }

    // ═══════════════════════════════════════════════════════════
    //  KATA 15: Custom Functional Interface
    // ═══════════════════════════════════════════════════════════
    @Test
    @DisplayName("🚨 TODO 15: Custom @FunctionalInterface")
    public void customFunctionalInterface() {
        // 🚨 TO DO: Implement Formatter (defined above as nested interface)
        // Truncate value to maxLength, append "..." if truncated
        Formatter truncate = null; // ❌ BROKEN

        assertThat(truncate.format("Hello World", 5)).isEqualTo("Hello...");
        assertThat(truncate.format("Hi", 5)).isEqualTo("Hi");

        // 💡 HINTS:
        // truncate = (value, maxLength) -> value.length() > maxLength
        //     ? value.substring(0, maxLength) + "..."
        //     : value;
    }

    // ═══════════════════════════════════════════════════════════
    //  KATA 16: Comparator.comparing
    // ═══════════════════════════════════════════════════════════
    @Test
    @DisplayName("🚨 TODO 16: Sort orders by amount using Comparator.comparing")
    public void comparatorComparing() {
        List<Order> orders = new ArrayList<>(OrderData.sampleOrders());

        // 🚨 TO DO: Sort by amount ascending using Comparator.comparing
        orders.sort(null); // ❌ BROKEN

        assertThat(orders.get(0).getAmount()).isEqualTo(45.0);
        assertThat(orders.get(orders.size() - 1).getAmount()).isEqualTo(1200.0);

        // 💡 HINTS:
        // orders.sort(Comparator.comparing(Order::getAmount));
    }

    // ═══════════════════════════════════════════════════════════
    //  KATA 17: Comparator thenComparing
    // ═══════════════════════════════════════════════════════════
    @Test
    @DisplayName("🚨 TODO 17: Multi-level sort - category then amount")
    public void comparatorThenComparing() {
        List<Order> orders = new ArrayList<>(OrderData.sampleOrders());

        // 🚨 TO DO: Sort by category ASC, then amount DESC
        Comparator<Order> comparator = null; // ❌ BROKEN

        orders.sort(comparator);

        // First group = Books (alphabetically first)
        assertThat(orders.get(0).getCategory()).isEqualTo("Books");

        // 💡 HINTS:
        // comparator = Comparator.comparing(Order::getCategory)
        //     .thenComparing(Comparator.comparing(Order::getAmount).reversed());
    }

    // ═══════════════════════════════════════════════════════════
    //  KATA 18: Predicate with stream - partitioningBy
    // ═══════════════════════════════════════════════════════════
    @Test
    @DisplayName("🚨 TODO 18: Partition orders into paid and non-paid")
    public void partitionOrders() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Partition orders into paid (true) and non-paid (false)
        Map<Boolean, List<Order>> partitioned = null; // ❌ BROKEN

        assertThat(partitioned.get(true)).hasSize(5);
        assertThat(partitioned.get(false)).hasSize(5);

        // 💡 HINTS:
        // partitioned = orders.stream()
        //     .collect(Collectors.partitioningBy(o -> "PAID".equals(o.getStatus())));
    }

    // ═══════════════════════════════════════════════════════════
    //  KATA 19: Function.identity()
    // ═══════════════════════════════════════════════════════════
    @Test
    @DisplayName("🚨 TODO 19: Use Function.identity() for toMap")
    public void functionIdentityToMap() {
        List<String> words = Arrays.asList("apple", "banana", "cherry");

        // 🚨 TO DO: Create Map<String, Integer> where key=word, value=length
        // Use Function.identity() for the key mapper
        Map<String, Integer> result = null; // ❌ BROKEN

        assertThat(result).containsEntry("apple", 5);
        assertThat(result).containsEntry("banana", 6);
        assertThat(result).containsEntry("cherry", 6);

        // 💡 HINTS:
        // result = words.stream()
        //     .collect(Collectors.toMap(Function.identity(), String::length));
    }

    // ═══════════════════════════════════════════════════════════
    //  KATA 20: Lambda with exception handling
    // ═══════════════════════════════════════════════════════════
    @Test
    @DisplayName("🚨 TODO 20: Safe integer parsing with lambda")
    public void safeParsingLambda() {
        List<String> inputs = Arrays.asList("1", "abc", "3", "xyz", "5");

        // 🚨 TO DO: Parse valid integers, skip invalid ones (return -1 for invalid)
        Function<String, Integer> safeParse = null; // ❌ BROKEN

        List<Integer> result = inputs.stream()
                .map(safeParse)
                .filter(n -> n > 0)
                .collect(Collectors.toList());

        assertThat(result).containsExactly(1, 3, 5);

        // 💡 HINTS:
        // safeParse = s -> {
        //     try { return Integer.parseInt(s); }
        //     catch (NumberFormatException e) { return -1; }
        // };
    }
}
