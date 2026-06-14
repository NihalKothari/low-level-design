package com.lld.problems.connectionpool;

import com.lld.common.ConcurrentTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConnectionPoolTest {

    private ConnectionPool pool;

    @BeforeEach
    void setUp() {
        pool = new ConnectionPool(3);
    }

    @Test
    void leaseAndRelease() throws Exception {
        Optional<PooledConnection> leased = pool.lease(100, TimeUnit.MILLISECONDS);
        assertTrue(leased.isPresent());
        assertEquals(2, pool.availableCount());
        pool.release(leased.get());
        assertEquals(3, pool.availableCount());
    }

    @Test
    void timesOutWhenExhausted() throws Exception {
        List<PooledConnection> leased = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            leased.add(pool.lease(100, TimeUnit.MILLISECONDS).orElseThrow());
        }
        long start = System.nanoTime();
        Optional<PooledConnection> timedOut = pool.lease(100, TimeUnit.MILLISECONDS);
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;
        assertTrue(timedOut.isEmpty());
        assertTrue(elapsedMs >= 50);

        pool.release(leased.get(0));
    }

    @Test
    void rejectsDoubleRelease() throws Exception {
        PooledConnection conn = pool.lease(100, TimeUnit.MILLISECONDS).orElseThrow();
        pool.release(conn);
        assertThrows(IllegalStateException.class, () -> pool.release(conn));
    }

    @Test
    void concurrentLeasingRespectsMaxSize() throws InterruptedException {
        AtomicInteger maxLeased = new AtomicInteger();

        ConcurrentTestHelper.runConcurrently(10, () -> {
            try {
                Optional<PooledConnection> conn = pool.lease(500, TimeUnit.MILLISECONDS);
                if (conn.isPresent()) {
                    int leased = pool.leasedCount();
                    maxLeased.updateAndGet(current -> Math.max(current, leased));
                    Thread.sleep(20);
                    pool.release(conn.get());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        assertTrue(maxLeased.get() <= 3);
        assertEquals(3, pool.availableCount());
    }
}
