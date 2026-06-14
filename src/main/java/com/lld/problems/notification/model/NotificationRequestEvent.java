package com.lld.problems.notification.model;

import com.lld.common.events.Event;

public class NotificationRequestEvent extends Event {

    public static final String TOPIC = "notification.send";

    private final NotificationRequest request;

    public NotificationRequestEvent(NotificationRequest request) {
        super(TOPIC);
        this.request = request;
    }

    public NotificationRequest getRequest() {
        return request;
    }
}
