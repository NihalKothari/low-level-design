package com.lld.common.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Base event type for pub-sub problems (21-25, 11, 22).
 */
public abstract class Event {

    private final String eventId;
    private final String topic;
    private final Instant timestamp;

    protected Event(String topic) {
        this.eventId = UUID.randomUUID().toString();
        this.topic = topic;
        this.timestamp = Instant.now();
    }

    public String getEventId() {
        return eventId;
    }

    public String getTopic() {
        return topic;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
