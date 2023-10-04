package com.festago.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

public class AsyncBatchExecutor<T> {

    private final int batchSize;
    private final Executor executor;

    public AsyncBatchExecutor(int batchSize, Executor executor) {
        this.batchSize = batchSize;
        this.executor = executor;
    }

    public boolean execute(List<T> items, Function<List<T>, Boolean> action) {
        if (items.size() <= batchSize) {
            return action.apply(items);
        }

        List<CompletableFuture<Boolean>> futures = createFuturesForBatches(items, action);
        return awaitAllFuturesAndCheckResults(futures);
    }

    private List<CompletableFuture<Boolean>> createFuturesForBatches(List<T> items, Function<List<T>, Boolean> action) {
        List<CompletableFuture<Boolean>> futures = new ArrayList<>();
        for (int i = 0; i < items.size(); i += batchSize) {
            List<T> batchItems = items.subList(i, Math.min(i + batchSize, items.size()));
            futures.add(createFutureForBatch(batchItems, action));
        }
        return futures;
    }

    private CompletableFuture<Boolean> createFutureForBatch(List<T> batchItems, Function<List<T>, Boolean> action) {
        return CompletableFuture.supplyAsync(() -> action.apply(batchItems), executor);
    }

    private boolean awaitAllFuturesAndCheckResults(List<CompletableFuture<Boolean>> futures) {
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        try {
            allOf.join();
            for (CompletableFuture<Boolean> future : futures) {
                if (!future.get()) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
