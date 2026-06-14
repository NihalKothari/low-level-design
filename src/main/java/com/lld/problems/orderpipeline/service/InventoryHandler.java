package com.lld.problems.orderpipeline.service;

import com.lld.common.events.EventBus;
import com.lld.problems.orderpipeline.model.InventoryReservedEvent;
import com.lld.problems.orderpipeline.model.PaymentProcessedEvent;
import com.lld.problems.orderpipeline.model.PipelineStatus;
import java.util.Map;

public class InventoryHandler {

    private final EventBus eventBus;
    private final Map<String, PipelineStatus> statusByOrder;

    public InventoryHandler(EventBus eventBus, Map<String, PipelineStatus> statusByOrder) {
        this.eventBus = eventBus;
        this.statusByOrder = statusByOrder;
    }

    public void register() {
        eventBus.subscribe(PaymentProcessedEvent.TOPIC, this::onPaymentProcessed);
    }

    private void onPaymentProcessed(PaymentProcessedEvent event) {
        String orderId = event.getOrderId();
        // TODO: reserve stock in WMS
        statusByOrder.put(orderId, PipelineStatus.INVENTORY_RESERVED);
        eventBus.publish(new InventoryReservedEvent(orderId));
    }
}
