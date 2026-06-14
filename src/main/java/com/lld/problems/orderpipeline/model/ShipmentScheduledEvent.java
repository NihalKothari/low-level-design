package com.lld.problems.orderpipeline.model;

import com.lld.common.events.Event;

public class ShipmentScheduledEvent extends Event {

    public static final String TOPIC = "order.shipment";

    private final String orderId;
    private final String trackingId;

    public ShipmentScheduledEvent(String orderId, String trackingId) {
        super(TOPIC);
        this.orderId = orderId;
        this.trackingId = trackingId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getTrackingId() {
        return trackingId;
    }
}
