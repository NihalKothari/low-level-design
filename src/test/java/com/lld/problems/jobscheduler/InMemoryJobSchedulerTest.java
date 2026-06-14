package com.lld.problems.jobscheduler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryJobSchedulerTest {

    private InMemoryJobScheduler scheduler;

    @AfterEach
    void tearDown() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }

    @Test
    void scheduleOnceRunsAfterDelay() throws Exception {
        scheduler = new InMemoryJobScheduler();
        CountDownLatch latch = new CountDownLatch(1);
        scheduler.scheduleOnce(latch::countDown, 100, TimeUnit.MILLISECONDS);
        assertTrue(latch.await(2, TimeUnit.SECONDS));
    }

    @Test
    void scheduleAtFixedRateRepeats() throws Exception {
        scheduler = new InMemoryJobScheduler();
        AtomicInteger runs = new AtomicInteger();
        scheduler.scheduleAtFixedRate(runs::incrementAndGet, 50, 50, TimeUnit.MILLISECONDS);

        Thread.sleep(250);
        assertTrue(runs.get() >= 3);
    }

    @Test
    void cancelPreventsFutureRuns() throws Exception {
        scheduler = new InMemoryJobScheduler();
        AtomicInteger runs = new AtomicInteger();
        String jobId = scheduler.scheduleOnce(runs::incrementAndGet, 200, TimeUnit.MILLISECONDS);
        assertTrue(scheduler.cancel(jobId));
        Thread.sleep(300);
        assertEquals(0, runs.get());
    }

    @Test
    void cancelUnknownJobReturnsFalse() {
        scheduler = new InMemoryJobScheduler();
        assertFalse(scheduler.cancel("missing"));
    }

    @Test
    void shutdownStopsAcceptingJobs() throws Exception {
        scheduler = new InMemoryJobScheduler();
        scheduler.shutdown();
        scheduler.awaitTermination(1, TimeUnit.SECONDS);
    }
}
