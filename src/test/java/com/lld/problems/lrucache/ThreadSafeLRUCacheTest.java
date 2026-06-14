package com.lld.problems.lrucache;

import com.lld.common.ConcurrentTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThreadSafeLRUCacheTest {

    private ThreadSafeLRUCache<Integer, String> cache;

    @BeforeEach
    void setUp() {
        cache = new ThreadSafeLRUCache<>(3);
    }

    @Test
    void putAndGet() {
        cache.put(1, "one");
        assertEquals(Optional.of("one"), cache.get(1));
    }

    @Test
    void evictsLeastRecentlyUsed() {
        cache.put(1, "a");
        cache.put(2, "b");
        cache.put(3, "c");
        cache.get(1);
        cache.put(4, "d");

        assertFalse(cache.containsKey(2));
        assertTrue(cache.containsKey(1));
        assertTrue(cache.containsKey(3));
        assertTrue(cache.containsKey(4));
        assertEquals(3, cache.size());
    }

    @Test
    void getUpdatesRecency() {
        cache.put(1, "a");
        cache.put(2, "b");
        cache.put(3, "c");
        cache.get(1);
        cache.put(4, "d");

        assertTrue(cache.containsKey(1));
        assertFalse(cache.containsKey(2));
    }

    @Test
    void concurrentAccessMaintainsCapacity() throws InterruptedException {
        ThreadSafeLRUCache<Integer, Integer> shared = new ThreadSafeLRUCache<>(50);
        AtomicInteger evicted = new AtomicInteger();

        ConcurrentTestHelper.runConcurrently(8, () -> {
            for (int i = 0; i < 200; i++) {
                int key = i % 100;
                shared.put(key, key);
                shared.get(key);
                if (shared.size() > 50) {
                    evicted.incrementAndGet();
                }
            }
        });

        assertEquals(50, shared.size());
        assertEquals(0, evicted.get());
    }
}
