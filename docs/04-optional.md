# 04 · Optional\<T\>

> **When to read:** After attempting Optional katas, before checking solutions.

---

## Core Idea

`Optional<T>` is a container that either holds a value or is empty. Its purpose is to make the **absence of a value explicit** in the API, eliminating silent `null` returns and the `NullPointerException` that follows.

```java
// Without Optional — caller might forget null check
public User findUser(Long id) {
    return database.findById(id); // could return null
}

// With Optional — absence is part of the contract
public Optional<User> findUser(Long id) {
    return Optional.ofNullable(database.findById(id));
}
```

---

## Creating Optional

```java
Optional<String> present  = Optional.of("hello");         // value must not be null
Optional<String> empty    = Optional.empty();              // explicitly empty
Optional<String> nullable = Optional.ofNullable(value);   // null → empty, non-null → present

// ❌ Never do this
Optional.of(null);   // throws NullPointerException immediately
```

---

## Checking & Consuming

```java
Optional<String> opt = Optional.ofNullable(getValue());

// Check presence
opt.isPresent()   // true if value present
opt.isEmpty()     // true if empty — NOTE: isEmpty() is Java 11

// Execute if present
opt.ifPresent(value -> System.out.println(value));
opt.ifPresent(System.out::println);   // method reference
```

---

## Getting the Value

```java
// ❌ Dangerous — throws NoSuchElementException if empty
String value = opt.get();

// ✅ With default
String value = opt.orElse("default");

// ✅ Lazy default (only evaluated if empty)
String value = opt.orElseGet(() -> computeDefault());

// ✅ Throw custom exception if empty
String value = opt.orElseThrow(() -> new UserNotFoundException(id));

// ✅ Java 10+: no-arg orElseThrow (throws NoSuchElementException)
String value = opt.orElseThrow();
```

### `orElse` vs `orElseGet` — Critical Difference

```java
// orElse: ALWAYS evaluates the argument — even if Optional has a value
opt.orElse(expensiveDbCall());       // expensiveDbCall() called EVERY TIME

// orElseGet: LAZY — only evaluates supplier if Optional is empty
opt.orElseGet(() -> expensiveDbCall()); // called ONLY when opt is empty
```

> **Interview trap:** Using `orElse(expensiveOp())` when value is usually present wastes cycles. Always prefer `orElseGet` for non-trivial defaults.

---

## Transforming Optional

```java
Optional<User> user = findUser(id);

// map — transform value if present (wraps result in Optional)
Optional<String> name    = user.map(User::getName);
Optional<Integer> nameLen = user.map(User::getName).map(String::length);

// filter — keep value only if predicate passes
Optional<User> adult = user.filter(u -> u.getAge() >= 18);

// flatMap — when the mapping function itself returns Optional
// (avoids Optional<Optional<T>>)
Optional<Address> address = user.flatMap(User::getAddress);
//  compare: user.map(User::getAddress) → Optional<Optional<Address>>  ❌
```

### Null-Safe Navigation Chain

```java
// Old way — pyramid of doom
String city = "Unknown";
if (user != null) {
    Address address = user.getAddress();
    if (address != null) {
        city = address.getCity();
    }
}

// Optional way
String city = Optional.ofNullable(user)
    .map(User::getAddress)
    .map(Address::getCity)
    .orElse("Unknown");
```

---

## Multiple Fallbacks (Java 8)

`Optional.or()` is Java 9+. In Java 8, chain with `map` and `orElseGet`:

```java
// Java 9+
Optional<User> user = findInCache(id)
    .or(() -> findInDatabase(id))
    .or(() -> findInBackup(id));

// Java 8 equivalent
Optional<User> user = findInCache(id);
if (!user.isPresent()) user = findInDatabase(id);
if (!user.isPresent()) user = findInBackup(id);
```

---

## Optional in Streams

```java
List<Optional<String>> opts = Arrays.asList(
    Optional.of("a"), Optional.empty(), Optional.of("b")
);

// Java 8: filter then map
List<String> values = opts.stream()
    .filter(Optional::isPresent)
    .map(Optional::get)
    .collect(Collectors.toList()); // ["a", "b"]

// Java 9+: flatMap(Optional::stream) — cleaner
List<String> values = opts.stream()
    .flatMap(Optional::stream)
    .collect(Collectors.toList());
```

---

## Rules: When to Use Optional

✅ **Use as method return type** when result might not exist:
```java
public Optional<User> findByEmail(String email) { ... }
```

✅ **Chain transformations** to avoid null checks:
```java
findUser(id).map(User::getProfile).map(Profile::getBio).orElse("");
```

❌ **Do NOT use as field** — serialization problems, null remains:
```java
class User {
    private Optional<String> middleName; // NO
    private String middleName;           // YES (can be null)

    public Optional<String> getMiddleName() {  // expose via Optional getter
        return Optional.ofNullable(middleName);
    }
}
```

❌ **Do NOT use for collections** — return empty collection instead:
```java
public Optional<List<Order>> getOrders() { ... }  // NO
public List<Order> getOrders() {                  // YES
    return Collections.emptyList();
}
```

❌ **Do NOT use as method parameter** — use overloading or nullable:
```java
void process(Optional<String> name) { ... }  // NO
void process(String name)           { ... }  // YES
```

❌ **Do NOT use `isPresent()` + `get()`** — defeats the purpose:
```java
// Bad
if (opt.isPresent()) {
    System.out.println(opt.get());
}

// Good
opt.ifPresent(System.out::println);
```

---

## Common Interview Questions

| Question | Key point |
|----------|-----------|
| Why Optional? | Explicit API contract for absence. Eliminates silent null returns. |
| `orElse` vs `orElseGet`? | `orElse` always evaluates. `orElseGet` is lazy — only evaluates if empty. |
| `map` vs `flatMap` on Optional? | `map` wraps result. `flatMap` when mapping fn returns `Optional` (avoids nested Optional). |
| Can Optional be serialized? | Not reliably — not `Serializable`. Don't use as field in JPA entities or DTOs. |
| Optional vs null check? | For fields/parameters, a null check is simpler. Optional shines for return types and chaining. |

---

## Gotchas

```java
// ❌ Calling get() without checking
String value = opt.get();   // NoSuchElementException if empty

// ❌ Using Optional just to null-check a local variable
if (Optional.ofNullable(value).isPresent()) { }   // pointless — just use: if (value != null)

// ❌ Nested Optional (usually means flatMap was needed)
Optional<Optional<Address>> addr = user.map(User::getAddress);  // use flatMap

// ❌ orElse with side effects
opt.orElse(sideEffectingMethod());  // side effect runs even when value present!

// ✅
opt.orElseGet(() -> sideEffectingMethod());
```
