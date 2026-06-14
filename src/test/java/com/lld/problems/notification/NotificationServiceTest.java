package com.lld.problems.notification;

import com.lld.common.events.InMemoryEventBus;
import com.lld.problems.notification.model.NotificationChannel;
import com.lld.problems.notification.model.NotificationRequest;
import com.lld.problems.notification.model.NotificationRequestEvent;
import com.lld.problems.notification.service.NotificationService;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotificationServiceTest {

    private InMemoryEventBus bus;
    private NotificationService service;
    private final List<NotificationRequestEvent> busEvents = new CopyOnWriteArrayList<>();

    @BeforeEach
    void setUp() {
        bus = new InMemoryEventBus();
        service = new NotificationService(bus);
        service.startListening();
        bus.subscribe(NotificationRequestEvent.TOPIC, (NotificationRequestEvent e) -> busEvents.add(e));
    }

    @Test
    void subscriberReceivesNotificationEventOnSend() {
        service.send(new NotificationRequest(
                "alice",
                "Welcome",
                "Thanks for signing up",
                EnumSet.of(NotificationChannel.EMAIL)
        ));

        assertEquals(1, busEvents.size());
        assertEquals("alice", busEvents.get(0).getRequest().getUserId());
        assertEquals(1, service.getDeliveryLog().size());
        assertTrue(service.getDeliveryLog().get(0).isDelivered());
    }

    @Test
    void dispatchesToMultipleChannels() {
        service.send(new NotificationRequest(
                "bob",
                "Alert",
                "Price drop",
                EnumSet.of(NotificationChannel.SMS, NotificationChannel.PUSH)
        ));

        assertEquals(2, service.getDeliveryLog().size());
        assertTrue(service.getDeliveryLog().stream().allMatch(r -> r.isDelivered()));
    }
}
