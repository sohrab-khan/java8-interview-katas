package com.mrkhan.katas.completablefuture.todo;

import com.mrkhan.katas.OrderData;
import com.mrkhan.katas.OrderData.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

import static org.assertj.core.api.Assertions.*;

/**
 * 🎯 COMPLETABLEFUTURE KATAS (20 katas)
 * ─────────────────────────────────────
 * Master async programming with CompletableFuture (Java 8).
 * Run: ./mvnw test -Dtest="CompletableFutureTodoTest"
 */
@DisplayName("🚨 CompletableFuture - TODO (Fix me!)")
public class CompletableFutureTodoTest {

    // ─── CREATION ───────────────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 1: supplyAsync basic")
    public void supplyAsyncBasic() throws Exception {
        // 🚨 TO DO: Create async computation that returns "Hello, Async!"
        CompletableFuture<String> future = null; // ❌ BROKEN

        assertThat(future.get()).isEqualTo("Hello, Async!");

        // 💡 HINTS:
        // CompletableFuture.supplyAsync(() -> "Hello, Async!")
    }

    @Test
    @DisplayName("🚨 TODO 2: runAsync for fire-and-forget")
    public void runAsyncFireAndForget() throws Exception {
        List<String> log = Collections.synchronizedList(new ArrayList<>());

        // 🚨 TO DO: Run async task that adds "done" to log
        CompletableFuture<Void> future = null; // ❌ BROKEN

        future.get();
        assertThat(log).contains("done");

        // 💡 HINTS:
        // CompletableFuture.runAsync(() -> log.add("done"))
    }

    @Test
    @DisplayName("🚨 TODO 3: completedFuture already done")
    public void completedFuture() throws Exception {
        // 🚨 TO DO: Create an already-completed future with value 42
        CompletableFuture<Integer> future = null; // ❌ BROKEN

        assertThat(future.isDone()).isTrue();
        assertThat(future.get()).isEqualTo(42);

        // 💡 HINTS:
        // CompletableFuture.completedFuture(42)
    }

    // ─── CHAINING ───────────────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 4: thenApply transformation")
    public void thenApplyTransform() throws Exception {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "hello");

        // 🚨 TO DO: Chain thenApply to uppercase the result
        CompletableFuture<String> upper = null; // ❌ BROKEN

        assertThat(upper.get()).isEqualTo("HELLO");

