package com.lld.problems.notification.model;

public class DeliveryRecord {

    private final String userId;
    private final NotificationChannel channel;
    private final boolean delivered;
    private final String detail;

    public DeliveryRecord(String userId, NotificationChannel channel, boolean delivered, String detail) {
        this.userId = userId;
        this.channel = channel;
        this.delivered = delivered;
        this.detail = detail;
    }

    public String getUserId() {
        return userId;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public String getDetail() {
        return detail;
    }
}
