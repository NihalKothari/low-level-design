package com.lld.problems.pubsub;

import com.lld.common.events.InMemoryEventBus;
import com.lld.problems.pubsub.model.Message;
import com.lld.problems.pubsub.model.MessagePublishedEvent;
import com.lld.problems.pubsub.model.Subscription;
import com.lld.problems.pubsub.service.PubSubService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PubSubTest {

    private PubSubService pubSub;
    private final List<MessagePublishedEvent> received = new CopyOnWriteArrayList<>();

    @BeforeEach
    void setUp() {
        received.clear();
        pubSub = new PubSubService(new InMemoryEventBus());
        pubSub.createTopic("orders").getValue().orElseThrow();
    }

    @Test
    void subscribersReceivePublishedMessages() {
        pubSub.subscribe("orders", (MessagePublishedEvent e) -> received.add(e)).getValue().orElseThrow();
        pubSub.subscribe("orders", (MessagePublishedEvent e) -> received.add(e)).getValue().orElseThrow();

        assertTrue(pubSub.publish(new Message("orders", "order-42")).isSuccess());

        assertEquals(2, received.size());
        assertEquals("order-42", received.get(0).getMessage().getPayload());
        assertEquals("order-42", received.get(1).getMessage().getPayload());
    }

    @Test
    void unsubscribeStopsDelivery() {
        List<MessagePublishedEvent> local = new ArrayList<>();
        Subscription sub = pubSub.subscribe("orders", (MessagePublishedEvent e) -> local.add(e)).getValue().orElseThrow();
        pubSub.subscribe("orders", (MessagePublishedEvent e) -> received.add(e)).getValue().orElseThrow();

        assertTrue(pubSub.unsubscribe(sub).isSuccess());
        assertTrue(pubSub.publish(new Message("orders", "after-unsub")).isSuccess());

        assertTrue(local.isEmpty());
        assertEquals(1, received.size());
    }

    @Test
    void rejectUnknownTopic() {
        assertTrue(pubSub.publish(new Message("unknown", "x")).getError().isPresent());
    }
}
