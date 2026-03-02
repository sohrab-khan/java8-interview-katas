# 07 · CompletableFuture

> **When to read:** After attempting CompletableFuture katas, before checking solutions.

---

## Core Idea

`CompletableFuture<T>` is Java 8's answer to async programming. It represents a computation that will complete in the future, and lets you chain further steps, handle errors, and combine multiple futures — all without blocking threads.

```java
// Old way — blocks the calling thread
String result = slowService.call();   // thread sits here waiting

// CompletableFuture — non-blocking
CompletableFuture.supplyAsync(() -> slowService.call())
    .thenApply(String::toUpperCase)
    .thenAccept(System.out::println);
// calling thread is free immediately
```

---

## Creating a Future

```java
// supplyAsync — runs a Supplier in background, returns a value
CompletableFuture<String> f = CompletableFuture.supplyAsync(() -> fetchData());

// runAsync — runs a Runnable in background, returns Void
CompletableFuture<Void> f = CompletableFuture.runAsync(() -> sendEmail());

// completedFuture — already-done future (useful in tests / stubs)
CompletableFuture<String> f = CompletableFuture.completedFuture("cached result");

// With custom executor (avoids using the common ForkJoinPool)
ExecutorService pool = Executors.newFixedThreadPool(4);
CompletableFuture<String> f = CompletableFuture.supplyAsync(() -> fetch(), pool);
```

> **Default executor:** `supplyAsync` / `runAsync` without an executor use `ForkJoinPool.commonPool()`. For I/O-bound work, always supply a dedicated thread pool.

---

## Transforming Results

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "hello");

// thenApply — transform value (like Stream.map)
CompletableFuture<Integer> length = future.thenApply(String::length);

// Chain multiple thenApply
CompletableFuture<String> result = future
    .thenApply(String::trim)
    .thenApply(String::toUpperCase)
    .thenApply(s -> "Result: " + s);

// thenApplyAsync — runs transformation on a different thread
future.thenApplyAsync(String::toUpperCase);
future.thenApplyAsync(String::toUpperCase, myExecutor);
```

---

## Consuming Results

```java
// thenAccept — consume value, return CompletableFuture<Void>
future.thenAccept(value -> System.out.println("Got: " + value));

// thenRun — run action after completion, no access to value
future.thenRun(() -> System.out.println("Done"));
```

---

## Chaining Futures — `thenCompose`

Use `thenCompose` when the next step itself returns a `CompletableFuture` (like `flatMap` for streams).

```java
// thenApply here gives CompletableFuture<CompletableFuture<User>> ❌
CompletableFuture<User> wrong = future
    .thenApply(id -> fetchUserAsync(id));   // fetchUserAsync returns CF<User>

// thenCompose flattens it ✅
CompletableFuture<User> user = future
    .thenCompose(id -> fetchUserAsync(id));
```

---

## Combining Two Futures — `thenCombine`

```java
CompletableFuture<String> nameFuture = CompletableFuture.supplyAsync(() -> fetchName());
CompletableFuture<Integer> ageFuture  = CompletableFuture.supplyAsync(() -> fetchAge());

// Run both in parallel, combine when both done
CompletableFuture<String> combined = nameFuture.thenCombine(
    ageFuture,
    (name, age) -> name + " is " + age
);
```

---

## Error Handling

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    if (Math.random() > 0.5) throw new RuntimeException("failed");
    return "ok";
});

// exceptionally — recover from error, provide fallback value
CompletableFuture<String> safe = future
    .exceptionally(ex -> "fallback: " + ex.getMessage());

// handle — process BOTH success and failure
CompletableFuture<String> handled = future.handle((value, ex) -> {
    if (ex != null) return "Error: " + ex.getMessage();
    return value.toUpperCase();
});

// whenComplete — side effect on both outcomes, does NOT change value
future.whenComplete((value, ex) -> {
    if (ex != null) logger.error("Failed", ex);
    else            logger.info("Success: " + value);
});
```

**Key differences:**

