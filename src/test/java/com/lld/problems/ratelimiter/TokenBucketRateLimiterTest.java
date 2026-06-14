package com.lld.problems.ratelimiter;

import com.lld.common.ConcurrentTestHelper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenBucketRateLimiterTest {

    @Test
    void consumesTokensUntilEmpty() {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(3, 100);
        assertTrue(limiter.tryAcquire());
        assertTrue(limiter.tryAcquire());
        assertTrue(limiter.tryAcquire());
        assertFalse(limiter.tryAcquire());
    }

    @Test
    void refillsOverTime() throws InterruptedException {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(2, 10);
        assertTrue(limiter.tryAcquire());
        assertTrue(limiter.tryAcquire());
        assertFalse(limiter.tryAcquire());

        Thread.sleep(150);
        assertTrue(limiter.tryAcquire());
    }

    @Test
    void tryAcquireMultiplePermits() {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(5, 1);
        assertTrue(limiter.tryAcquire(3));
        assertFalse(limiter.tryAcquire(3));
        assertTrue(limiter.tryAcquire(2));
    }

    @Test
    void concurrentAcquiresRespectCapacity() throws Exception {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(10, 0.1);
        AtomicInteger successes = new AtomicInteger();

        List<Boolean> results = ConcurrentTestHelper.invokeAll(20, () -> {
            if (limiter.tryAcquire()) {
                successes.incrementAndGet();
                return true;
            }
            return false;
        });

        assertEquals(20, results.size());
        assertTrue(successes.get() <= 10);
    }
}
