package com.lld.problems.orderpipeline.service;

import com.lld.common.events.EventBus;
import com.lld.problems.orderpipeline.model.InventoryReservedEvent;
import com.lld.problems.orderpipeline.model.PipelineStatus;
import com.lld.problems.orderpipeline.model.ShipmentScheduledEvent;
import java.util.Map;
import java.util.UUID;

public class ShipmentHandler {

    private final EventBus eventBus;
    private final Map<String, PipelineStatus> statusByOrder;

    public ShipmentHandler(EventBus eventBus, Map<String, PipelineStatus> statusByOrder) {
        this.eventBus = eventBus;
        this.statusByOrder = statusByOrder;
    }

    public void register() {
        eventBus.subscribe(InventoryReservedEvent.TOPIC, this::onInventoryReserved);
    }

    private void onInventoryReserved(InventoryReservedEvent event) {
        String orderId = event.getOrderId();
        // TODO: schedule carrier pickup
        statusByOrder.put(orderId, PipelineStatus.SHIPMENT_SCHEDULED);
        eventBus.publish(new ShipmentScheduledEvent(orderId, UUID.randomUUID().toString()));
    }
}
