# 01 · Lambda Expressions & Functional Interfaces

> **When to read:** After attempting Lambda Basics katas, before checking solutions.

---

## Core Idea

A lambda is an anonymous function — no class, no name, just behaviour.
It can only be used where a **functional interface** (exactly one abstract method) is expected.

```java
// Before Java 8
Runnable r = new Runnable() {
    @Override public void run() { System.out.println("hello"); }
};

// Java 8
Runnable r = () -> System.out.println("hello");
```

---

## Lambda Syntax

```java
// No params
() -> System.out.println("hi")

// One param (parens optional)
s -> s.toUpperCase()
(s) -> s.toUpperCase()

// Multiple params
(a, b) -> a + b

// Block body (needs return)
(a, b) -> {
    int result = a + b;
    return result;
}

// With declared types (optional)
(String s, int n) -> s.repeat(n)   // String.repeat is Java 11 - just for illustration
```

---

## Built-in Functional Interfaces (`java.util.function`)

| Interface | Signature | Use case |
|-----------|-----------|----------|
| `Predicate<T>` | `T → boolean` | filter, test condition |
| `Function<T,R>` | `T → R` | transform/map |
| `Consumer<T>` | `T → void` | side effect (print, add to list) |
| `Supplier<T>` | `() → T` | lazy init, factory |
| `UnaryOperator<T>` | `T → T` | transform same type |
| `BinaryOperator<T>` | `(T,T) → T` | reduce, combine |
| `BiFunction<T,U,R>` | `(T,U) → R` | combine two inputs |
| `BiConsumer<T,U>` | `(T,U) → void` | Map.forEach |
| `BiPredicate<T,U>` | `(T,U) → boolean` | two-arg test |

```java
Predicate<String>         isEmpty   = String::isEmpty;
Function<String, Integer> length    = String::length;
Consumer<String>          print     = System.out::println;
Supplier<List<String>>    newList   = ArrayList::new;
UnaryOperator<Integer>    square    = x -> x * x;
BinaryOperator<Integer>   max       = Integer::max;
BiFunction<String,Integer,String> repeat = (s, n) -> s + n; // simplified
```

---

## Predicate Composition

```java
Predicate<Integer> isEven     = n -> n % 2 == 0;
Predicate<Integer> isPositive = n -> n > 0;

// AND
Predicate<Integer> evenAndPositive = isEven.and(isPositive);

// OR
Predicate<Integer> evenOrPositive  = isEven.or(isPositive);

// NOT
Predicate<Integer> isOdd           = isEven.negate();
```

> **Interview trap:** `Predicate.not(p)` is Java 11. In Java 8 use `p.negate()`.

---

## Function Composition

```java
Function<String, String>  trim   = String::trim;
Function<String, Integer> length = String::length;

// andThen: trim first, then length  →  length(trim(x))
Function<String, Integer> f1 = trim.andThen(length);

// compose: trim first, then length  →  trim(length... no — length(trim(x)))
// compose reverses the call order from the perspective of the receiver:
// length.compose(trim) = length(trim(x))
Function<String, Integer> f2 = length.compose(trim);

f1.apply("  hi  ") // 2  — same result
f2.apply("  hi  ") // 2  — same result
```

**Memory aid:**
- `f.andThen(g)` → read left to right: f, **then** g
- `f.compose(g)` → g runs first, then f (math notation: f∘g)

---

## Method References

Four types — all are shorthand for a lambda that calls exactly one method.

```java
// 1. Static method
Function<String, Integer> parse = Integer::parseInt;
// same as: s -> Integer.parseInt(s)

// 2. Instance method on a specific object
String prefix = "Hello";
Supplier<String> upper = prefix::toUpperCase;
// same as: () -> prefix.toUpperCase()

// 3. Instance method on arbitrary object of that type
Function<String, Integer> length = String::length;
// same as: s -> s.length()

BiPredicate<String,String> equals = String::equals;
// same as: (a, b) -> a.equals(b)

// 4. Constructor
Supplier<List<String>>       listFactory  = ArrayList::new;
Function<Integer, int[]>     arrayFactory = int[]::new;
```

---

## @FunctionalInterface

```java
@FunctionalInterface          // enforces SAM at compile time
public interface Transformer<T, R> {
    R transform(T input);     // single abstract method

    default Transformer<T, R> andLog() {   // default methods OK
        return input -> {
            R result = this.transform(input);
            System.out.println(input + " -> " + result);
            return result;
        };
    }
}

Transformer<String, Integer> length = String::length;
length.transform("hello"); // 5
```

---

## Variable Capture

```java
String prefix = "ID-";          // effectively final
list.stream()
    .map(id -> prefix + id)     // OK — captured
    .forEach(System.out::println);

// prefix = "X";                // ← would break the lambda above
```

- ✅ Instance variables — always accessible
- ✅ Static variables — always accessible
- ✅ Local variables that are **effectively final** (never reassigned)
- ❌ Local variables that are reassigned after declaration

**Workaround for mutable capture:**
```java
AtomicInteger count = new AtomicInteger();
list.forEach(item -> count.incrementAndGet()); // OK
```

---

## Comparator (default methods in practice)

```java
List<Person> people = ...;

// Single field
people.sort(Comparator.comparing(Person::getAge));

// Multiple fields
people.sort(
    Comparator.comparing(Person::getLastName)
              .thenComparing(Person::getFirstName)
);

// Reversed
people.sort(Comparator.comparing(Person::getSalary).reversed());

// Null-safe
people.sort(Comparator.comparing(Person::getMiddleName,
            Comparator.nullsLast(Comparator.naturalOrder())));
```

---

## Common Interview Questions

| Question | Key point |
|----------|-----------|
| What is a functional interface? | Exactly one abstract method (SAM). Default/static methods are allowed. |
| Why `@FunctionalInterface`? | Optional annotation — but enforces SAM at compile time and documents intent. |
| `andThen` vs `compose`? | `f.andThen(g)` = g after f. `f.compose(g)` = f after g. |
| Can lambdas modify local variables? | No. They can only capture effectively final variables. |
| `Predicate.or()` vs `||`? | `or()` composes two Predicates into one reusable Predicate object. |
| When to use method reference? | When the lambda body does nothing except call a single existing method. |

---

## Gotchas

```java
// ❌ This compiles but evaluates expensiveDefault() ALWAYS
opt.orElse(expensiveDefault());

// ✅ Lazy — only called when needed
opt.orElseGet(() -> expensiveDefault());

// ❌ Ambiguous — Predicate<String> or Function<String,Boolean>?
// method(s -> s.isEmpty());  // needs context

// ❌ Can't throw checked exceptions in lambdas directly
list.forEach(f -> processFile(f));      // if processFile throws IOException
// Wrap in try-catch or use a helper method that rethrows as unchecked
```
