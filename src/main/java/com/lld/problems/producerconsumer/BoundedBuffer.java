package com.lld.problems.producerconsumer;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Bounded buffer for multiple producers and consumers using BlockingQueue.
 */
public class BoundedBuffer<T> {

    private final BlockingQueue<T> queue;

    public BoundedBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.queue = new ArrayBlockingQueue<>(capacity);
    }

    public void put(T item) throws InterruptedException {
        queue.put(item);
    }

    public T take() throws InterruptedException {
        return queue.take();
    }

    public boolean offer(T item, long timeout, TimeUnit unit) throws InterruptedException {
        return queue.offer(item, timeout, unit);
    }

    public Optional<T> poll(long timeout, TimeUnit unit) throws InterruptedException {
        return Optional.ofNullable(queue.poll(timeout, unit));
    }

    public int size() {
        return queue.size();
    }

    public int remainingCapacity() {
        return queue.remainingCapacity();
    }

    public int capacity() {
        return queue.size() + queue.remainingCapacity();
    }
}
