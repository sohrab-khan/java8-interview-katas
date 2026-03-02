package com.mrkhan.katas.production.todo;

import com.mrkhan.katas.OrderData;
import com.mrkhan.katas.OrderData.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static org.assertj.core.api.Assertions.*;

/**
 * 🎯 PRODUCTION PATTERNS KATAS (5 katas)
 * ─────────────────────────────────────────
 * Real-world Java 8 patterns used in production code.
 * Run: ./mvnw test -Dtest="ProductionPatternsTodoTest"
 */
@DisplayName("🚨 Production Patterns - TODO (Fix me!)")
public class ProductionPatternsTodoTest {

    @Test
    @DisplayName("🚨 TODO 1: Paginated stream processing")
    public void paginatedStreamProcessing() {
        List<Order> allOrders = OrderData.sampleOrders();

        // 🚨 TO DO: Implement a paginate helper
        // paginate(list, pageNumber, pageSize) → List<T>
        Function<Integer, List<Order>> getPage = null; // ❌ BROKEN

        List<Order> page0 = getPage.apply(0);
        List<Order> page1 = getPage.apply(1);
        List<Order> page2 = getPage.apply(2);

        assertThat(page0).hasSize(3);
        assertThat(page1).hasSize(3);
        assertThat(page2).hasSize(3);
        assertThat(page0.get(0).getId()).isEqualTo(1);
        assertThat(page1.get(0).getId()).isEqualTo(4);

        // 💡 HINTS:
        // getPage = pageNum -> allOrders.stream()
        //     .skip((long) pageNum * 3)
        //     .limit(3)
        //     .collect(Collectors.toList());
    }

    @Test
    @DisplayName("🚨 TODO 2: Validation chain with functional style")
    public void validationChain() {
        // Validation pipeline
        Predicate<String> notEmpty   = s -> s != null && !s.trim().isEmpty();
        Predicate<String> isEmail    = s -> s.contains("@") && s.contains(".");
        Predicate<String> notTooLong = s -> s.length() <= 50;

        // 🚨 TO DO: Combine all validators into one
        Predicate<String> emailValidator = null; // ❌ BROKEN

        assertThat(emailValidator.test("alice@corp.com")).isTrue();
        assertThat(emailValidator.test("")).isFalse();
        assertThat(emailValidator.test("not-an-email")).isFalse();
        assertThat(emailValidator.test(new String(new char[60]).replace("\0", "a") + "@b.com")).isFalse();
    }

    @Test
    @DisplayName("🚨 TODO 3: Build an index Map<String, User> with memoization pattern")
    public void buildIndexedLookup() {
        List<User> users = OrderData.sampleUsers();

        // 🚨 TO DO: Build an index: email -> User for O(1) lookups
        Map<String, User> emailIndex = null; // ❌ BROKEN

        User alice = emailIndex.get("alice@corp.com");
        assertThat(alice).isNotNull();
        assertThat(alice.getName()).isEqualTo("Alice");

        User nonExistent = emailIndex.get("nobody@x.com");
        assertThat(nonExistent).isNull();
    }

    @Test
    @DisplayName("🚨 TODO 4: Batch processing with partition")
    public void batchProcessing() {
        List<Integer> ids = IntStream.rangeClosed(1, 25).boxed().collect(Collectors.toList());
        int batchSize = 10;

        // 🚨 TO DO: Split ids into batches of batchSize
        List<List<Integer>> batches = null; // ❌ BROKEN

        assertThat(batches).hasSize(3);
        assertThat(batches.get(0)).hasSize(10);
        assertThat(batches.get(1)).hasSize(10);
        assertThat(batches.get(2)).hasSize(5); // remainder

        // 💡 HINTS:
        // IntStream.range(0, (ids.size() + batchSize - 1) / batchSize)
        //     .mapToObj(i -> ids.subList(i * batchSize, Math.min((i + 1) * batchSize, ids.size())))
        //     .collect(Collectors.toList())
    }

    @Test
    @DisplayName("🚨 TODO 5: Summary report as Map<String, Object>")
    public void summaryReport() {
        List<Order> orders = OrderData.sampleOrders();

        // 🚨 TO DO: Build a summary report map:
        // "totalOrders"  -> 10L
        // "totalRevenue" -> sum of all amounts
        // "paidCount"    -> count of PAID orders
        // "avgAmount"    -> average amount
        Map<String, Object> report = null; // ❌ BROKEN

        assertThat(report).containsKey("totalOrders");
        assertThat(report.get("totalOrders")).isEqualTo(10L);
        assertThat(report.get("paidCount")).isEqualTo(5L);
        assertThat((Double) report.get("totalRevenue")).isEqualTo(4061.25);
        assertThat((Double) report.get("avgAmount")).isGreaterThan(400.0);

        // 💡 HINTS:
        // Map<String, Object> report = new HashMap<>();
        // report.put("totalOrders", orders.stream().count());
        // report.put("totalRevenue", orders.stream().mapToDouble(Order::getAmount).sum());
        // report.put("paidCount", orders.stream().filter(o -> "PAID".equals(o.getStatus())).count());
        // report.put("avgAmount", orders.stream().mapToDouble(Order::getAmount).average().orElse(0));
    }
}
