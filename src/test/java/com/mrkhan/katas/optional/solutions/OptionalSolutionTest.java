package com.mrkhan.katas.optional.solutions;

import com.mrkhan.katas.OrderData;
import com.mrkhan.katas.OrderData.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.*;
import java.util.function.*;

import static org.assertj.core.api.Assertions.*;

/**
 * ✅ OPTIONAL - SOLUTIONS
 */
@DisplayName("✅ Optional - Solutions")
public class OptionalSolutionTest {

    @Test @DisplayName("✅ SOLUTION 1: of vs ofNullable")
    public void ofVsOfNullable_solution() {
        String value = null;
        Optional<String> opt = Optional.ofNullable(value);
        assertThat(opt).isEmpty();

        String realValue = "Hello";
        Optional<String> opt2 = Optional.ofNullable(realValue);
        assertThat(opt2).isPresent();
        assertThat(opt2.get()).isEqualTo("Hello");
        // 💡 NEVER use Optional.of(null) → NullPointerException!
        // 💡 Use Optional.ofNullable() when value might be null
    }

    @Test @DisplayName("✅ SOLUTION 2: orElse")
    public void orElseDefault_solution() {
        Optional<String> empty = Optional.empty();
        Optional<String> withValue = Optional.of("Actual");

        String result1 = empty.orElse("Default");
        String result2 = withValue.orElse("Default");

        assertThat(result1).isEqualTo("Default");
        assertThat(result2).isEqualTo("Actual");
        // 💡 orElse ALWAYS evaluates the argument, even when value is present!
        // 💡 Use orElseGet for expensive defaults
    }

    @Test @DisplayName("✅ SOLUTION 3: orElseGet lazy")
    public void orElseGetLazy_solution() {
        List<String> log = new ArrayList<>();
        Optional<String> opt = Optional.empty();

        String result = opt.orElseGet(() -> {
            log.add("computed");
            return "computed-value";
        });

        assertThat(result).isEqualTo("computed-value");
        assertThat(log).contains("computed");

        log.clear();
        Optional<String> withValue = Optional.of("present");
        String result2 = withValue.orElseGet(() -> {
            log.add("computed");
            return "computed-value";
        });

        assertThat(result2).isEqualTo("present");
        assertThat(log).isEmpty();
        // 💡 KEY: orElseGet(() -> ...) is LAZY – only runs the supplier if empty
        // 💡 This is the MOST IMPORTANT difference vs orElse
    }

    @Test @DisplayName("✅ SOLUTION 4: orElseThrow")
    public void orElseThrowCustom_solution() {
        Optional<User> empty = Optional.empty();

        assertThatThrownBy(() -> {
            User user = empty.orElseThrow(() -> new IllegalStateException("User not found"));
        }).isInstanceOf(IllegalStateException.class)
          .hasMessage("User not found");
        // 💡 orElseThrow(Supplier<X>) – the supplier creates the exception
        // 💡 Java 10+: orElseThrow() without args throws NoSuchElementException
    }

    @Test @DisplayName("✅ SOLUTION 5: Optional.map chain")
    public void optionalMapChain_solution() {
        Optional<User> user = Optional.of(OrderData.sampleUsers().get(0));

        Optional<String> deptUpper = user
                .map(User::getDepartment)
                .map(String::toUpperCase);

        assertThat(deptUpper).isPresent();
        assertThat(deptUpper.get()).isEqualTo("ENGINEERING");
        // 💡 Each .map() returns a new Optional
        // 💡 If any step is null/empty, the whole chain returns empty
    }

    @Test @DisplayName("✅ SOLUTION 6: Optional.filter")
    public void optionalFilter_solution() {
        Optional<Integer> evenOnly = Optional.of(42).filter(n -> n % 2 == 0);
        assertThat(evenOnly).isPresent();

        Optional<Integer> filteredOdd = Optional.of(7).filter(n -> n % 2 == 0);
        assertThat(filteredOdd).isEmpty();
        // 💡 filter() returns empty if predicate is false
        // 💡 Useful for validation: opt.filter(validator).orElseThrow(...)
    }

    @Test @DisplayName("✅ SOLUTION 7: ifPresent consumer")
    public void ifPresentConsumer_solution() {
        List<String> log = new ArrayList<>();
        Optional<String> opt = Optional.of("Hello");

        opt.ifPresent(v -> log.add(v));

        assertThat(log).containsExactly("Hello");

        Optional<String> empty = Optional.empty();
        empty.ifPresent(v -> log.add(v));
        assertThat(log).hasSize(1);
        // 💡 ifPresent replaces: if (opt.isPresent()) { opt.get()... }
        // 💡 Java 9+: ifPresentOrElse(consumer, runnable) for else branch
    }

