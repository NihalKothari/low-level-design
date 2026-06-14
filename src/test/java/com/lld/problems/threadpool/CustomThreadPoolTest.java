package com.lld.problems.threadpool;

import com.lld.common.ConcurrentTestHelper;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomThreadPoolTest {

    @Test
    void executesSubmittedTasks() throws Exception {
        CustomThreadPool pool = new CustomThreadPool(2, 2, 4, RejectionPolicy.ABORT);
        CountDownLatch latch = new CountDownLatch(3);
        for (int i = 0; i < 3; i++) {
            pool.submit(latch::countDown);
        }
        assertTrue(latch.await(3, TimeUnit.SECONDS));
        pool.shutdown();
        pool.awaitTermination(2, TimeUnit.SECONDS);
    }

    @Test
    void abortPolicyThrowsWhenSaturated() throws Exception {
        CustomThreadPool pool = new CustomThreadPool(1, 1, 1, RejectionPolicy.ABORT);
        CountDownLatch blocker = new CountDownLatch(1);
        pool.submit(() -> {
            try {
                blocker.await(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        Thread.sleep(50);
        pool.submit(() -> {});
        assertThrows(RejectedExecutionException.class, () -> pool.submit(() -> {}));
        blocker.countDown();
        pool.shutdown();
        pool.awaitTermination(2, TimeUnit.SECONDS);
    }

    @Test
    void callerRunsPolicyExecutesOnSubmittingThread() throws Exception {
        CustomThreadPool pool = new CustomThreadPool(1, 1, 1, RejectionPolicy.CALLER_RUNS);
        CountDownLatch blocker = new CountDownLatch(1);
        pool.submit(() -> {
            try {
                blocker.await(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        Thread.sleep(50);
        pool.submit(() -> { /* fills the queue while worker is busy */ });

        AtomicInteger ran = new AtomicInteger();
        pool.submit(ran::incrementAndGet);
        assertEquals(1, ran.get());

        blocker.countDown();
        pool.shutdown();
        pool.awaitTermination(2, TimeUnit.SECONDS);
    }

    @Test
    void concurrentSubmissionsAreProcessed() throws Exception {
        CustomThreadPool pool = new CustomThreadPool(2, 4, 8, RejectionPolicy.DISCARD_OLDEST);
        AtomicInteger completed = new AtomicInteger();

        ConcurrentTestHelper.runConcurrently(8, () -> {
            for (int i = 0; i < 10; i++) {
                pool.submit(completed::incrementAndGet);
            }
        });

        Thread.sleep(500);
        assertTrue(completed.get() >= 50);
        pool.shutdown();
        pool.awaitTermination(3, TimeUnit.SECONDS);
    }
}
