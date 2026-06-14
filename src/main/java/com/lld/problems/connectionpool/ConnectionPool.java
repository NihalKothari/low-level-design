package com.lld.problems.connectionpool;

import com.lld.common.concurrency.LockableResource;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Bounded connection pool using Semaphore for lease limits and a queue for idle connections.
 */
public class ConnectionPool {

    private final Semaphore permits;
    private final BlockingQueue<PooledConnection> available;
    private final LockableResource registry = new LockableResource();
    private final Set<PooledConnection> leased = ConcurrentHashMap.newKeySet();
    private volatile boolean shutdown;

    public ConnectionPool(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize must be positive");
        }
        this.permits = new Semaphore(maxSize, true);
        this.available = new LinkedBlockingQueue<>(maxSize);
        for (int i = 0; i < maxSize; i++) {
            available.offer(new PooledConnection(i));
        }
    }

    public Optional<PooledConnection> lease(long timeout, TimeUnit unit) throws InterruptedException {
        if (shutdown) {
            return Optional.empty();
        }
        if (!permits.tryAcquire(timeout, unit)) {
            return Optional.empty();
        }

        PooledConnection conn = available.poll();
        if (conn == null) {
            permits.release();
            return Optional.empty();
        }

        registry.execute(() -> leased.add(conn));
        return Optional.of(conn);
    }

    public void release(PooledConnection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("connection must not be null");
        }
        if (shutdown) {
            return;
        }

        boolean wasLeased = registry.execute(() -> leased.remove(connection));
        if (!wasLeased) {
            throw new IllegalStateException("Connection was not leased from this pool: " + connection);
        }

        available.offer(connection);
        permits.release();
    }

    public int availableCount() {
        return available.size();
    }

    public int leasedCount() {
        return registry.execute(leased::size);
    }

    public void shutdown() {
        shutdown = true;
        registry.execute(() -> {
            leased.clear();
            PooledConnection conn;
            while ((conn = available.poll()) != null) {
                conn.markClosed();
            }
        });
    }
}
