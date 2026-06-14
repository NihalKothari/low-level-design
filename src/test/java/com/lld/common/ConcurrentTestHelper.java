package com.lld.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Multi-threaded test utilities for concurrency problems (15-20).
 */
public final class ConcurrentTestHelper {

    private ConcurrentTestHelper() {}

    public static void runConcurrently(int threadCount, Runnable task) throws InterruptedException {
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threadCount);
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            Thread t = new Thread(() -> {
                try {
                    start.await();
                    task.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
            threads.add(t);
            t.start();
        }

        start.countDown();
        if (!done.await(30, TimeUnit.SECONDS)) {
            throw new AssertionError("Concurrent tasks did not finish in time");
        }
    }

    public static <T> List<T> invokeAll(int threadCount, Callable<T> task) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        try {
            List<Callable<T>> tasks = new ArrayList<>();
            for (int i = 0; i < threadCount; i++) {
                tasks.add(task);
            }
            List<Future<T>> futures = pool.invokeAll(tasks);
            List<T> results = new ArrayList<>();
            for (Future<T> f : futures) {
                results.add(f.get(10, TimeUnit.SECONDS));
            }
            return results;
        } finally {
            pool.shutdown();
            pool.awaitTermination(10, TimeUnit.SECONDS);
        }
    }
}