        // 💡 HINTS:
        // future.thenApply(String::toUpperCase)
    }

    @Test
    @DisplayName("🚨 TODO 5: thenApply chain (multiple)")
    public void thenApplyChain() throws Exception {
        CompletableFuture<String> base = CompletableFuture
                .supplyAsync(() -> "  hello world  ");

        // 🚨 TO DO: chain thenApply to trim, then thenApply to uppercase
        CompletableFuture<String> result = null; // ❌ BROKEN

        assertThat(result.get()).isEqualTo("HELLO WORLD");

        // 💡 HINTS:
        // base.thenApply(String::trim).thenApply(String::toUpperCase)
    }

    @Test
    @DisplayName("🚨 TODO 6: thenAccept terminal consumer")
    public void thenAcceptConsumer() throws Exception {
        List<String> results = Collections.synchronizedList(new ArrayList<>());

        CompletableFuture<String> fetched = CompletableFuture
                .supplyAsync(() -> "Order processed");

        // 🚨 TO DO: chain thenAccept on 'fetched' to add result to 'results' list
        CompletableFuture<Void> future = null; // ❌ BROKEN

        future.get();
        assertThat(results).contains("Order processed");

        // 💡 HINTS:
        // fetched.thenAccept(results::add)
    }

    @Test
    @DisplayName("🚨 TODO 7: thenCompose (flatMap for futures)")
    public void thenComposeChain() throws Exception {
        CompletableFuture<User> userFuture = CompletableFuture
                .supplyAsync(() -> OrderData.sampleUsers().get(0));

        // 🚨 TO DO: thenCompose to get user's department asynchronously
        CompletableFuture<String> deptFuture = null; // ❌ BROKEN

        assertThat(deptFuture.get()).isEqualTo("Engineering");

        // 💡 HINTS:
        // userFuture.thenCompose(u -> CompletableFuture.supplyAsync(() -> u.getDepartment()))
        // NOTE: thenApply would give CompletableFuture<CompletableFuture<String>>!
    }

    @Test
    @DisplayName("🚨 TODO 8: thenCombine (merge two futures)")
    public void thenCombineTwoFutures() throws Exception {
        CompletableFuture<Integer> price = CompletableFuture.supplyAsync(() -> 100);
        CompletableFuture<Double>  tax   = CompletableFuture.supplyAsync(() -> 0.18);

        // 🚨 TO DO: Combine price and tax to compute total (price * (1 + tax))
        CompletableFuture<Double> total = null; // ❌ BROKEN

        assertThat(total.get()).isEqualTo(118.0);

        // 💡 HINTS:
        // price.thenCombine(tax, (p, t) -> p * (1 + t))
    }

    // ─── ERROR HANDLING ─────────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 9: exceptionally - recover from error")
    public void exceptionallyRecover() throws Exception {
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> {
                    throw new RuntimeException("Service unavailable");
                });

        // 🚨 TO DO: Recover from exception and return "fallback"
        CompletableFuture<String> safe = null; // ❌ BROKEN

        assertThat(safe.get()).isEqualTo("fallback");

        // 💡 HINTS:
        // future.exceptionally(ex -> "fallback")
    }

    @Test
    @DisplayName("🚨 TODO 10: handle - both success and failure")
    public void handleSuccessAndFailure() throws Exception {
        CompletableFuture<Integer> success = CompletableFuture.supplyAsync(() -> 42);
        CompletableFuture<Integer> failure = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("Error");
        });

        // 🚨 TO DO: Handle both - return value if success, return -1 if exception
        CompletableFuture<Integer> safeSuccess = null; // ❌ BROKEN
        CompletableFuture<Integer> safeFailure = null; // ❌ BROKEN

        assertThat(safeSuccess.get()).isEqualTo(42);
        assertThat(safeFailure.get()).isEqualTo(-1);

        // 💡 HINTS:
        // .handle((result, ex) -> ex != null ? -1 : result)
    }

    @Test
    @DisplayName("🚨 TODO 11: whenComplete side effect on both outcomes")
    public void whenCompleteSideEffect() throws Exception {
        List<String> log = Collections.synchronizedList(new ArrayList<>());
        CompletableFuture<String> base = CompletableFuture.supplyAsync(() -> "success");

        // 🚨 TO DO: Call whenComplete on 'base' to log "completed: <value>" or "failed: <msg>"
        CompletableFuture<String> future = null; // ❌ BROKEN

        future.get();
        assertThat(log).anyMatch(entry -> entry.startsWith("completed:"));

        // 💡 HINTS:
        // base.whenComplete((r, ex) -> { if (ex != null) log.add("failed: " + ex.getMessage()); else log.add("completed: " + r); })
    }

    // ─── COMBINING MULTIPLE FUTURES ─────────────────────────────

    @Test
    @DisplayName("🚨 TODO 12: allOf - wait for all futures")
    public void allOfWaitAll() throws Exception {
        List<String> results = Collections.synchronizedList(new ArrayList<>());

        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> results.add("A"));
        CompletableFuture<Void> f2 = CompletableFuture.runAsync(() -> results.add("B"));
        CompletableFuture<Void> f3 = CompletableFuture.runAsync(() -> results.add("C"));

        // 🚨 TO DO: Wait for all three futures to complete
        CompletableFuture<Void> all = null; // ❌ BROKEN

        all.get();
        assertThat(results).containsExactlyInAnyOrder("A", "B", "C");

        // 💡 HINTS:
        // CompletableFuture.allOf(f1, f2, f3)
    }

    @Test
    @DisplayName("🚨 TODO 13: anyOf - first completed wins")
    public void anyOfFirstWins() throws Exception {
        CompletableFuture<Object> f1 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return "slow";
        });
        CompletableFuture<Object> f2 = CompletableFuture.completedFuture("fast");

        // 🚨 TO DO: Get the first completed result
        CompletableFuture<Object> first = null; // ❌ BROKEN

        assertThat(first.get()).isEqualTo("fast");

        // 💡 HINTS:
        // CompletableFuture.anyOf(f1, f2)
    }

    @Test
    @DisplayName("🚨 TODO 14: allOf + collect results pattern")
    public void allOfCollectResults() throws Exception {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        List<CompletableFuture<String>> futures = names.stream()
                .map(name -> CompletableFuture.supplyAsync(() -> name.toUpperCase()))
                .collect(Collectors.toList());

        // 🚨 TO DO: Wait for all and collect results into List<String>
        List<String> results = null; // ❌ BROKEN

        assertThat(results).containsExactlyInAnyOrder("ALICE", "BOB", "CHARLIE");

        // 💡 HINTS:
        // CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
        //     .thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()))
        //     .get()
    }

    // ─── ADVANCED ───────────────────────────────────────────────

    @Test
    @DisplayName("🚨 TODO 15: Custom executor")
    public void customExecutor() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // 🚨 TO DO: Run async task on the custom executor, capturing thread name
        CompletableFuture<String> future = null; // ❌ BROKEN

        future.get();
        executor.shutdown();
        assertThat(future).isNotNull();

        // 💡 HINTS:
        // CompletableFuture.supplyAsync(() -> Thread.currentThread().getName(), executor)
    }

    @Test
    @DisplayName("🚨 TODO 16: getNow with default")
    public void getNowWithDefault() {
        CompletableFuture<String> notDone = new CompletableFuture<>();
        CompletableFuture<String> done    = CompletableFuture.completedFuture("done");

        // 🚨 TO DO: Get value if done, or "not-ready" if not completed
        String result1 = null; // ❌ BROKEN - notDone.getNow("not-ready")
        String result2 = null; // ❌ BROKEN - done.getNow("not-ready")

        assertThat(result1).isEqualTo("not-ready");
        assertThat(result2).isEqualTo("done");

        // 💡 HINTS:
        // notDone.getNow("not-ready")
        // done.getNow("not-ready")
    }

    @Test
    @DisplayName("🚨 TODO 17: Complete manually with complete()")
    public void completeManually() throws Exception {
        CompletableFuture<String> promise = new CompletableFuture<>();

        new Thread(() -> {
            try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            promise.complete("manual-result");
        }).start();

        // 🚨 TO DO: Wait for the result
        String result = null; // ❌ BROKEN

        assertThat(result).isEqualTo("manual-result");

        // 💡 HINTS:
        // promise.get()
    }

    @Test
    @DisplayName("🚨 TODO 18: Pipeline - fetch, transform, validate")
    public void pipelinePattern() throws Exception {
        CompletableFuture<String> base = CompletableFuture.supplyAsync(() -> "  order-123  ");

        // 🚨 TO DO: Chain thenApply to trim, then uppercase, then prepend "PROCESSED:"
        CompletableFuture<String> pipeline = null; // ❌ BROKEN

        assertThat(pipeline.get()).isEqualTo("PROCESSED:ORDER-123");

        // 💡 HINTS:
        // base.thenApply(String::trim).thenApply(String::toUpperCase).thenApply(s -> "PROCESSED:" + s)
    }

    @Test
    @DisplayName("🚨 TODO 19: Exception propagation check")
    public void exceptionPropagation() {
        CompletableFuture<String> withException = CompletableFuture
                .<String>supplyAsync(() -> { throw new IllegalStateException("bad state"); });

        // 🚨 TO DO: Recover using exceptionally, then join()
        String result = null; // ❌ BROKEN

        assertThat(result).startsWith("recovered:");

        // 💡 HINTS:
        // withException.exceptionally(ex -> "recovered: " + ex.getMessage()).join()
    }

    @Test
    @DisplayName("🚨 TODO 20: Fetch multiple orders in parallel")
    public void parallelFetch() throws Exception {
        List<Integer> orderIds = Arrays.asList(1, 2, 3, 4, 5);

        // 🚨 TO DO: For each ID create a CompletableFuture that returns id * 10
        List<CompletableFuture<Integer>> futures = orderIds.stream()
                .map(id -> (CompletableFuture<Integer>) null) // ❌ BROKEN - CompletableFuture.supplyAsync(() -> id * 10)
                .collect(Collectors.toList());

        CompletableFuture<Void> allDone = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0]));

        List<Integer> results = allDone.thenApply(v ->
                futures.stream().map(CompletableFuture::join).collect(Collectors.toList())
        ).get();

        assertThat(results).containsExactlyInAnyOrder(10, 20, 30, 40, 50);

        // 💡 HINTS:
        // .map(id -> CompletableFuture.supplyAsync(() -> id * 10))
    }
}
