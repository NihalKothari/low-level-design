package com.lld.problems.ratelimiter;

import com.lld.common.concurrency.LockableResource;

/**
 * Thread-safe token-bucket rate limiter with continuous refill.
 */
public class TokenBucketRateLimiter {

    private final LockableResource lock = new LockableResource();
    private final double capacity;
    private final double refillRatePerSecond;

    private double tokens;
    private long lastRefillNanos;

    public TokenBucketRateLimiter(double capacity, double refillRatePerSecond) {
        if (capacity <= 0 || refillRatePerSecond <= 0) {
            throw new IllegalArgumentException("capacity and refill rate must be positive");
        }
        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
        this.tokens = capacity;
        this.lastRefillNanos = System.nanoTime();
    }

    public boolean tryAcquire() {
        return tryAcquire(1);
    }

    public boolean tryAcquire(int permits) {
        if (permits <= 0) {
            throw new IllegalArgumentException("permits must be positive");
        }
        return lock.execute(() -> {
            refill();
            if (tokens >= permits) {
                tokens -= permits;
                return true;
            }
            return false;
        });
    }

    public double availableTokens() {
        return lock.execute(() -> {
            refill();
            return tokens;
        });
    }

    private void refill() {
        long now = System.nanoTime();
        double elapsedSeconds = (now - lastRefillNanos) / 1_000_000_000.0;
        if (elapsedSeconds > 0) {
            tokens = Math.min(capacity, tokens + elapsedSeconds * refillRatePerSecond);
            lastRefillNanos = now;
        }
    }
}
