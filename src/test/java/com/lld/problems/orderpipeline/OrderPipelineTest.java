package com.lld.problems.orderpipeline;

import com.lld.common.events.InMemoryEventBus;
import com.lld.problems.orderpipeline.model.InventoryReservedEvent;
import com.lld.problems.orderpipeline.model.OrderPlacedEvent;
import com.lld.problems.orderpipeline.model.PaymentProcessedEvent;
import com.lld.problems.orderpipeline.model.PipelineOrder;
import com.lld.problems.orderpipeline.model.PipelineStatus;
import com.lld.problems.orderpipeline.model.ShipmentScheduledEvent;
import com.lld.problems.orderpipeline.service.OrderPipelineService;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderPipelineTest {

    private OrderPipelineService pipeline;
    private final List<OrderPlacedEvent> placed = new CopyOnWriteArrayList<>();
    private final List<PaymentProcessedEvent> paid = new CopyOnWriteArrayList<>();
    private final List<InventoryReservedEvent> reserved = new CopyOnWriteArrayList<>();
    private final List<ShipmentScheduledEvent> shipped = new CopyOnWriteArrayList<>();

    @BeforeEach
    void setUp() {
        InMemoryEventBus bus = new InMemoryEventBus();
        pipeline = new OrderPipelineService(bus);
        pipeline.startPipeline();
        bus.subscribe(OrderPlacedEvent.TOPIC, (OrderPlacedEvent e) -> placed.add(e));
        bus.subscribe(PaymentProcessedEvent.TOPIC, (PaymentProcessedEvent e) -> paid.add(e));
        bus.subscribe(InventoryReservedEvent.TOPIC, (InventoryReservedEvent e) -> reserved.add(e));
        bus.subscribe(ShipmentScheduledEvent.TOPIC, (ShipmentScheduledEvent e) -> shipped.add(e));
    }

    @Test
    void sagaPublishesEachStageEvent() {
        PipelineOrder order = pipeline.placeOrder(List.of("item-a"));

        assertEquals(1, placed.size());
        assertEquals(1, paid.size());
        assertEquals(1, reserved.size());
        assertEquals(1, shipped.size());
        assertEquals(order.getOrderId(), placed.get(0).getOrder().getOrderId());
        assertEquals(order.getOrderId(), shipped.get(0).getOrderId());
        assertEquals(PipelineStatus.SHIPMENT_SCHEDULED, pipeline.getStatus(order.getOrderId()));
    }
}
