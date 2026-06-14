package com.lld.problems.orderpipeline.service;

import com.lld.common.events.EventBus;
import com.lld.problems.orderpipeline.model.OrderPlacedEvent;
import com.lld.problems.orderpipeline.model.PaymentProcessedEvent;
import com.lld.problems.orderpipeline.model.PipelineStatus;
import java.util.Map;

public class PaymentHandler {

    private final EventBus eventBus;
    private final Map<String, PipelineStatus> statusByOrder;

    public PaymentHandler(EventBus eventBus, Map<String, PipelineStatus> statusByOrder) {
        this.eventBus = eventBus;
        this.statusByOrder = statusByOrder;
    }

    public void register() {
        eventBus.subscribe(OrderPlacedEvent.TOPIC, this::onOrderPlaced);
    }

    private void onOrderPlaced(OrderPlacedEvent event) {
        String orderId = event.getOrder().getOrderId();
        // TODO: charge payment gateway
        statusByOrder.put(orderId, PipelineStatus.PAYMENT_PROCESSED);
        event.getOrder().setStatus(PipelineStatus.PAYMENT_PROCESSED);
        eventBus.publish(new PaymentProcessedEvent(orderId));
    }
}
