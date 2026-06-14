package com.lld.problems.notification.service;

import com.lld.problems.notification.model.NotificationChannel;
import com.lld.problems.notification.model.NotificationRequest;

public class EmailChannelHandler implements ChannelHandler {

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.EMAIL;
    }

    @Override
    public boolean deliver(NotificationRequest request) {
        // TODO: integrate SMTP provider
        return true;
    }
}
