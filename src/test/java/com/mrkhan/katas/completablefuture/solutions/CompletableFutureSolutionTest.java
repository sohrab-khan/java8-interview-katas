package com.mrkhan.katas.completablefuture.solutions;

import com.mrkhan.katas.OrderData;
import com.mrkhan.katas.OrderData.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

import static org.assertj.core.api.Assertions.*;

/**
 * ✅ COMPLETABLEFUTURE - SOLUTIONS
 */
@DisplayName("✅ CompletableFuture - Solutions")
public class CompletableFutureSolutionTest {

    @Test @DisplayName("✅ SOLUTION 1: supplyAsync basic")
    public void supplyAsyncBasic_solution() throws Exception {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello, Async!");
        assertThat(future.get()).isEqualTo("Hello, Async!");
        // 💡 supplyAsync uses ForkJoinPool.commonPool() by default
        // 💡 .get() blocks until result available (can throw checked exceptions)
    }

    @Test @DisplayName("✅ SOLUTION 2: runAsync")
    public void runAsyncFireAndForget_solution() throws Exception {
        List<String> log = Collections.synchronizedList(new ArrayList<>());
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> log.add("done"));
        future.get();
        assertThat(log).contains("done");
        // 💡 runAsync: no return value (Void), just side effects
    }

    @Test @DisplayName("✅ SOLUTION 3: completedFuture")
    public void completedFuture_solution() throws Exception {
        CompletableFuture<Integer> future = CompletableFuture.completedFuture(42);
        assertThat(future.isDone()).isTrue();
        assertThat(future.get()).isEqualTo(42);
        // 💡 Useful for unit tests and mocking async services
    }

    @Test @DisplayName("✅ SOLUTION 4: thenApply")
    public void thenApplyTransform_solution() throws Exception {
        CompletableFuture<String> upper = CompletableFuture.supplyAsync(() -> "hello")
                .thenApply(String::toUpperCase);
        assertThat(upper.get()).isEqualTo("HELLO");
        // 💡 thenApply = map for CompletableFuture
        // 💡 Runs in same thread as the previous stage (or common pool)
    }

    @Test @DisplayName("✅ SOLUTION 5: thenApply chain")
    public void thenApplyChain_solution() throws Exception {
        CompletableFuture<String> result = CompletableFuture
                .supplyAsync(() -> "  hello world  ")
                .thenApply(String::trim)
                .thenApply(String::toUpperCase);
        assertThat(result.get()).isEqualTo("HELLO WORLD");
        // 💡 Each thenApply creates a new CompletableFuture stage
    }

    @Test @DisplayName("✅ SOLUTION 6: thenAccept")
    public void thenAcceptConsumer_solution() throws Exception {
        List<String> results = Collections.synchronizedList(new ArrayList<>());
        CompletableFuture<Void> future = CompletableFuture
                .supplyAsync(() -> "Order processed")
                .thenAccept(results::add);
        future.get();
        assertThat(results).contains("Order processed");
        // 💡 thenAccept = Consumer, no return value
        // 💡 Use for logging, saving, side effects
    }

    @Test @DisplayName("✅ SOLUTION 7: thenCompose")
    public void thenComposeChain_solution() throws Exception {
        CompletableFuture<User> userFuture = CompletableFuture
                .supplyAsync(() -> OrderData.sampleUsers().get(0));

        CompletableFuture<String> deptFuture = userFuture
                .thenCompose(u -> CompletableFuture.supplyAsync(u::getDepartment));

        assertThat(deptFuture.get()).isEqualTo("Engineering");
        // 💡 thenCompose = flatMap for CompletableFuture
        // 💡 Avoids CompletableFuture<CompletableFuture<T>>
        // 💡 Use when the next step also returns a CompletableFuture
    }

    @Test @DisplayName("✅ SOLUTION 8: thenCombine")
    public void thenCombineTwoFutures_solution() throws Exception {
        CompletableFuture<Integer> price = CompletableFuture.supplyAsync(() -> 100);
        CompletableFuture<Double>  tax   = CompletableFuture.supplyAsync(() -> 0.18);

        CompletableFuture<Double> total = price.thenCombine(tax, (p, t) -> p * (1 + t));

        assertThat(total.get()).isEqualTo(118.0);
        // 💡 thenCombine: runs BOTH futures independently, combines results
        // 💡 Like a BiFunction joining two async results
    }

    @Test @DisplayName("✅ SOLUTION 9: exceptionally")
    public void exceptionallyRecover_solution() throws Exception {
        CompletableFuture<String> safe = CompletableFuture
                .<String>supplyAsync(() -> { throw new RuntimeException("Service unavailable"); })
                .exceptionally(ex -> "fallback");
        assertThat(safe.get()).isEqualTo("fallback");
        // 💡 exceptionally handles ONLY exceptions (no access to result)
        // 💡 For both cases, use handle()
    }

    @Test @DisplayName("✅ SOLUTION 10: handle both outcomes")
    public void handleSuccessAndFailure_solution() throws Exception {
        CompletableFuture<Integer> safeSuccess = CompletableFuture
                .supplyAsync(() -> 42)
                .handle((result, ex) -> ex != null ? -1 : result);

        CompletableFuture<Integer> safeFailure = CompletableFuture
                .<Integer>supplyAsync(() -> { throw new RuntimeException("Error"); })
                .handle((result, ex) -> ex != null ? -1 : result);

        assertThat(safeSuccess.get()).isEqualTo(42);
        assertThat(safeFailure.get()).isEqualTo(-1);
        // 💡 handle(BiFunction<T, Throwable, R>) – always called (success or failure)
        // 💡 ex is null on success, result is null on failure
    }

    @Test @DisplayName("✅ SOLUTION 11: whenComplete side effect")
    public void whenCompleteSideEffect_solution() throws Exception {
        List<String> log = Collections.synchronizedList(new ArrayList<>());

        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> "success")
                .whenComplete((r, ex) -> {
                    if (ex != null) log.add("failed: " + ex.getMessage());
                    else log.add("completed: " + r);
                });

        future.get();
        assertThat(log).anyMatch(entry -> entry.startsWith("completed:"));
        // 💡 whenComplete: side effect only, result passes through unchanged
        // 💡 Differs from handle: handle CAN change the result type
    }

    @Test @DisplayName("✅ SOLUTION 12: allOf wait all")
    public void allOfWaitAll_solution() throws Exception {
        List<String> results = Collections.synchronizedList(new ArrayList<>());

        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> results.add("A"));
        CompletableFuture<Void> f2 = CompletableFuture.runAsync(() -> results.add("B"));
        CompletableFuture<Void> f3 = CompletableFuture.runAsync(() -> results.add("C"));

        CompletableFuture.allOf(f1, f2, f3).get();

        assertThat(results).containsExactlyInAnyOrder("A", "B", "C");
        // 💡 allOf returns Void – you must collect results from original futures
    }

    @Test @DisplayName("✅ SOLUTION 13: anyOf first wins")
    public void anyOfFirstWins_solution() throws Exception {
        CompletableFuture<Object> f1 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return "slow";
        });
        CompletableFuture<Object> f2 = CompletableFuture.completedFuture("fast");

        CompletableFuture<Object> first = CompletableFuture.anyOf(f1, f2);

        assertThat(first.get()).isEqualTo("fast");
        // 💡 anyOf: first to complete wins, result is Object (no generic)
        // 💡 Useful for race conditions / hedged requests
    }

    @Test @DisplayName("✅ SOLUTION 14: allOf + collect results")
    public void allOfCollectResults_solution() throws Exception {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        List<CompletableFuture<String>> futures = names.stream()
                .map(name -> CompletableFuture.supplyAsync(name::toUpperCase))
                .collect(Collectors.toList());

        List<String> results = CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()))
                .get();

        assertThat(results).containsExactlyInAnyOrder("ALICE", "BOB", "CHARLIE");
        // 💡 The allOf + thenApply + join pattern is the standard Java 8 way
        // 💡 .join() is like .get() but throws unchecked exception
    }

    @Test @DisplayName("✅ SOLUTION 15: Custom executor")
    public void customExecutor_solution() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() ->
                Thread.currentThread().getName(), executor);

        String threadName = future.get();
        executor.shutdown();

        assertThat(threadName).isNotNull();
        // 💡 Always provide executor in production (don't rely on common pool)
        // 💡 Common pool is shared – blocking tasks can starve other async work
    }

    @Test @DisplayName("✅ SOLUTION 16: getNow with default")
    public void getNowWithDefault_solution() {
        CompletableFuture<String> notDone = new CompletableFuture<>();
        CompletableFuture<String> done    = CompletableFuture.completedFuture("done");

        String result1 = notDone.getNow("not-ready");
        String result2 = done.getNow("not-ready");

        assertThat(result1).isEqualTo("not-ready");
        assertThat(result2).isEqualTo("done");
        // 💡 getNow: non-blocking, returns default if not complete
    }

    @Test @DisplayName("✅ SOLUTION 17: Complete manually")
    public void completeManually_solution() throws Exception {
        CompletableFuture<String> promise = new CompletableFuture<>();

        new Thread(() -> {
            try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            promise.complete("manual-result");
        }).start();

        String result = promise.get();
        assertThat(result).isEqualTo("manual-result");
        // 💡 promise.complete() fulfills a manually created future
        // 💡 Use for callback-to-future bridges (legacy API integration)
    }

    @Test @DisplayName("✅ SOLUTION 18: Pipeline")
    public void pipelinePattern_solution() throws Exception {
        CompletableFuture<String> pipeline = CompletableFuture
                .supplyAsync(() -> "  order-123  ")
                .thenApply(String::trim)
                .thenApply(String::toUpperCase)
                .thenApply(s -> "PROCESSED:" + s);

        assertThat(pipeline.get()).isEqualTo("PROCESSED:ORDER-123");
        // 💡 Pipelines make async processing readable and composable
    }

    @Test @DisplayName("✅ SOLUTION 19: join() unchecked")
    public void exceptionPropagation_solution() {
        CompletableFuture<String> future = CompletableFuture
                .<String>supplyAsync(() -> { throw new IllegalStateException("bad state"); })
                .exceptionally(ex -> "recovered: " + ex.getMessage());

        String result = future.join();
        assertThat(result).startsWith("recovered:");
        // 💡 join() ~ get() but throws CompletionException (unchecked) instead of checked exception
        // 💡 Use join() in stream pipelines where checked exceptions are awkward
    }

    @Test @DisplayName("✅ SOLUTION 20: Parallel fetch pattern")
    public void parallelFetch_solution() throws Exception {
        List<Integer> orderIds = Arrays.asList(1, 2, 3, 4, 5);

        List<CompletableFuture<Integer>> futures = orderIds.stream()
                .map(id -> CompletableFuture.supplyAsync(() -> id * 10))
                .collect(Collectors.toList());

        List<Integer> results = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()))
                .get();

        assertThat(results).containsExactlyInAnyOrder(10, 20, 30, 40, 50);
        // 💡 This is the canonical pattern for parallel processing with results
        // 💡 All futures run concurrently, collected after allOf completes
    }
}
