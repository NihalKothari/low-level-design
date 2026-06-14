package com.lld.problems.threadpool;

/**
 * Saturation policy when the pool cannot accept a new task.
 */
public enum RejectionPolicy {
    /** Throw {@link java.util.concurrent.RejectedExecutionException}. */
    ABORT,
    /** Run the task on the calling thread. */
    CALLER_RUNS,
    /** Silently drop the incoming task. */
    DISCARD,
    /** Remove the oldest queued task and enqueue the new one. */
    DISCARD_OLDEST
}
