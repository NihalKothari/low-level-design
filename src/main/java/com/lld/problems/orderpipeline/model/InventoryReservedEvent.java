package com.lld.problems.orderpipeline.model;

import com.lld.common.events.Event;

public class InventoryReservedEvent extends Event {

    public static final String TOPIC = "order.inventory";

    private final String orderId;

    public InventoryReservedEvent(String orderId) {
        super(TOPIC);
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}
