package com.mrkhan.katas.optional.todo;

import com.mrkhan.katas.OrderData;
import com.mrkhan.katas.OrderData.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.*;

import static org.assertj.core.api.Assertions.*;

/**
 * 🎯 OPTIONAL KATAS (15 katas)
 * ─────────────────────────────
 * Master Optional to eliminate NullPointerException.
 * Run: ./mvnw test -Dtest="OptionalTodoTest"
 */
@DisplayName("🚨 Optional - TODO (Fix me!)")
public class OptionalTodoTest {

    @Test
    @DisplayName("🚨 TODO 1: Optional.of vs ofNullable")
    public void ofVsOfNullable() {
        String value = null;

        // 🚨 TO DO: Create Optional safely (value might be null)
        Optional<String> opt = Optional.empty(); // ❌ BROKEN - use ofNullable

        assertThat(opt).isEmpty();

        // Now with a real value:
        String realValue = "Hello";
        Optional<String> opt2 = Optional.empty(); // ❌ BROKEN - use ofNullable

        assertThat(opt2).isPresent();
        assertThat(opt2.get()).isEqualTo("Hello");
    }

    @Test
    @DisplayName("🚨 TODO 2: orElse default value")
    public void orElseDefault() {
        Optional<String> empty = Optional.empty();
        Optional<String> withValue = Optional.of("Actual");

        // 🚨 TO DO: Get value or return "Default"
        String result1 = null; // ❌ BROKEN
        String result2 = null; // ❌ BROKEN

        assertThat(result1).isEqualTo("Default");
        assertThat(result2).isEqualTo("Actual");
    }

    @Test
    @DisplayName("🚨 TODO 3: orElseGet lazy evaluation")
    public void orElseGetLazy() {
        List<String> log = new ArrayList<>();

        Optional<String> opt = Optional.empty();

        // 🚨 TO DO: Use orElseGet with a supplier that logs "computed" to the log list
        String result = null; // ❌ BROKEN

        assertThat(result).isEqualTo("computed-value");
        assertThat(log).contains("computed");

        // Now verify the supplier is NOT called when value is present:
        log.clear();
        Optional<String> withValue = Optional.of("present");
        String result2 = withValue.orElseGet(() -> {
            log.add("computed");
            return "computed-value";
        });

        assertThat(result2).isEqualTo("present");
        assertThat(log).isEmpty(); // supplier NOT called
    }

    @Test
    @DisplayName("🚨 TODO 4: orElseThrow custom exception")
    public void orElseThrowCustom() {
        Optional<User> empty = Optional.empty();

        // 🚨 TO DO: Throw IllegalStateException with message "User not found"
        assertThatThrownBy(() -> {
            User user = empty.orElseThrow(null); // ❌ BROKEN - replace null with supplier
        }).isInstanceOf(IllegalStateException.class)
          .hasMessage("User not found");
    }

    @Test
    @DisplayName("🚨 TODO 5: Optional.map chain")
    public void optionalMapChain() {
        Optional<User> user = Optional.of(OrderData.sampleUsers().get(0)); // Alice

        // 🚨 TO DO: Extract department name in uppercase from optional user
        Optional<String> deptUpper = Optional.empty(); // ❌ BROKEN

        assertThat(deptUpper).isPresent();
        assertThat(deptUpper.get()).isEqualTo("ENGINEERING");

        // 💡 HINTS:
        // user.map(User::getDepartment).map(String::toUpperCase)
    }

    @Test
    @DisplayName("🚨 TODO 6: Optional.filter")
    public void optionalFilter() {
        Optional<Integer> number = Optional.of(42);

        // 🚨 TO DO: Filter - keep only if even
        Optional<Integer> evenOnly = Optional.empty(); // ❌ BROKEN

        assertThat(evenOnly).isPresent();
        assertThat(evenOnly.get()).isEqualTo(42);

        Optional<Integer> odd = Optional.of(7);
        Optional<Integer> filteredOdd = Optional.empty(); // ❌ BROKEN - filter odd

        assertThat(filteredOdd).isEmpty();

        // 💡 HINTS:
        // number.filter(n -> n % 2 == 0)
    }

    @Test
    @DisplayName("🚨 TODO 7: ifPresent consumer")
    public void ifPresentConsumer() {
        List<String> log = new ArrayList<>();
        Optional<String> opt = Optional.of("Hello");
        Optional<String> empty = Optional.empty();

        // 🚨 TO DO: If present, add value to log
        // opt.ifPresent(...)  ❌ BROKEN - add the consumer

        assertThat(log).containsExactly("Hello");

        // ifPresent on empty should not add anything
        empty.ifPresent(v -> log.add(v));
        assertThat(log).hasSize(1); // still only 1 element
    }

    @Test
    @DisplayName("🚨 TODO 8: Null-safe navigation chain")
    public void nullSafeNavigation() {
        // Simulate database lookup returning null
        User nullUser = null;

        // 🚨 TO DO: Safely navigate null user -> department -> uppercase
        // No NullPointerException!
        String dept = null; // ❌ BROKEN - use Optional chain

        assertThat(dept).isEqualTo("UNKNOWN");

        // Real user case:
        User realUser = OrderData.sampleUsers().get(0); // Alice, Engineering
        String dept2 = null; // ❌ BROKEN - use Optional chain

        assertThat(dept2).isEqualTo("ENGINEERING");

        // 💡 HINTS:
        // Optional.ofNullable(user).map(User::getDepartment).map(String::toUpperCase).orElse("UNKNOWN")
    }

