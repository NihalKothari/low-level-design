package com.lld.problems.pubsub.service;

import com.lld.common.ErrorCode;
import com.lld.common.Result;
import com.lld.common.events.EventBus;
import com.lld.common.events.Subscriber;
import com.lld.problems.pubsub.model.Message;
import com.lld.problems.pubsub.model.MessagePublishedEvent;
import com.lld.problems.pubsub.model.Subscription;
import com.lld.problems.pubsub.model.Topic;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Facade over {@link EventBus} with topic registry and subscription handles.
 */
public class PubSubService {

    private final EventBus eventBus;
    private final Map<String, Topic> topics = new ConcurrentHashMap<>();
    private final Map<String, Subscription> subscriptions = new ConcurrentHashMap<>();

    public PubSubService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public Result<Topic> createTopic(String name) {
        if (topics.containsKey(name)) {
            return Result.failure(ErrorCode.CONFLICT);
        }
        Topic topic = new Topic(name);
        topics.put(name, topic);
        return Result.success(topic);
    }

    public Result<Subscription> subscribe(String topicName, Subscriber<MessagePublishedEvent> subscriber) {
        if (!topics.containsKey(topicName)) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        Subscription subscription = new Subscription(topicName, subscriber);
        eventBus.subscribe(MessagePublishedEvent.TOPIC_PREFIX + topicName, subscriber);
        subscriptions.put(subscription.getSubscriptionId(), subscription);
        return Result.success(subscription);
    }

    public Result<Void> unsubscribe(Subscription subscription) {
        if (!subscriptions.containsKey(subscription.getSubscriptionId())) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        eventBus.unsubscribe(
                MessagePublishedEvent.TOPIC_PREFIX + subscription.getTopicName(),
                subscription.getSubscriber()
        );
        subscriptions.remove(subscription.getSubscriptionId());
        return Result.success(null);
    }

    public Result<Void> publish(Message message) {
        if (!topics.containsKey(message.getTopicName())) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        eventBus.publish(new MessagePublishedEvent(message));
        return Result.success(null);
    }

    public List<String> getTopics() {
        return new ArrayList<>(topics.keySet());
    }

    public int getSubscriptionCount(String topicName) {
        return (int) subscriptions.values().stream()
                .filter(s -> s.getTopicName().equals(topicName))
                .count();
    }
}
