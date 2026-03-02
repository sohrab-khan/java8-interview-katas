package com.mrkhan.katas.basics.solutions;

import com.mrkhan.katas.OrderData;
import com.mrkhan.katas.OrderData.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

/**
 * ✅ LAMBDA BASICS - SOLUTIONS
 * Study these AFTER attempting the todo tests.
 * Each solution includes interview insights.
 */
@DisplayName("✅ Lambda Basics - Solutions")
public class LambdaBasicsSolutionTest {

    // ─── Static nested interface (local interfaces not allowed in Java 8) ───
    @FunctionalInterface
    interface Formatter {
        String format(String value, int maxLength);
    }

    @Test
    @DisplayName("✅ SOLUTION 1: Filter paid orders using Predicate")
    public void filterPaidOrders_solution() {
        List<Order> orders = OrderData.sampleOrders();

        Predicate<Order> isPaid = order -> "PAID".equals(order.getStatus());

        List<String> result = orders.stream()
                .filter(isPaid)
                .map(Order::getCustomerName)
                .collect(Collectors.toList());

        assertThat(result).containsExactlyInAnyOrder("John", "Bob", "Diana", "Frank", "Hank");

        // 💡 INTERVIEW INSIGHTS:
        // 1. "PAID".equals(order.getStatus()) prevents NPE if status is null
        // 2. Predicate<T> is a functional interface: T -> boolean
        // 3. Prefer method reference Order::getCustomerName over o -> o.getCustomerName()
    }

    @Test
    @DisplayName("✅ SOLUTION 2: Combine predicates with .and()")
    public void combinedPredicateAnd_solution() {
        List<Order> orders = OrderData.sampleOrders();

        Predicate<Order> isPaid      = order -> "PAID".equals(order.getStatus());
        Predicate<Order> isExpensive = order -> order.getAmount() > 200.0;
        Predicate<Order> isPaidAndExpensive = isPaid.and(isExpensive);

        List<String> result = orders.stream()
                .filter(isPaidAndExpensive)
                .map(Order::getCustomerName)
                .collect(Collectors.toList());

        assertThat(result).containsExactlyInAnyOrder("John", "Bob", "Diana", "Hank");

        // 💡 INTERVIEW INSIGHTS:
        // 1. Predicate.and() = logical AND (short-circuits on false)
        // 2. Predicate.or()  = logical OR  (short-circuits on true)
        // 3. Predicate.negate() = logical NOT
        // 4. Short-circuit: if isPaid is false, isExpensive is NOT evaluated
    }

    @Test
    @DisplayName("✅ SOLUTION 3: Predicate negate and or")
    public void predicateNegateAndOr_solution() {
        List<Order> orders = OrderData.sampleOrders();

        Predicate<Order> isPaid      = order -> "PAID".equals(order.getStatus());
        Predicate<Order> isHighValue = order -> order.getAmount() >= 800.0;

        Predicate<Order> isNotPaid          = isPaid.negate();
        Predicate<Order> notPaidOrHighValue = isNotPaid.or(isHighValue);

        long notPaidCount            = orders.stream().filter(isNotPaid).count();
        long notPaidOrHighValueCount = orders.stream().filter(notPaidOrHighValue).count();

        assertThat(notPaidCount).isEqualTo(5);
        assertThat(notPaidOrHighValueCount).isEqualTo(7);

        // 💡 INTERVIEW INSIGHTS:
        // 1. .negate() = !, .and() = &&, .or() = || — all return new Predicates
        // 2. Predicate.not(p) is Java 11; in Java 8 use p.negate()
        // 3. Composing predicates eliminates nested if/else chains
    }

