package com.lld.problems.jobscheduler;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * DelayQueue entry wrapping a runnable task.
 */
public class ScheduledJob implements Delayed {

    private final String id;
    private final Runnable task;
    private volatile long executeAtNanos;
    private final long periodNanos;
    private volatile boolean cancelled;

    public ScheduledJob(String id, Runnable task, long delay, TimeUnit unit) {
        this(id, task, delay, unit, 0);
    }

    public ScheduledJob(String id, Runnable task, long initialDelay, TimeUnit unit, long period) {
        this.id = id;
        this.task = task;
        this.periodNanos = unit.toNanos(period);
        this.executeAtNanos = System.nanoTime() + unit.toNanos(initialDelay);
    }

    public String getId() {
        return id;
    }

    public Runnable getTask() {
        return task;
    }

    public boolean isRecurring() {
        return periodNanos > 0;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        cancelled = true;
    }

    public void reschedule() {
        if (isRecurring() && !cancelled) {
            executeAtNanos = System.nanoTime() + periodNanos;
        }
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(executeAtNanos - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed other) {
        return Long.compare(getDelay(TimeUnit.NANOSECONDS), other.getDelay(TimeUnit.NANOSECONDS));
    }
}
