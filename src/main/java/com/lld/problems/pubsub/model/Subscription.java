package com.lld.problems.pubsub.model;

import com.lld.common.events.Subscriber;
import java.util.Objects;
import java.util.UUID;

public class Subscription {

    private final String subscriptionId;
    private final String topicName;
    private final Subscriber<MessagePublishedEvent> subscriber;

    public Subscription(String topicName, Subscriber<MessagePublishedEvent> subscriber) {
        this(UUID.randomUUID().toString(), topicName, subscriber);
    }

    public Subscription(String subscriptionId, String topicName, Subscriber<MessagePublishedEvent> subscriber) {
        this.subscriptionId = Objects.requireNonNull(subscriptionId, "subscriptionId");
        this.topicName = Objects.requireNonNull(topicName, "topicName");
        this.subscriber = Objects.requireNonNull(subscriber, "subscriber");
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public String getTopicName() {
        return topicName;
    }

    public Subscriber<MessagePublishedEvent> getSubscriber() {
        return subscriber;
    }

    @Override
    public String toString() {
        return "Subscription{" + subscriptionId + ", topic=" + topicName + "}";
    }
}