    @Test
    @DisplayName("🚨 TODO 9: Optional flatMap to avoid Optional<Optional<T>>")
    public void optionalFlatMap() {
        // Method that returns Optional<String>
        java.util.function.Function<User, Optional<String>> getEmail =
                u -> Optional.ofNullable(u.getEmail());

        User user = OrderData.sampleUsers().get(0);

        // 🚨 TO DO: Use flatMap to get Optional<String> (not Optional<Optional<String>>)
        Optional<String> email = Optional.empty(); // ❌ BROKEN

        assertThat(email).isPresent();
        assertThat(email.get()).isEqualTo("alice@corp.com");

        // 💡 HINTS:
        // Optional.of(user).flatMap(getEmail)
        // NOTE: if you use .map(getEmail) you get Optional<Optional<String>>!
    }

    @Test
    @DisplayName("🚨 TODO 10: Find first active user in Engineering")
    public void findFirstActiveEngineering() {
        List<User> users = OrderData.sampleUsers();

        // 🚨 TO DO: Find first active Engineering user name, or "No engineer found"
        String name = ""; // ❌ BROKEN

        assertThat(name).isEqualTo("Alice");

        // 💡 HINTS:
        // users.stream()
        //     .filter(u -> "Engineering".equals(u.getDepartment()) && u.isActive())
        //     .findFirst()
        //     .map(User::getName)
        //     .orElse("No engineer found")
    }

    @Test
    @DisplayName("🚨 TODO 11: Optional in stream - filter empty")
    public void optionalInStream() {
        List<Optional<String>> optionals = Arrays.asList(
                Optional.of("Alice"),
                Optional.empty(),
                Optional.of("Bob"),
                Optional.empty(),
                Optional.of("Charlie")
        );

        // 🚨 TO DO: Extract present values into a list (Java 8 way)
        List<String> present = null; // ❌ BROKEN

        assertThat(present).containsExactly("Alice", "Bob", "Charlie");

        // 💡 HINTS:
        // optionals.stream()
        //     .filter(Optional::isPresent)
        //     .map(Optional::get)
        //     .collect(Collectors.toList())
    }

    @Test
    @DisplayName("🚨 TODO 12: Optional with multiple fallbacks")
    public void optionalMultipleFallbacks() {
        // Simulating cache miss, then DB miss, then default
        Optional<String> cache = Optional.empty();
        Optional<String> database = Optional.empty();
        String hardcoded = "default-config";

        // 🚨 TO DO: Return first non-empty, fall back to hardcoded
        // Java 8 way (no Optional.or() which is Java 9)
        String result = ""; // ❌ BROKEN

        assertThat(result).isEqualTo("default-config");

        // Now with cache hit:
        Optional<String> cacheHit = Optional.of("cached-value");
        String result2 = ""; // ❌ BROKEN - should return cached-value

        assertThat(result2).isEqualTo("cached-value");

        // 💡 HINTS (Java 8):
        // cache.map(v -> v).orElse(database.orElse(hardcoded))
        // OR: cacheHit.orElseGet(() -> database.orElse(hardcoded))
    }

    @Test
    @DisplayName("🚨 TODO 13: Optional to prevent NPE in map lookups")
    public void optionalMapLookup() {
        Map<String, User> userMap = new HashMap<>();
        OrderData.sampleUsers().forEach(u -> userMap.put(u.getName(), u));

        // 🚨 TO DO: Safely get user email - return "unknown" if user not found
        String email = ""; // ❌ BROKEN - safe lookup for "NonExistent"

        assertThat(email).isEqualTo("unknown");

        String aliceEmail = ""; // ❌ BROKEN - safe lookup for "Alice"

        assertThat(aliceEmail).isEqualTo("alice@corp.com");

        // 💡 HINTS:
        // Optional.ofNullable(userMap.get("NonExistent")).map(User::getEmail).orElse("unknown")
    }

    @Test
    @DisplayName("🚨 TODO 14: isPresent anti-pattern → ifPresent")
    public void isPresentAntiPattern() {
        Optional<String> opt = Optional.of("Important Value");
        List<String> processed = new ArrayList<>();

        // 🚨 TO DO: Replace the anti-pattern below with ifPresent
        // ANTI-PATTERN (don't do this):
        // if (opt.isPresent()) { processed.add(opt.get()); }

        // 💡 TO DO: Use opt.ifPresent(...) to add to processed

        assertThat(processed).containsExactly("Important Value");
    }

    @Test
    @DisplayName("🚨 TODO 15: Validate and transform with Optional")
    public void validateAndTransform() {
        java.util.function.Function<String, Optional<Integer>> parseAge = s -> {
            try {
                int age = Integer.parseInt(s);
                return age >= 0 && age <= 150 ? Optional.of(age) : Optional.empty();
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        };

        // 🚨 TO DO: Parse "25" and return age, or -1 if invalid
        int age1 = 0; // ❌ BROKEN - should return 25

        // 🚨 TO DO: Parse "abc" and return -1 (invalid)
        int age2 = 0; // ❌ BROKEN - should return -1

        // 🚨 TO DO: Parse "200" and return -1 (out of range)
        int age3 = 0; // ❌ BROKEN - should return -1

        assertThat(age1).isEqualTo(25);
        assertThat(age2).isEqualTo(-1);
        assertThat(age3).isEqualTo(-1);

        // 💡 HINTS:
        // parseAge.apply("25").orElse(-1)
    }
}
