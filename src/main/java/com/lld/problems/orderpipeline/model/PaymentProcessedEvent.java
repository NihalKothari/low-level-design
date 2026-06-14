package com.lld.problems.orderpipeline.model;

import com.lld.common.events.Event;

public class PaymentProcessedEvent extends Event {

    public static final String TOPIC = "order.payment";

    private final String orderId;

    public PaymentProcessedEvent(String orderId) {
        super(TOPIC);
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}
