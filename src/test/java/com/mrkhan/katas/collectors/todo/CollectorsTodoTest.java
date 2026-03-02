package com.mrkhan.katas.collectors.todo;

import com.mrkhan.katas.OrderData;
import com.mrkhan.katas.OrderData.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.*;
import java.util.function.*;

import static org.assertj.core.api.Assertions.*;

/**
 * 🎯 COLLECTORS KATAS (25 katas)
 * ──────────────────────────────
 * Master Collectors.groupingBy, toMap, partitioningBy, custom collectors.
 * Run: ./mvnw test -Dtest="CollectorsTodoTest"
 */
@DisplayName("🚨 Collectors - TODO (Fix me!)")
public class CollectorsTodoTest {

    @Test
    @DisplayName("🚨 TODO 1: groupingBy single level")
    public void groupingBySingle() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Group orders by category
        Map<String, List<Order>> byCategory = null; // ❌ BROKEN

        assertThat(byCategory).containsKey("Electronics");
        assertThat(byCategory).containsKey("Books");
        assertThat(byCategory.get("Electronics")).hasSize(3);
        assertThat(byCategory.get("Books")).hasSize(4);
    }

    @Test
    @DisplayName("🚨 TODO 2: groupingBy + counting downstream")
    public void groupingByCount() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Count orders per category
        Map<String, Long> countPerCategory = null; // ❌ BROKEN

        assertThat(countPerCategory.get("Electronics")).isEqualTo(3L);
        assertThat(countPerCategory.get("Books")).isEqualTo(4L);
        assertThat(countPerCategory.get("Clothing")).isEqualTo(2L);
    }

    @Test
    @DisplayName("🚨 TODO 3: groupingBy + summingDouble downstream")
    public void groupingBySumming() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Total revenue per category
        Map<String, Double> revenuePerCategory = null; // ❌ BROKEN

        assertThat(revenuePerCategory.get("Books")).isEqualTo(244.49);
        assertThat(revenuePerCategory.get("Clothing")).isEqualTo(740.75);
    }

    @Test
    @DisplayName("🚨 TODO 4: groupingBy + averagingDouble downstream")
    public void groupingByAveraging() {
        List<User> users = OrderData.sampleUsers();

        // 🚨 TO DO: Average age per department
        Map<String, Double> avgAgeByDept = null; // ❌ BROKEN

        assertThat(avgAgeByDept.get("Engineering")).isGreaterThan(25.0);
        assertThat(avgAgeByDept.get("Finance")).isGreaterThan(30.0);
    }

    @Test
    @DisplayName("🚨 TODO 5: groupingBy + mapping downstream")
    public void groupingByMapping() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Get list of customer names per category
        Map<String, List<String>> namesByCategory = null; // ❌ BROKEN

        assertThat(namesByCategory.get("Books")).contains("Alice", "Charlie", "Frank", "Ivy");
        assertThat(namesByCategory.get("Electronics")).contains("John", "Bob");
    }

    @Test
    @DisplayName("🚨 TODO 6: groupingBy + maxBy downstream")
    public void groupingByMax() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Find the most expensive order per category
        Map<String, Optional<Order>> maxByCategory = null; // ❌ BROKEN

        assertThat(maxByCategory.get("Electronics")).isPresent();
        assertThat(maxByCategory.get("Electronics").get().getAmount()).isEqualTo(1200.0);
    }

    @Test
    @DisplayName("🚨 TODO 7: Multi-level groupingBy")
    public void multiLevelGroupingBy() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Group by category, then by status
        Map<String, Map<String, List<Order>>> byCategAndStatus = null; // ❌ BROKEN

        assertThat(byCategAndStatus).containsKey("Electronics");
        assertThat(byCategAndStatus.get("Electronics")).containsKey("PAID");
    }

    @Test
    @DisplayName("🚨 TODO 8: groupingBy with TreeMap (sorted keys)")
    public void groupingBySortedMap() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Group by category into a TreeMap (sorted keys)
        Map<String, Long> sortedMap = null; // ❌ BROKEN

        assertThat(new ArrayList<>(sortedMap.keySet()))
                .containsExactly("Books", "Clothing", "Electronics");

        // 💡 HINTS:
        // Collectors.groupingBy(Order::getCategory, TreeMap::new, Collectors.counting())
    }

    @Test
    @DisplayName("🚨 TODO 9: toMap with merge function")
    public void toMapWithMerge() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Create map of category -> total amount (handle duplicate keys with sum)
        Map<String, Double> categoryTotals = null; // ❌ BROKEN

        assertThat(categoryTotals).containsKey("Electronics");
        assertThat(categoryTotals.get("Books")).isEqualTo(244.49);
    }

    @Test
    @DisplayName("🚨 TODO 10: toMap id -> object")
    public void toMapIdToObject() {
        List<User> users = OrderData.sampleUsers();

        // 🚨 TO DO: Create a Map<Integer, User> keyed by user ID
        Map<Integer, User> userById = null; // ❌ BROKEN

        assertThat(userById).containsKey(1);
        assertThat(userById.get(1).getName()).isEqualTo("Alice");
        assertThat(userById).hasSize(10);
    }

    @Test
    @DisplayName("🚨 TODO 11: partitioningBy simple")
    public void partitioningBySimple() {
        List<Transaction> txns = OrderData.sampleTransactions();

        // 🚨 TO DO: Partition transactions into successful (true) and failed (false)
        Map<Boolean, List<Transaction>> partitioned = null; // ❌ BROKEN

        assertThat(partitioned.get(true)).hasSize(7);
        assertThat(partitioned.get(false)).hasSize(3);
    }

    @Test
    @DisplayName("🚨 TODO 12: partitioningBy + counting")
    public void partitioningByCount() {
        List<Product> products = OrderData.sampleProducts();

        // 🚨 TO DO: Count products above and below $100 price threshold
        Map<Boolean, Long> priceSplit = null; // ❌ BROKEN

        assertThat(priceSplit.get(true)).isEqualTo(3L);   // expensive (> 100)
        assertThat(priceSplit.get(false)).isEqualTo(7L);  // affordable (<= 100)
    }

    @Test
    @DisplayName("🚨 TODO 13: Collectors.counting()")
    public void counting() {
        List<User> users = OrderData.sampleUsers();

        // 🚨 TO DO: Count active users using Collectors.counting()
        long activeCount = 0; // ❌ BROKEN - use stream + filter + count OR groupingBy

        assertThat(activeCount).isEqualTo(8L);
    }

    @Test
    @DisplayName("🚨 TODO 14: Collectors.joining variants")
    public void joiningVariants() {
        List<User> users = OrderData.sampleUsers();

        // 🚨 TO DO: Join all user names with " | " separator, wrapped in << >>
        String nameList = ""; // ❌ BROKEN

        assertThat(nameList).startsWith("<<");
        assertThat(nameList).endsWith(">>");
        assertThat(nameList).contains(" | ");
        assertThat(nameList).contains("Alice").contains("Bob");
    }

    @Test
    @DisplayName("🚨 TODO 15: Collectors.summarizingInt")
    public void summarizingInt() {
        List<Product> products = OrderData.sampleProducts();

        // 🚨 TO DO: Get summary statistics for product stock quantities
        IntSummaryStatistics stockStats = null; // ❌ BROKEN

        assertThat(stockStats.getMin()).isEqualTo(5);
        assertThat(stockStats.getMax()).isEqualTo(200);
        assertThat(stockStats.getSum()).isEqualTo(605L);
    }

    @Test
    @DisplayName("🚨 TODO 16: groupingBy with joining downstream")
    public void groupingByJoining() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: For each category, join customer names with ", "
        Map<String, String> nameStringByCategory = null; // ❌ BROKEN

        assertThat(nameStringByCategory.get("Books")).contains("Alice");
        assertThat(nameStringByCategory.get("Books")).contains(",");
    }

    @Test
    @DisplayName("🚨 TODO 17: Collectors.toSet()")
    public void collectToSet() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Collect unique order statuses into a Set
        Set<String> statuses = null; // ❌ BROKEN

        assertThat(statuses).containsExactlyInAnyOrder("PAID", "PENDING", "CANCELED", "SHIPPED");
    }

    @Test
    @DisplayName("🚨 TODO 18: groupingBy users by department count")
    public void groupingByDeptCount() {
        List<User> users = OrderData.sampleUsers();

        // 🚨 TO DO: Map of department -> count of employees
        Map<String, Long> countByDept = null; // ❌ BROKEN

        assertThat(countByDept.get("Engineering")).isEqualTo(4L);
        assertThat(countByDept.get("HR")).isEqualTo(2L);
        assertThat(countByDept.get("Finance")).isEqualTo(2L);
    }

    @Test
    @DisplayName("🚨 TODO 19: Custom Collector - product of integers")
    public void customCollector() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        // 🚨 TO DO: Use Collectors.reducing() to calculate product (1*2*3*4*5)
        Optional<Integer> product = Optional.empty(); // ❌ BROKEN

        assertThat(product).isPresent();
        assertThat(product.get()).isEqualTo(120);

        // 💡 HINTS:
        // numbers.stream().collect(Collectors.reducing((a, b) -> a * b))
    }

    @Test
    @DisplayName("🚨 TODO 20: collectingAndThen to get first element")
    public void collectingAndThenFirst() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Collect paid orders sorted by amount, then get the most expensive
        Optional<Order> mostExpensivePaid = Optional.empty(); // ❌ BROKEN

        assertThat(mostExpensivePaid).isPresent();
        assertThat(mostExpensivePaid.get().getAmount()).isEqualTo(1200.0);

        // 💡 HINTS:
        // orders.stream()
        //     .filter(o -> "PAID".equals(o.getStatus()))
        //     .collect(Collectors.maxBy(Comparator.comparing(Order::getAmount)))
    }

    @Test
    @DisplayName("🚨 TODO 21: Grouping products by price range")
    public void groupingByPriceRange() {
        List<Product> products = OrderData.sampleProducts();

        // 🚨 TO DO: Group products: "budget" (<50), "mid" (50-500), "premium" (>500)
        Map<String, List<String>> grouped = null; // ❌ BROKEN

        assertThat(grouped.get("budget")).isNotEmpty();
        assertThat(grouped.get("premium")).contains("Laptop", "Phone");

        // 💡 HINTS:
        // Collectors.groupingBy(p -> {
        //     if (p.getPrice() < 50) return "budget";
        //     else if (p.getPrice() <= 500) return "mid";
        //     else return "premium";
        // }, Collectors.mapping(Product::getName, Collectors.toList()))
    }

    @Test
    @DisplayName("🚨 TODO 22: toMap with Function.identity()")
    public void toMapWithIdentity() {
        List<String> emails = Arrays.asList(
                "alice@corp.com", "bob@corp.com", "charlie@corp.com"
        );

        // 🚨 TO DO: Create Map<String, String> where key=full email, value=username (before @)
        Map<String, String> emailToUsername = null; // ❌ BROKEN

        assertThat(emailToUsername.get("alice@corp.com")).isEqualTo("alice");
        assertThat(emailToUsername.get("bob@corp.com")).isEqualTo("bob");
    }

    @Test
    @DisplayName("🚨 TODO 23: Counting by multiple criteria")
    public void countingByMultipleCriteria() {
        List<Transaction> txns = OrderData.sampleTransactions();

        // 🚨 TO DO: Group by currency, count transactions per currency
        Map<String, Long> countByCurrency = null; // ❌ BROKEN

        assertThat(countByCurrency.get("USD")).isEqualTo(4L);
        assertThat(countByCurrency.get("EUR")).isEqualTo(3L);
        assertThat(countByCurrency.get("GBP")).isEqualTo(2L);
    }

    @Test
    @DisplayName("🚨 TODO 24: groupingBy active users by department")
    public void groupActiveUsersByDept() {
        List<User> users = OrderData.sampleUsers();

        // 🚨 TO DO: Group ACTIVE users only, by department, collect their names
        Map<String, List<String>> activeNamesByDept = null; // ❌ BROKEN

        assertThat(activeNamesByDept.get("Engineering")).contains("Alice", "Bob", "Frank", "Ivy");
        assertThat(activeNamesByDept.get("Engineering")).doesNotContain("Charlie");
    }

    @Test
    @DisplayName("🚨 TODO 25: Flat-map collector - collect all tags across orders into a Set")
    public void flatMappingCollector() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Collect all unique tags across all orders into a single Set
        Set<String> allTags = null; // ❌ BROKEN

        assertThat(allTags).containsExactlyInAnyOrder("new", "vip", "repeat");

        // 💡 HINTS (Java 8 way):
        // orders.stream()
        //     .flatMap(o -> o.getTags().stream())
        //     .collect(Collectors.toSet())
    }
}
