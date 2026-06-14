package com.lld.problems.pubsub.demo;

import com.lld.common.events.InMemoryEventBus;
import com.lld.problems.pubsub.model.Message;
import com.lld.problems.pubsub.service.PubSubService;
import java.util.ArrayList;
import java.util.List;

public class PubSubDemo {

    public static void main(String[] args) {
        InMemoryEventBus bus = new InMemoryEventBus();
        PubSubService pubSub = new PubSubService(bus);

        pubSub.createTopic("alerts").getValue().orElseThrow();
        List<String> received = new ArrayList<>();

        pubSub.subscribe("alerts", event -> received.add(event.getMessage().getPayload()))
                .getValue()
                .orElseThrow();

        pubSub.publish(new Message("alerts", "CPU threshold exceeded"));
        System.out.println("Subscribers received: " + received);
    }
}