    @Test @DisplayName("✅ SOLUTION 8: Null-safe navigation")
    public void nullSafeNavigation_solution() {
        User nullUser = null;

        String dept = Optional.ofNullable(nullUser)
                .map(User::getDepartment)
                .map(String::toUpperCase)
                .orElse("UNKNOWN");

        assertThat(dept).isEqualTo("UNKNOWN");

        User realUser = OrderData.sampleUsers().get(0);
        String dept2 = Optional.ofNullable(realUser)
                .map(User::getDepartment)
                .map(String::toUpperCase)
                .orElse("UNKNOWN");

        assertThat(dept2).isEqualTo("ENGINEERING");
        // 💡 This replaces the pyramid of doom: if(u != null) { if(d != null) {...} }
    }

    @Test @DisplayName("✅ SOLUTION 9: flatMap avoids Optional<Optional<T>>")
    public void optionalFlatMap_solution() {
        Function<User, Optional<String>> getEmail = u -> Optional.ofNullable(u.getEmail());
        User user = OrderData.sampleUsers().get(0);

        Optional<String> email = Optional.of(user).flatMap(getEmail);

        assertThat(email).isPresent();
        assertThat(email.get()).isEqualTo("alice@corp.com");
        // 💡 .map(getEmail) returns Optional<Optional<String>> – wrong!
        // 💡 .flatMap(getEmail) returns Optional<String> – correct!
    }

    @Test @DisplayName("✅ SOLUTION 10: Find first active Engineering user")
    public void findFirstActiveEngineering_solution() {
        List<User> users = OrderData.sampleUsers();

        String name = users.stream()
                .filter(u -> "Engineering".equals(u.getDepartment()) && u.isActive())
                .findFirst()
                .map(User::getName)
                .orElse("No engineer found");

        assertThat(name).isEqualTo("Alice");
        // 💡 Stream.findFirst() returns Optional<T>
        // 💡 Chain .map().orElse() directly on the Optional
    }

    @Test @DisplayName("✅ SOLUTION 11: Optional in stream")
    public void optionalInStream_solution() {
        List<Optional<String>> optionals = Arrays.asList(
                Optional.of("Alice"), Optional.empty(), Optional.of("Bob"),
                Optional.empty(), Optional.of("Charlie")
        );

        List<String> present = optionals.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        assertThat(present).containsExactly("Alice", "Bob", "Charlie");
        // 💡 Java 9+: .flatMap(Optional::stream) is cleaner
        // 💡 Java 8: filter(isPresent) + map(get) pattern
    }

    @Test @DisplayName("✅ SOLUTION 12: Multiple fallbacks (Java 8)")
    public void optionalMultipleFallbacks_solution() {
        Optional<String> cache    = Optional.empty();
        Optional<String> database = Optional.empty();
        String hardcoded = "default-config";

        String result = cache.orElseGet(() -> database.orElse(hardcoded));

        assertThat(result).isEqualTo("default-config");

        Optional<String> cacheHit = Optional.of("cached-value");
        String result2 = cacheHit.orElseGet(() -> database.orElse(hardcoded));

        assertThat(result2).isEqualTo("cached-value");
        // 💡 Java 9 introduced Optional.or(Supplier<Optional>) – much cleaner
        // 💡 Java 8 workaround: nested orElse/orElseGet
    }

    @Test @DisplayName("✅ SOLUTION 13: Safe map lookup")
    public void optionalMapLookup_solution() {
        Map<String, User> userMap = new HashMap<>();
        OrderData.sampleUsers().forEach(u -> userMap.put(u.getName(), u));

        String email = Optional.ofNullable(userMap.get("NonExistent"))
                .map(User::getEmail)
                .orElse("unknown");
        assertThat(email).isEqualTo("unknown");

        String aliceEmail = Optional.ofNullable(userMap.get("Alice"))
                .map(User::getEmail)
                .orElse("unknown");
        assertThat(aliceEmail).isEqualTo("alice@corp.com");
        // 💡 Map.get() returns null for missing keys → wrap with ofNullable
    }

    @Test @DisplayName("✅ SOLUTION 14: ifPresent over isPresent anti-pattern")
    public void isPresentAntiPattern_solution() {
        Optional<String> opt = Optional.of("Important Value");
        List<String> processed = new ArrayList<>();

        opt.ifPresent(processed::add);

        assertThat(processed).containsExactly("Important Value");
        // 💡 Anti-pattern: if(opt.isPresent()) { process(opt.get()); }
        // 💡 Clean pattern: opt.ifPresent(this::process)
    }

    @Test @DisplayName("✅ SOLUTION 15: Validate and transform")
    public void validateAndTransform_solution() {
        Function<String, Optional<Integer>> parseAge = s -> {
            try {
                int age = Integer.parseInt(s);
                return age >= 0 && age <= 150 ? Optional.of(age) : Optional.empty();
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        };

        int age1 = parseAge.apply("25").orElse(-1);
        int age2 = parseAge.apply("abc").orElse(-1);
        int age3 = parseAge.apply("200").orElse(-1);

        assertThat(age1).isEqualTo(25);
        assertThat(age2).isEqualTo(-1);
        assertThat(age3).isEqualTo(-1);
        // 💡 Combining Optional with validation is elegant
        // 💡 Optional.empty() signals "no valid result" without exceptions
    }
}
