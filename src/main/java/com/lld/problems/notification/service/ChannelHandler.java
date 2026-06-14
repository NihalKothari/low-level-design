package com.lld.problems.notification.service;

import com.lld.problems.notification.model.NotificationChannel;
import com.lld.problems.notification.model.NotificationRequest;

public interface ChannelHandler {

    NotificationChannel getChannel();

    boolean deliver(NotificationRequest request);
}
