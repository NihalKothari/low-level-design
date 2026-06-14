package com.lld.problems.notification.service;

import com.lld.problems.notification.model.NotificationChannel;
import com.lld.problems.notification.model.NotificationRequest;

public class PushChannelHandler implements ChannelHandler {

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.PUSH;
    }

    @Override
    public boolean deliver(NotificationRequest request) {
        // TODO: integrate FCM/APNs
        return true;
    }
}
