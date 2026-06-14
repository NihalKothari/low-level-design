package com.lld.problems.notification.service;

import com.lld.problems.notification.model.NotificationChannel;
import com.lld.problems.notification.model.NotificationRequest;

public class SmsChannelHandler implements ChannelHandler {

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.SMS;
    }

    @Override
    public boolean deliver(NotificationRequest request) {
        // TODO: integrate SMS gateway
        return true;
    }
}
