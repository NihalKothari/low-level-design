package com.lld.common.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * In-process event bus with optional async delivery.
 */
public class InMemoryEventBus implements EventBus {

    private final Map<String, List<Subscriber<? extends Event>>> subscribers = new ConcurrentHashMap<>();
    private final ExecutorService executor;
    private final boolean async;

    public InMemoryEventBus() {
        this(false);
    }

    public InMemoryEventBus(boolean async) {
        this.async = async;
        this.executor = async ? Executors.newCachedThreadPool() : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Event> void subscribe(String topic, Subscriber<T> subscriber) {
        subscribers.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>()).add(subscriber);
    }

    @Override
    public void unsubscribe(String topic, Subscriber<? extends Event> subscriber) {
        List<Subscriber<? extends Event>> list = subscribers.get(topic);
        if (list != null) {
            list.remove(subscriber);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Event> void publish(T event) {
        List<Subscriber<? extends Event>> list = subscribers.getOrDefault(event.getTopic(), List.of());
        for (Subscriber<? extends Event> subscriber : list) {
            if (async) {
                executor.submit(() -> ((Subscriber<T>) subscriber).onEvent(event));
            } else {
                ((Subscriber<T>) subscriber).onEvent(event);
            }
        }
    }

    @Override
    public List<String> getTopics() {
        return new ArrayList<>(subscribers.keySet());
    }

    public void shutdown() {
        if (executor != null) {
            executor.shutdown();
        }
    }
}
