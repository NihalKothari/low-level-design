package com.lld.problems.pubsub.model;

import com.lld.common.events.Event;

public class MessagePublishedEvent extends Event {

    public static final String TOPIC_PREFIX = "pubsub.";

    private final Message message;

    public MessagePublishedEvent(Message message) {
        super(TOPIC_PREFIX + message.getTopicName());
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
