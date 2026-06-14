package com.lld.problems.connectionpool;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Lightweight stand-in for a real database or socket connection.
 */
public class PooledConnection {

    private final int id;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public PooledConnection(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isClosed() {
        return closed.get();
    }

    void markClosed() {
        closed.set(true);
    }

    /** Simulated work on the connection. */
    public void execute(String query) {
        if (closed.get()) {
            throw new IllegalStateException("Connection " + id + " is closed");
        }
    }

    @Override
    public String toString() {
        return "PooledConnection{id=" + id + ", closed=" + closed.get() + "}";
    }
}
