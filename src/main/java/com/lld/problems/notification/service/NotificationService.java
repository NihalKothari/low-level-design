package com.lld.problems.notification.service;

import com.lld.common.events.EventBus;
import com.lld.problems.notification.model.DeliveryRecord;
import com.lld.problems.notification.model.NotificationChannel;
import com.lld.problems.notification.model.NotificationRequest;
import com.lld.problems.notification.model.NotificationRequestEvent;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Listens on shared {@link EventBus} and routes notifications to channel handlers.
 */
public class NotificationService {

    private final EventBus eventBus;
    private final Map<NotificationChannel, ChannelHandler> channels = new EnumMap<>(NotificationChannel.class);
    private final List<DeliveryRecord> deliveryLog = new ArrayList<>();
    private boolean listening;

    public NotificationService(EventBus eventBus) {
        this.eventBus = eventBus;
        registerChannel(new EmailChannelHandler());
        registerChannel(new SmsChannelHandler());
        registerChannel(new PushChannelHandler());
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void registerChannel(ChannelHandler handler) {
        channels.put(handler.getChannel(), handler);
    }

    public void startListening() {
        if (listening) {
            return;
        }
        eventBus.subscribe(NotificationRequestEvent.TOPIC, this::handleNotificationEvent);
        listening = true;
    }

    public void send(NotificationRequest request) {
        eventBus.publish(new NotificationRequestEvent(request));
    }

    private void handleNotificationEvent(NotificationRequestEvent event) {
        NotificationRequest request = event.getRequest();
        for (NotificationChannel channel : request.getChannels()) {
            ChannelHandler handler = channels.get(channel);
            if (handler == null) {
                deliveryLog.add(new DeliveryRecord(request.getUserId(), channel, false, "no handler"));
                continue;
            }
            boolean ok = handler.deliver(request);
            deliveryLog.add(new DeliveryRecord(
                    request.getUserId(),
                    channel,
                    ok,
                    ok ? "sent" : "failed"
            ));
        }
    }

    public List<DeliveryRecord> getDeliveryLog() {
        return List.copyOf(deliveryLog);
    }
}