| Method | Changes result? | Access to exception? |
|--------|----------------|---------------------|
| `exceptionally` | Yes (on error) | Yes (only error path) |
| `handle` | Yes (always) | Yes (both paths) |
| `whenComplete` | No | Yes (both paths) |

---

## Combining Multiple Futures

```java
CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "A");
CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "B");
CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> "C");

// allOf — wait for ALL to complete (returns CompletableFuture<Void>)
CompletableFuture<Void> all = CompletableFuture.allOf(f1, f2, f3);
all.join();   // blocks until all done

// Collect results after allOf
CompletableFuture<List<String>> results = CompletableFuture.allOf(f1, f2, f3)
    .thenApply(v -> Stream.of(f1, f2, f3)
        .map(CompletableFuture::join)
        .collect(Collectors.toList()));

// anyOf — completes as soon as ANY one finishes (returns CompletableFuture<Object>)
CompletableFuture<Object> first = CompletableFuture.anyOf(f1, f2, f3);
String winner = (String) first.join();
```

---

## Getting the Value

```java
// get() — blocks, throws checked exceptions
String value = future.get();
String value = future.get(5, TimeUnit.SECONDS);  // with timeout

// join() — blocks, throws unchecked CompletionException
String value = future.join();   // preferred in non-throws contexts

// getNow(default) — returns immediately: value if done, default if not
String value = future.getNow("not ready yet");

// isDone() — check without blocking
if (future.isDone()) { ... }
```

---

## Manual Completion

```java
CompletableFuture<String> promise = new CompletableFuture<>();

// In another thread / callback:
promise.complete("result");              // success
promise.completeExceptionally(new RuntimeException("oops"));  // failure

// Waiting side:
String value = promise.join();
```

Useful for bridging callback-based APIs into CompletableFuture.

---

## Parallel Fetch Pattern

```java
List<Long> orderIds = List.of(1L, 2L, 3L, 4L, 5L);

List<CompletableFuture<Order>> futures = orderIds.stream()
    .map(id -> CompletableFuture.supplyAsync(() -> fetchOrder(id), executor))
    .collect(Collectors.toList());

List<Order> orders = CompletableFuture.allOf(
        futures.toArray(new CompletableFuture[0]))
    .thenApply(v -> futures.stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList()))
    .join();
```

---

## `thenApply` vs `thenCompose` vs `thenCombine`

| Method | Input | Use when |
|--------|-------|----------|
| `thenApply(fn)` | `T → U` | Transform result synchronously |
| `thenCompose(fn)` | `T → CF<U>` | Next step is also async (flatMap) |
| `thenCombine(cf, fn)` | Two CFs `→ V` | Merge two independent futures |

---

## Common Interview Questions

| Question | Key point |
|----------|-----------|
| `thenApply` vs `thenCompose`? | `thenApply` = sync transform. `thenCompose` = next step is also a future (flatMap). |
| `handle` vs `exceptionally`? | `exceptionally` only on error. `handle` runs on both success and failure. |
| `allOf` vs `anyOf`? | `allOf` waits for all. `anyOf` returns when first completes. |
| `get()` vs `join()`? | `get()` throws checked exceptions. `join()` throws unchecked `CompletionException`. |
| Why use custom executor? | Default `ForkJoinPool` is for CPU work. I/O tasks need a separate thread pool. |

---

## Gotchas

```java
// ❌ allOf result is Void — can't get values directly from it
CompletableFuture.allOf(f1, f2).thenApply(v -> f1.join() + f2.join()); // OK, but ugly
// ✅ Build list before allOf, then join each after
List<CompletableFuture<String>> futures = ...;
CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
    .thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));

// ❌ Blocking inside thenApply on the common pool — starves other tasks
future.thenApply(v -> {
    Thread.sleep(5000);   // blocking on ForkJoinPool thread!
    return v;
});
// ✅ Use thenApplyAsync with a dedicated executor for blocking ops

// ❌ Swallowing exceptions silently
future.thenApply(v -> riskyOp(v));   // exception stored but ignored if nobody calls .get()
// ✅ Always attach .exceptionally() or .handle() on pipelines that may fail
```
