package com.lld.problems.threadpool;

import com.lld.common.concurrency.LockableResource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Simplified thread pool with bounded queue and configurable rejection policy.
 */
public class CustomThreadPool {

    private final int corePoolSize;
    private final int maxPoolSize;
    private final BlockingQueue<Runnable> workQueue;
    private final RejectionPolicy rejectionPolicy;
    private final LockableResource stateLock = new LockableResource();
    private final List<Worker> workers = new ArrayList<>();

    private volatile boolean running = true;

    public CustomThreadPool(int corePoolSize, int maxPoolSize, int queueCapacity, RejectionPolicy rejectionPolicy) {
        if (corePoolSize <= 0 || maxPoolSize < corePoolSize || queueCapacity <= 0) {
            throw new IllegalArgumentException("Invalid pool configuration");
        }
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.workQueue = new ArrayBlockingQueue<>(queueCapacity);
        this.rejectionPolicy = rejectionPolicy;

        stateLock.execute(() -> {
            for (int i = 0; i < corePoolSize; i++) {
                addWorker();
            }
        });
    }

    public void submit(Runnable task) {
        if (task == null) {
            throw new NullPointerException("task");
        }
        if (!running) {
            throw new RejectedExecutionException("Pool is shut down");
        }

        if (workQueue.offer(task)) {
            return;
        }

        boolean addedWorker = stateLock.execute(() -> {
            if (!running) {
                return false;
            }
            if (workers.size() < maxPoolSize) {
                addWorker();
                return true;
            }
            return false;
        });

        if (addedWorker && workQueue.offer(task)) {
            return;
        }

        handleRejection(task);
    }

    public void shutdown() {
        running = false;
        stateLock.execute(() -> {
            for (Worker worker : workers) {
                worker.thread.interrupt();
            }
        });
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long deadline = System.nanoTime() + unit.toNanos(timeout);
        List<Worker> snapshot = stateLock.execute(() -> new ArrayList<>(workers));
        for (Worker worker : snapshot) {
            long remaining = deadline - System.nanoTime();
            if (remaining <= 0) {
                return false;
            }
            worker.thread.join(remaining / 1_000_000, (int) (remaining % 1_000_000));
            if (worker.thread.isAlive()) {
                return false;
            }
        }
        return true;
    }

    public int getActiveCount() {
        return stateLock.execute(workers::size);
    }

    public int getQueueSize() {
        return workQueue.size();
    }

    private void addWorker() {
        Worker worker = new Worker();
        workers.add(worker);
        worker.thread.start();
    }

    private void handleRejection(Runnable task) {
        switch (rejectionPolicy) {
            case ABORT -> throw new RejectedExecutionException("Pool saturated");
            case CALLER_RUNS -> task.run();
            case DISCARD -> { /* drop */ }
            case DISCARD_OLDEST -> {
                workQueue.poll();
                workQueue.offer(task);
            }
        }
    }

    private final class Worker implements Runnable {
        private final Thread thread = new Thread(this, "custom-pool-worker");

        @Override
        public void run() {
            while (running || !workQueue.isEmpty()) {
                try {
                    Runnable task = workQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (task != null) {
                        task.run();
                    }
                } catch (InterruptedException e) {
                    if (!running) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
    }
}
