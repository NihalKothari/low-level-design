package com.lld.problems.orderpipeline.model;

import com.lld.common.events.Event;

public class OrderPlacedEvent extends Event {

    public static final String TOPIC = "order.placed";

    private final PipelineOrder order;

    public OrderPlacedEvent(PipelineOrder order) {
        super(TOPIC);
        this.order = order;
    }

    public PipelineOrder getOrder() {
        return order;
    }
}
