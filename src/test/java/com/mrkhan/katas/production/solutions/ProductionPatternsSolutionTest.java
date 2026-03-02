package com.mrkhan.katas.production.solutions;

import com.mrkhan.katas.OrderData;
import com.mrkhan.katas.OrderData.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static org.assertj.core.api.Assertions.*;

/**
 * ✅ PRODUCTION PATTERNS - SOLUTIONS
 */
@DisplayName("✅ Production Patterns - Solutions")
public class ProductionPatternsSolutionTest {

    @Test @DisplayName("✅ SOLUTION 1: Paginated stream processing")
    public void paginatedStreamProcessing_solution() {
        List<Order> allOrders = OrderData.sampleOrders();

        Function<Integer, List<Order>> getPage = pageNum -> allOrders.stream()
                .skip((long) pageNum * 3)
                .limit(3)
                .collect(Collectors.toList());

        assertThat(getPage.apply(0)).hasSize(3);
        assertThat(getPage.apply(1)).hasSize(3);
        assertThat(getPage.apply(0).get(0).getId()).isEqualTo(1);
        assertThat(getPage.apply(1).get(0).getId()).isEqualTo(4);
        // 💡 skip(page * size).limit(size) is the universal pagination pattern
    }

    @Test @DisplayName("✅ SOLUTION 2: Validation chain")
    public void validationChain_solution() {
        Predicate<String> notEmpty   = s -> s != null && !s.trim().isEmpty();
        Predicate<String> isEmail    = s -> s.contains("@") && s.contains(".");
        Predicate<String> notTooLong = s -> s.length() <= 50;

        Predicate<String> emailValidator = notEmpty.and(isEmail).and(notTooLong);

        assertThat(emailValidator.test("alice@corp.com")).isTrue();
        assertThat(emailValidator.test("")).isFalse();
        assertThat(emailValidator.test("not-an-email")).isFalse();
        assertThat(emailValidator.test(new String(new char[60]).replace("\0", "a") + "@b.com")).isFalse();
        // 💡 Composing predicates = Strategy pattern done functionally
        // 💡 Each predicate is independently testable
    }

    @Test @DisplayName("✅ SOLUTION 3: Indexed lookup")
    public void buildIndexedLookup_solution() {
        List<User> users = OrderData.sampleUsers();

        Map<String, User> emailIndex = users.stream()
                .collect(Collectors.toMap(User::getEmail, Function.identity()));

        assertThat(emailIndex.get("alice@corp.com").getName()).isEqualTo("Alice");
        assertThat(emailIndex.get("nobody@x.com")).isNull();
        // 💡 Building indexes for O(1) lookup is a key production pattern
        // 💡 Much better than filtering a list repeatedly in loops
    }

    @Test @DisplayName("✅ SOLUTION 4: Batch processing")
    public void batchProcessing_solution() {
        List<Integer> ids = IntStream.rangeClosed(1, 25).boxed().collect(Collectors.toList());
        int batchSize = 10;

        List<List<Integer>> batches = IntStream.range(0, (ids.size() + batchSize - 1) / batchSize)
                .mapToObj(i -> ids.subList(i * batchSize, Math.min((i + 1) * batchSize, ids.size())))
                .collect(Collectors.toList());

        assertThat(batches).hasSize(3);
        assertThat(batches.get(0)).hasSize(10);
        assertThat(batches.get(2)).hasSize(5);
        // 💡 (size + batchSize - 1) / batchSize = ceiling division trick
        // 💡 Math.min handles the last batch (remainder)
    }

    @Test @DisplayName("✅ SOLUTION 5: Summary report")
    public void summaryReport_solution() {
        List<Order> orders = OrderData.sampleOrders();

        Map<String, Object> report = new HashMap<>();
        report.put("totalOrders",  orders.stream().count());
        report.put("totalRevenue", orders.stream().mapToDouble(Order::getAmount).sum());
        report.put("paidCount",    orders.stream().filter(o -> "PAID".equals(o.getStatus())).count());
        report.put("avgAmount",    orders.stream().mapToDouble(Order::getAmount).average().orElse(0));

        assertThat(report.get("totalOrders")).isEqualTo(10L);
        assertThat(report.get("paidCount")).isEqualTo(5L);
        assertThat((Double) report.get("totalRevenue")).isEqualTo(4061.25);
        // 💡 Multiple stream passes for clarity. Could combine with summarizingDouble.
        // 💡 In production, a DTO/record (Java 16+) is cleaner than Map<String, Object>
    }
}
