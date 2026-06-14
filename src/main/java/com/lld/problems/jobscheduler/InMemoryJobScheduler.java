package com.lld.problems.jobscheduler;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * In-memory job scheduler backed by DelayQueue and a dedicated dispatcher thread.
 */
public class InMemoryJobScheduler {

    private final DelayQueue<ScheduledJob> queue = new DelayQueue<>();
    private final Map<String, ScheduledJob> jobs = new ConcurrentHashMap<>();
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final Thread dispatcher;
    private final CountDownLatch terminationLatch = new CountDownLatch(1);

    public InMemoryJobScheduler() {
        dispatcher = new Thread(this::dispatchLoop, "job-scheduler-dispatcher");
        dispatcher.setDaemon(true);
        dispatcher.start();
    }

    public String scheduleOnce(Runnable task, long delay, TimeUnit unit) {
        ensureRunning();
        String id = UUID.randomUUID().toString();
        ScheduledJob job = new ScheduledJob(id, task, delay, unit);
        register(job);
        return id;
    }

    public String scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        ensureRunning();
        String id = UUID.randomUUID().toString();
        ScheduledJob job = new ScheduledJob(id, task, initialDelay, unit, period);
        register(job);
        return id;
    }

    public boolean cancel(String jobId) {
        ScheduledJob job = jobs.remove(jobId);
        if (job != null) {
            job.cancel();
            queue.remove(job);
            return true;
        }
        return false;
    }

    public void shutdown() {
        if (running.compareAndSet(true, false)) {
            dispatcher.interrupt();
        }
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return terminationLatch.await(timeout, unit);
    }

    public int pendingJobCount() {
        return jobs.size();
    }

    private void register(ScheduledJob job) {
        jobs.put(job.getId(), job);
        queue.offer(job);
    }

    private void ensureRunning() {
        if (!running.get()) {
            throw new IllegalStateException("Scheduler is shut down");
        }
    }

    private void dispatchLoop() {
        try {
            while (running.get()) {
                ScheduledJob job = queue.take();
                if (job.isCancelled()) {
                    jobs.remove(job.getId());
                    continue;
                }
                try {
                    job.getTask().run();
                } catch (RuntimeException ignored) {
                    // isolate task failures
                }
                if (job.isRecurring() && !job.isCancelled()) {
                    job.reschedule();
                    queue.offer(job);
                } else {
                    jobs.remove(job.getId());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            terminationLatch.countDown();
        }
    }
}
