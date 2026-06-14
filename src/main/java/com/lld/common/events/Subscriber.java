package com.lld.common.events;

@FunctionalInterface
public interface Subscriber<T extends Event> {
    void onEvent(T event);
}
