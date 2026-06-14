package com.lld.common.concurrency;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * Helper for problems 15-17, 20 — encapsulates lock acquire/release with timeout.
 */
public class LockableResource {

    private final ReentrantLock lock = new ReentrantLock();

    public <T> T execute(Supplier<T> action) {
        lock.lock();
        try {
            return action.get();
        } finally {
            lock.unlock();
        }
    }

    public void execute(Runnable action) {
        lock.lock();
        try {
            action.run();
        } finally {
            lock.unlock();
        }
    }

    public <T> T tryExecute(long timeoutMs, Supplier<T> action) throws InterruptedException {
        if (lock.tryLock(timeoutMs, TimeUnit.MILLISECONDS)) {
            try {
                return action.get();
            } finally {
                lock.unlock();
            }
        }
        return null;
    }
}