    @Test
    @DisplayName("✅ SOLUTION 4: Function<T,R> transform")
    public void functionTransform_solution() {
        Function<String, String> trimAndUpper = s -> s.trim().toUpperCase();

        assertThat(trimAndUpper.apply("  hello  ")).isEqualTo("HELLO");
        assertThat(trimAndUpper.apply(" world ")).isEqualTo("WORLD");

        // 💡 INTERVIEW INSIGHTS:
        // 1. Function<T,R>: T -> R (input type, output type)
        // 2. .apply() executes the function
        // 3. Can also compose: trim.andThen(String::toUpperCase)
    }

    @Test
    @DisplayName("✅ SOLUTION 5: Function.andThen and compose")
    public void functionAndThenAndCompose_solution() {
        Function<String, String>  trim   = String::trim;
        Function<String, Integer> length = String::length;

        Function<String, Integer> trimThenLength = trim.andThen(length);    // trim → length
        Function<String, Integer> composed       = length.compose(trim);    // trim → length (same)

        assertThat(trimThenLength.apply("  hello  ")).isEqualTo(5);
        assertThat(composed.apply("  hello  ")).isEqualTo(5);

        // 💡 INTERVIEW INSIGHTS:
        // f.andThen(g): apply f first, then g  →  g(f(x))
        // f.compose(g): apply g first, then f  →  f(g(x))
        // trim.andThen(length) == length.compose(trim) — same pipeline, different call site
        // Think of andThen as "pipe forward" (|>), compose as "math function notation"
    }

    @Test
    @DisplayName("✅ SOLUTION 6: Consumer side effect")
    public void consumerSideEffect_solution() {
        List<Order> orders = OrderData.sampleOrders();
        List<String> collected = new ArrayList<>();

        Consumer<Order> collectName = order -> collected.add(order.getCustomerName());

        orders.stream()
                .filter(o -> "PAID".equals(o.getStatus()))
                .forEach(collectName);

        assertThat(collected).hasSize(5);
        assertThat(collected).contains("John", "Bob");

        // 💡 INTERVIEW INSIGHTS:
        // 1. Consumer<T>: T -> void (side effects only)
        // 2. Consumer.andThen(other) chains consumers
        // 3. Avoid side effects in streams when possible – prefer collect()
    }

    @Test
    @DisplayName("✅ SOLUTION 7: BiFunction")
    public void biFunctionCombine_solution() {
        BiFunction<String, String, String> formatOrder = (name, status) -> name + " (" + status + ")";

        assertThat(formatOrder.apply("John", "PAID")).isEqualTo("John (PAID)");
        assertThat(formatOrder.apply("Alice", "PENDING")).isEqualTo("Alice (PENDING)");

        // 💡 INTERVIEW INSIGHTS:
        // 1. BiFunction<T,U,R>: (T,U) -> R
        // 2. BiConsumer<T,U>: (T,U) -> void
        // 3. BiPredicate<T,U>: (T,U) -> boolean
    }

    @Test
    @DisplayName("✅ SOLUTION 8: Supplier lazy init")
    public void supplierLazyInit_solution() {
        Supplier<List<String>> listSupplier = ArrayList::new;

        List<String> list1 = listSupplier.get();
        List<String> list2 = listSupplier.get();

        assertThat(list1).isNotNull().isEmpty();
        assertThat(list2).isNotNull().isEmpty();
        assertThat(list1).isNotSameAs(list2);

        // 💡 INTERVIEW INSIGHTS:
        // 1. Supplier<T>: () -> T (deferred/lazy execution)
        // 2. ArrayList::new is a constructor method reference
        // 3. Used heavily in Optional.orElseGet(() -> expensiveCall())
    }

    @Test
    @DisplayName("✅ SOLUTION 9: UnaryOperator")
    public void unaryOperator_solution() {
        List<String> names = new ArrayList<>(Arrays.asList("alice", "bob", "charlie"));

        UnaryOperator<String> capitalize =
                s -> s.substring(0, 1).toUpperCase() + s.substring(1);

        names.replaceAll(capitalize);

        assertThat(names).containsExactly("Alice", "Bob", "Charlie");

        // 💡 INTERVIEW INSIGHTS:
        // 1. UnaryOperator<T> extends Function<T,T> (same input and output type)
        // 2. List.replaceAll() takes UnaryOperator<E>
        // 3. Equivalent: names.stream().map(capitalize).collect(toList())
    }

