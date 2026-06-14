package com.lld.problems.producerconsumer;

import com.lld.common.ConcurrentTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoundedBufferTest {

    private BoundedBuffer<String> buffer;

    @BeforeEach
    void setUp() {
        buffer = new BoundedBuffer<>(5);
    }

    @Test
    void putAndTake() throws Exception {
        buffer.put("item");
        assertEquals("item", buffer.take());
    }

    @Test
    void offerTimesOutWhenFull() throws Exception {
        for (int i = 0; i < 5; i++) {
            buffer.put("x" + i);
        }
        long start = System.nanoTime();
        boolean offered = buffer.offer("overflow", 100, TimeUnit.MILLISECONDS);
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;
        assertTrue(!offered);
        assertTrue(elapsedMs >= 50);
    }

    @Test
    void multipleProducersAndConsumers() throws InterruptedException {
        BoundedBuffer<Integer> shared = new BoundedBuffer<>(10);
        AtomicInteger produced = new AtomicInteger();
        AtomicInteger consumed = new AtomicInteger();
        AtomicInteger roleAssigner = new AtomicInteger();

        ConcurrentTestHelper.runConcurrently(4, () -> {
            int role = roleAssigner.getAndIncrement();
            if (role < 2) {
                int producerId = role;
                for (int i = producerId * 50; i < (producerId + 1) * 50; i++) {
                    try {
                        shared.put(i);
                        produced.incrementAndGet();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            } else {
                for (int j = 0; j < 50; j++) {
                    try {
                        shared.take();
                        consumed.incrementAndGet();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        });

        assertEquals(100, produced.get());
        assertEquals(100, consumed.get());
    }
}
