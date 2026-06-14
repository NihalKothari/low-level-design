package com.lld.problems.pubsub.model;

import java.util.Objects;
import java.util.UUID;

public class Message {

    private final String messageId;
    private final String topicName;
    private final String payload;

    public Message(String topicName, String payload) {
        this(UUID.randomUUID().toString(), topicName, payload);
    }

    public Message(String messageId, String topicName, String payload) {
        this.messageId = Objects.requireNonNull(messageId, "messageId");
        this.topicName = Objects.requireNonNull(topicName, "topicName");
        this.payload = Objects.requireNonNull(payload, "payload");
    }

    public String getMessageId() {
        return messageId;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "Message{" + messageId + ", topic=" + topicName + ", payload=" + payload + "}";
    }
}