    @Test
    @DisplayName("✅ SOLUTION 10: BinaryOperator with reduce")
    public void binaryOperatorReduce_solution() {
        List<Double> amounts = Arrays.asList(100.0, 250.0, 75.5, 430.0);

        BinaryOperator<Double> maxOp = (a, b) -> a > b ? a : b;
        // Or: BinaryOperator<Double> maxOp = Double::max;

        Optional<Double> max = amounts.stream().reduce(maxOp);

        assertThat(max).isPresent();
        assertThat(max.get()).isEqualTo(430.0);

        // 💡 INTERVIEW INSIGHTS:
        // 1. BinaryOperator<T> extends BiFunction<T,T,T>
        // 2. reduce() without identity returns Optional (stream might be empty)
        // 3. reduce(0, Integer::sum) is safer than reduce(Integer::sum)
    }

    @Test
    @DisplayName("✅ SOLUTION 11: Static method reference")
    public void staticMethodReference_solution() {
        List<String> numbers = Arrays.asList("1", "2", "3", "4", "5");

        Function<String, Integer> parser = Integer::parseInt;

        List<Integer> result = numbers.stream()
                .map(parser)
                .collect(Collectors.toList());

        assertThat(result).containsExactly(1, 2, 3, 4, 5);

        // 💡 INTERVIEW INSIGHTS:
        // 4 types of method references:
        // 1. Static:     Class::staticMethod     (Integer::parseInt)
        // 2. Bound:      instance::method         (str::toUpperCase)
        // 3. Unbound:    Class::instanceMethod    (String::toUpperCase)
        // 4. Constructor: Class::new              (ArrayList::new)
    }

    @Test
    @DisplayName("✅ SOLUTION 12: Instance method reference on arbitrary object")
    public void instanceMethodReferenceArbitrary_solution() {
        List<String> words = Arrays.asList("Hello", "WORLD", "java", "Lambda");

        List<String> result = words.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        assertThat(result).containsExactly("HELLO", "WORLD", "JAVA", "LAMBDA");

        // 💡 INTERVIEW INSIGHTS:
        // String::toUpperCase = s -> s.toUpperCase()
        // The stream element IS the receiver of the method call
        // This is "unbound" instance method reference
    }

    @Test
    @DisplayName("✅ SOLUTION 13: Constructor method reference")
    public void constructorMethodReference_solution() {
        Supplier<List<String>> listFactory = ArrayList::new;

        List<String> list = listFactory.get();
        list.add("test");

        assertThat(list).hasSize(1).contains("test");

        // 💡 INTERVIEW INSIGHTS:
        // ArrayList::new equivalent to () -> new ArrayList<>()
        // With Function<Integer,List>: ArrayList::new → capacity constructor
    }

    @Test
    @DisplayName("✅ SOLUTION 14: Effectively final capture")
    public void effectivelyFinalCapture_solution() {
        String prefix = "ORDER-";

        List<Integer> ids = Arrays.asList(1, 2, 3, 4, 5);

        List<String> result = ids.stream()
                .map(id -> prefix + id)
                .collect(Collectors.toList());

        assertThat(result).containsExactly("ORDER-1", "ORDER-2", "ORDER-3", "ORDER-4", "ORDER-5");

        // 💡 INTERVIEW INSIGHTS:
        // "Effectively final" = not declared final but never reassigned
        // Lambdas capture variables, not values (but immutability makes it safe)
        // Use AtomicInteger if you need mutable captured variable
    }

