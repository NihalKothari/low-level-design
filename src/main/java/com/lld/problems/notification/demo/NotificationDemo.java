package com.lld.problems.notification.demo;

import com.lld.common.events.InMemoryEventBus;
import com.lld.problems.notification.model.NotificationChannel;
import com.lld.problems.notification.model.NotificationRequest;
import com.lld.problems.notification.service.NotificationService;
import java.util.EnumSet;

public class NotificationDemo {

    public static void main(String[] args) {
        InMemoryEventBus bus = new InMemoryEventBus();
        NotificationService notifications = new NotificationService(bus);
        notifications.startListening();

        notifications.send(new NotificationRequest(
                "user-1",
                "Order shipped",
                "Your order #42 is on the way.",
                EnumSet.of(NotificationChannel.EMAIL, NotificationChannel.PUSH)
        ));

        System.out.println("Delivery log: " + notifications.getDeliveryLog());
    }
}
