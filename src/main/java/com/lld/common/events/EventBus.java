package com.lld.common.events;

import java.util.List;

/**
 * Topic-based event bus used across pub-sub and event-driven problems.
 */
public interface EventBus {

    <T extends Event> void subscribe(String topic, Subscriber<T> subscriber);

    void unsubscribe(String topic, Subscriber<? extends Event> subscriber);

    <T extends Event> void publish(T event);

    List<String> getTopics();
}
