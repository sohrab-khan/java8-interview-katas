package com.mrkhan.katas.collectors.solutions;

import com.mrkhan.katas.OrderData;
import com.mrkhan.katas.OrderData.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.*;
import java.util.function.*;

import static org.assertj.core.api.Assertions.*;

/**
 * ✅ COLLECTORS - SOLUTIONS
 */
@DisplayName("✅ Collectors - Solutions")
public class CollectorsSolutionTest {

    @Test @DisplayName("✅ SOLUTION 1: groupingBy single level")
    public void groupingBySingle_solution() {
        List<Order> orders = OrderData.sampleOrders();
        Map<String, List<Order>> byCategory =
                orders.stream().collect(Collectors.groupingBy(Order::getCategory));
        assertThat(byCategory.get("Electronics")).hasSize(3);
        assertThat(byCategory.get("Books")).hasSize(4);
        // 💡 groupingBy returns Map<K, List<V>> by default
    }

    @Test @DisplayName("✅ SOLUTION 2: groupingBy + counting")
    public void groupingByCount_solution() {
        List<Order> orders = OrderData.sampleOrders();
        Map<String, Long> countPerCategory = orders.stream()
                .collect(Collectors.groupingBy(Order::getCategory, Collectors.counting()));
        assertThat(countPerCategory.get("Electronics")).isEqualTo(3L);
        assertThat(countPerCategory.get("Books")).isEqualTo(4L);
    }

    @Test @DisplayName("✅ SOLUTION 3: groupingBy + summingDouble")
    public void groupingBySumming_solution() {
        List<Order> orders = OrderData.sampleOrders();
        Map<String, Double> revenuePerCategory = orders.stream()
                .collect(Collectors.groupingBy(Order::getCategory,
                        Collectors.summingDouble(Order::getAmount)));
        assertThat(revenuePerCategory.get("Clothing")).isEqualTo(740.75);
        // 💡 summingInt, summingLong, summingDouble available
    }

    @Test @DisplayName("✅ SOLUTION 4: groupingBy + averaging")
    public void groupingByAveraging_solution() {
        List<User> users = OrderData.sampleUsers();
        Map<String, Double> avgAgeByDept = users.stream()
                .collect(Collectors.groupingBy(User::getDepartment,
                        Collectors.averagingInt(User::getAge)));
        assertThat(avgAgeByDept.get("Engineering")).isGreaterThan(25.0);
        // 💡 averagingInt, averagingLong, averagingDouble
    }

    @Test @DisplayName("✅ SOLUTION 5: groupingBy + mapping downstream")
    public void groupingByMapping_solution() {
        List<Order> orders = OrderData.sampleOrders();
        Map<String, List<String>> namesByCategory = orders.stream()
                .collect(Collectors.groupingBy(Order::getCategory,
                        Collectors.mapping(Order::getCustomerName, Collectors.toList())));
        assertThat(namesByCategory.get("Books")).contains("Alice", "Charlie");
        // 💡 mapping(extractor, downstream) transforms before collecting
    }

    @Test @DisplayName("✅ SOLUTION 6: groupingBy + maxBy")
    public void groupingByMax_solution() {
        List<Order> orders = OrderData.sampleOrders();
        Map<String, Optional<Order>> maxByCategory = orders.stream()
                .collect(Collectors.groupingBy(Order::getCategory,
                        Collectors.maxBy(Comparator.comparing(Order::getAmount))));
        assertThat(maxByCategory.get("Electronics").get().getAmount()).isEqualTo(1200.0);
        // 💡 Also available: minBy()
    }

    @Test @DisplayName("✅ SOLUTION 7: Multi-level groupingBy")
    public void multiLevelGroupingBy_solution() {
        List<Order> orders = OrderData.sampleOrders();
        Map<String, Map<String, List<Order>>> byCategAndStatus = orders.stream()
                .collect(Collectors.groupingBy(Order::getCategory,
                        Collectors.groupingBy(Order::getStatus)));
        assertThat(byCategAndStatus.get("Electronics")).containsKey("PAID");
        // 💡 Nested groupingBy creates two-level map
    }

    @Test @DisplayName("✅ SOLUTION 8: groupingBy with TreeMap")
    public void groupingBySortedMap_solution() {
        List<Order> orders = OrderData.sampleOrders();
        Map<String, Long> sortedMap = orders.stream()
                .collect(Collectors.groupingBy(Order::getCategory, TreeMap::new, Collectors.counting()));
        assertThat(new ArrayList<>(sortedMap.keySet()))
                .containsExactly("Books", "Clothing", "Electronics");
        // 💡 groupingBy(classifier, mapFactory, downstream) lets you specify map type
    }

