package com.lld.problems.chat.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Message {

    private final String messageId;
    private final String roomId;
    private final String senderId;
    private final String text;
    private final Instant sentAt;

    public Message(String roomId, String senderId, String text) {
        this.messageId = UUID.randomUUID().toString();
        this.roomId = Objects.requireNonNull(roomId, "roomId");
        this.senderId = Objects.requireNonNull(senderId, "senderId");
        this.text = Objects.requireNonNull(text, "text");
        this.sentAt = Instant.now();
    }

    public String getMessageId() {
        return messageId;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getText() {
        return text;
    }

    public Instant getSentAt() {
        return sentAt;
    }
}
