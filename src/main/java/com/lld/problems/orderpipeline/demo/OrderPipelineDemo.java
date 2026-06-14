package com.lld.problems.orderpipeline.demo;

import com.lld.common.events.InMemoryEventBus;
import com.lld.problems.orderpipeline.model.PipelineOrder;
import com.lld.problems.orderpipeline.model.PipelineStatus;
import com.lld.problems.orderpipeline.model.ShipmentScheduledEvent;
import com.lld.problems.orderpipeline.service.OrderPipelineService;
import java.util.List;

public class OrderPipelineDemo {

    public static void main(String[] args) {
        InMemoryEventBus bus = new InMemoryEventBus();
        OrderPipelineService pipeline = new OrderPipelineService(bus);
        pipeline.startPipeline();

        bus.subscribe(ShipmentScheduledEvent.TOPIC, (ShipmentScheduledEvent e) ->
                System.out.println("Shipped order " + e.getOrderId() + " tracking " + e.getTrackingId()));

        PipelineOrder order = pipeline.placeOrder(List.of("keyboard", "mouse"));
        System.out.println("Final status: " + pipeline.getStatus(order.getOrderId()));
        System.out.println("Expected: " + PipelineStatus.SHIPMENT_SCHEDULED);
    }
}
