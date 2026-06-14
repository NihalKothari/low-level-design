package com.lld.problems.notification.model;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public class NotificationRequest {

    private final String userId;
    private final String subject;
    private final String body;
    private final Set<NotificationChannel> channels;

    public NotificationRequest(String userId, String subject, String body, Set<NotificationChannel> channels) {
        this.userId = Objects.requireNonNull(userId, "userId");
        this.subject = Objects.requireNonNull(subject, "subject");
        this.body = Objects.requireNonNull(body, "body");
        this.channels = EnumSet.copyOf(Objects.requireNonNull(channels, "channels"));
        if (this.channels.isEmpty()) {
            throw new IllegalArgumentException("At least one channel required");
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public Set<NotificationChannel> getChannels() {
        return EnumSet.copyOf(channels);
    }
}