    @Test
    @DisplayName("✅ SOLUTION 15: Custom @FunctionalInterface")
    public void customFunctionalInterface_solution() {
        Formatter truncate = (value, maxLength) ->
                value.length() > maxLength ? value.substring(0, maxLength) + "..." : value;

        assertThat(truncate.format("Hello World", 5)).isEqualTo("Hello...");
        assertThat(truncate.format("Hi", 5)).isEqualTo("Hi");

        // 💡 INTERVIEW INSIGHTS:
        // @FunctionalInterface enforces single abstract method at compile time
        // Any interface with 1 SAM can be used as functional interface
        // @FunctionalInterface is optional but recommended for documentation
    }

    @Test
    @DisplayName("✅ SOLUTION 16: Comparator.comparing")
    public void comparatorComparing_solution() {
        List<Order> orders = new ArrayList<>(OrderData.sampleOrders());

        orders.sort(Comparator.comparing(Order::getAmount));

        assertThat(orders.get(0).getAmount()).isEqualTo(45.0);
        assertThat(orders.get(orders.size() - 1).getAmount()).isEqualTo(1200.0);

        // 💡 INTERVIEW INSIGHTS:
        // Comparator.comparing() creates Comparator from key extractor
        // .reversed() for descending order
        // Comparator.comparingInt/Double for primitives (avoids boxing)
    }

    @Test
    @DisplayName("✅ SOLUTION 17: Comparator thenComparing")
    public void comparatorThenComparing_solution() {
        List<Order> orders = new ArrayList<>(OrderData.sampleOrders());

        Comparator<Order> comparator = Comparator.comparing(Order::getCategory)
                .thenComparing(Comparator.comparing(Order::getAmount).reversed());

        orders.sort(comparator);

        assertThat(orders.get(0).getCategory()).isEqualTo("Books");

        // 💡 INTERVIEW INSIGHTS:
        // thenComparing() breaks ties in the primary sort
        // .reversed() reverses the entire comparator
        // Chain as many as needed: .thenComparing(...)
    }

    @Test
    @DisplayName("✅ SOLUTION 18: Partition orders")
    public void partitionOrders_solution() {
        List<Order> orders = OrderData.sampleOrders();

        Map<Boolean, List<Order>> partitioned = orders.stream()
                .collect(Collectors.partitioningBy(o -> "PAID".equals(o.getStatus())));

        assertThat(partitioned.get(true)).hasSize(5);
        assertThat(partitioned.get(false)).hasSize(5);

        // 💡 INTERVIEW INSIGHTS:
        // partitioningBy always returns a Map with exactly 2 keys: true and false
        // groupingBy is more general (many keys), partitioningBy is for binary splits
    }

    @Test
    @DisplayName("✅ SOLUTION 19: Function.identity() for toMap")
    public void functionIdentityToMap_solution() {
        List<String> words = Arrays.asList("apple", "banana", "cherry");

        Map<String, Integer> result = words.stream()
                .collect(Collectors.toMap(Function.identity(), String::length));

        assertThat(result).containsEntry("apple", 5);
        assertThat(result).containsEntry("banana", 6);

        // 💡 INTERVIEW INSIGHTS:
        // Function.identity() = t -> t (returns its input)
        // More readable than w -> w in toMap key mapper
        // toMap throws IllegalStateException on duplicate keys!
        // Solution: .toMap(k, v, (v1, v2) -> v1) to handle duplicates
    }

    @Test
    @DisplayName("✅ SOLUTION 20: Safe parsing with lambda")
    public void safeParsingLambda_solution() {
        List<String> inputs = Arrays.asList("1", "abc", "3", "xyz", "5");

        Function<String, Integer> safeParse = s -> {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                return -1;
            }
        };

        List<Integer> result = inputs.stream()
                .map(safeParse)
                .filter(n -> n > 0)
                .collect(Collectors.toList());

        assertThat(result).containsExactly(1, 3, 5);

        // 💡 INTERVIEW INSIGHTS:
        // Lambda bodies CAN contain try-catch
        // Best practice: extract to named method for reuse
        // Alternative: wrap checked exceptions with RuntimeException
    }
}