    @Test @DisplayName("✅ SOLUTION 9: toMap with merge function")
    public void toMapWithMerge_solution() {
        List<Order> orders = OrderData.sampleOrders();
        Map<String, Double> categoryTotals = orders.stream()
                .collect(Collectors.toMap(
                        Order::getCategory,
                        Order::getAmount,
                        Double::sum // merge function for duplicate keys
                ));
        assertThat(categoryTotals.get("Books")).isEqualTo(244.49);
        // 💡 Without merge function: IllegalStateException on duplicate key!
    }

    @Test @DisplayName("✅ SOLUTION 10: toMap id -> object")
    public void toMapIdToObject_solution() {
        List<User> users = OrderData.sampleUsers();
        Map<Integer, User> userById = users.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));
        assertThat(userById.get(1).getName()).isEqualTo("Alice");
        assertThat(userById).hasSize(10);
        // 💡 Function.identity() = u -> u (returns the element itself)
    }

    @Test @DisplayName("✅ SOLUTION 11: partitioningBy simple")
    public void partitioningBySimple_solution() {
        List<Transaction> txns = OrderData.sampleTransactions();
        Map<Boolean, List<Transaction>> partitioned =
                txns.stream().collect(Collectors.partitioningBy(Transaction::isSuccessful));
        assertThat(partitioned.get(true)).hasSize(7);
        assertThat(partitioned.get(false)).hasSize(3);
    }

    @Test @DisplayName("✅ SOLUTION 12: partitioningBy + counting")
    public void partitioningByCount_solution() {
        List<Product> products = OrderData.sampleProducts();
        Map<Boolean, Long> priceSplit = products.stream()
                .collect(Collectors.partitioningBy(p -> p.getPrice() > 100, Collectors.counting()));
        assertThat(priceSplit.get(true)).isEqualTo(3L);
        assertThat(priceSplit.get(false)).isEqualTo(7L);
    }

    @Test @DisplayName("✅ SOLUTION 13: counting active users")
    public void counting_solution() {
        List<User> users = OrderData.sampleUsers();
        long activeCount = users.stream().filter(User::isActive).count();
        assertThat(activeCount).isEqualTo(8L);
    }

    @Test @DisplayName("✅ SOLUTION 14: joining with separator and wrapping")
    public void joiningVariants_solution() {
        List<User> users = OrderData.sampleUsers();
        String nameList = users.stream()
                .map(User::getName)
                .collect(Collectors.joining(" | ", "<<", ">>"));
        assertThat(nameList).startsWith("<<");
        assertThat(nameList).endsWith(">>");
        assertThat(nameList).contains(" | ");
        // 💡 joining(delimiter) | joining(delimiter, prefix, suffix)
    }

    @Test @DisplayName("✅ SOLUTION 15: summarizingInt")
    public void summarizingInt_solution() {
        List<Product> products = OrderData.sampleProducts();
        IntSummaryStatistics stockStats = products.stream()
                .collect(Collectors.summarizingInt(Product::getStock));
        assertThat(stockStats.getMin()).isEqualTo(5);
        assertThat(stockStats.getMax()).isEqualTo(200);
        assertThat(stockStats.getSum()).isEqualTo(605L);
        // 💡 summarizingInt = count+sum+min+max+average in ONE pass!
    }

    @Test @DisplayName("✅ SOLUTION 16: groupingBy + joining downstream")
    public void groupingByJoining_solution() {
        List<Order> orders = OrderData.sampleOrders();
        Map<String, String> nameStringByCategory = orders.stream()
                .collect(Collectors.groupingBy(Order::getCategory,
                        Collectors.mapping(Order::getCustomerName, Collectors.joining(", "))));
        assertThat(nameStringByCategory.get("Books")).contains("Alice");
        assertThat(nameStringByCategory.get("Books")).contains(",");
    }

    @Test @DisplayName("✅ SOLUTION 17: collect to Set")
    public void collectToSet_solution() {
        List<Order> orders = OrderData.sampleOrders();
        Set<String> statuses = orders.stream()
                .map(Order::getStatus)
                .collect(Collectors.toSet());
        assertThat(statuses).containsExactlyInAnyOrder("PAID", "PENDING", "CANCELED", "SHIPPED");
    }

    @Test @DisplayName("✅ SOLUTION 18: groupingBy dept count")
    public void groupingByDeptCount_solution() {
        List<User> users = OrderData.sampleUsers();
        Map<String, Long> countByDept = users.stream()
                .collect(Collectors.groupingBy(User::getDepartment, Collectors.counting()));
        assertThat(countByDept.get("Engineering")).isEqualTo(4L);
        assertThat(countByDept.get("HR")).isEqualTo(2L);
    }

    @Test @DisplayName("✅ SOLUTION 19: Collectors.reducing() for product")
    public void customCollector_solution() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Optional<Integer> product = numbers.stream()
                .collect(Collectors.reducing((a, b) -> a * b));
        assertThat(product).isPresent();
        assertThat(product.get()).isEqualTo(120);
        // 💡 Collectors.reducing() mirrors Stream.reduce()
        // 💡 reducing(identity, mapper, op) is the most powerful form
    }

    @Test @DisplayName("✅ SOLUTION 20: maxBy paid order")
    public void collectingAndThenFirst_solution() {
        List<Order> orders = OrderData.sampleOrders();
        Optional<Order> mostExpensivePaid = orders.stream()
                .filter(o -> "PAID".equals(o.getStatus()))
                .collect(Collectors.maxBy(Comparator.comparing(Order::getAmount)));
        assertThat(mostExpensivePaid).isPresent();
        assertThat(mostExpensivePaid.get().getAmount()).isEqualTo(1200.0);
    }

    @Test @DisplayName("✅ SOLUTION 21: groupingBy price range")
    public void groupingByPriceRange_solution() {
        List<Product> products = OrderData.sampleProducts();
        Map<String, List<String>> grouped = products.stream()
                .collect(Collectors.groupingBy(
                        p -> {
                            if (p.getPrice() < 50) return "budget";
                            else if (p.getPrice() <= 500) return "mid";
                            else return "premium";
                        },
                        Collectors.mapping(Product::getName, Collectors.toList())
                ));
        assertThat(grouped.get("budget")).isNotEmpty();
        assertThat(grouped.get("premium")).contains("Laptop", "Phone");
    }

    @Test @DisplayName("✅ SOLUTION 22: toMap email to username")
    public void toMapWithIdentity_solution() {
        List<String> emails = Arrays.asList("alice@corp.com", "bob@corp.com", "charlie@corp.com");
        Map<String, String> emailToUsername = emails.stream()
                .collect(Collectors.toMap(Function.identity(), e -> e.split("@")[0]));
        assertThat(emailToUsername.get("alice@corp.com")).isEqualTo("alice");
        assertThat(emailToUsername.get("bob@corp.com")).isEqualTo("bob");
    }

    @Test @DisplayName("✅ SOLUTION 23: count by currency")
    public void countingByMultipleCriteria_solution() {
        List<Transaction> txns = OrderData.sampleTransactions();
        Map<String, Long> countByCurrency = txns.stream()
                .collect(Collectors.groupingBy(Transaction::getCurrency, Collectors.counting()));
        assertThat(countByCurrency.get("USD")).isEqualTo(4L);
        assertThat(countByCurrency.get("EUR")).isEqualTo(3L);
    }

    @Test @DisplayName("✅ SOLUTION 24: Group active users by dept")
    public void groupActiveUsersByDept_solution() {
        List<User> users = OrderData.sampleUsers();
        Map<String, List<String>> activeNamesByDept = users.stream()
                .filter(User::isActive)
                .collect(Collectors.groupingBy(User::getDepartment,
                        Collectors.mapping(User::getName, Collectors.toList())));
        assertThat(activeNamesByDept.get("Engineering")).contains("Alice", "Bob", "Frank", "Ivy");
    }

    @Test @DisplayName("✅ SOLUTION 25: Collect all tags (flatMap)")
    public void flatMappingCollector_solution() {
        List<Order> orders = OrderData.sampleOrders();
        Set<String> allTags = orders.stream()
                .flatMap(o -> o.getTags().stream())
                .collect(Collectors.toSet());
        assertThat(allTags).containsExactlyInAnyOrder("new", "vip", "repeat");
        // 💡 Java 9 has Collectors.flatMapping(Order::getTags, Collectors.toSet())
        // 💡 Java 8: use flatMap before collect
    }
}
