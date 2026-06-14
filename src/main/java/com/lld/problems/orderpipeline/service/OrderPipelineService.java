package com.lld.problems.orderpipeline.service;

import com.lld.common.events.EventBus;
import com.lld.problems.orderpipeline.model.OrderPlacedEvent;
import com.lld.problems.orderpipeline.model.PipelineOrder;
import com.lld.problems.orderpipeline.model.PipelineStatus;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrderPipelineService {

    private final EventBus eventBus;
    private final Map<String, PipelineOrder> orders = new ConcurrentHashMap<>();
    private final Map<String, PipelineStatus> statusByOrder = new ConcurrentHashMap<>();
    private boolean pipelineStarted;

    public OrderPipelineService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void startPipeline() {
        if (pipelineStarted) {
            return;
        }
        new PaymentHandler(eventBus, statusByOrder).register();
        new InventoryHandler(eventBus, statusByOrder).register();
        new ShipmentHandler(eventBus, statusByOrder).register();
        pipelineStarted = true;
    }

    public PipelineOrder placeOrder(List<String> items) {
        PipelineOrder order = new PipelineOrder(items);
        orders.put(order.getOrderId(), order);
        statusByOrder.put(order.getOrderId(), PipelineStatus.PLACED);
        eventBus.publish(new OrderPlacedEvent(order));
        return order;
    }

    public PipelineStatus getStatus(String orderId) {
        return statusByOrder.getOrDefault(orderId, PipelineStatus.PLACED);
    }

    public PipelineOrder getOrder(String orderId) {
        return orders.get(orderId);
    }
}
