package com.lld.problems.orderpipeline.model;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PipelineOrder {

    private final String orderId;
    private final List<String> items;
    private volatile PipelineStatus status;

    public PipelineOrder(List<String> items) {
        this(UUID.randomUUID().toString(), items, PipelineStatus.PLACED);
    }

    public PipelineOrder(String orderId, List<String> items, PipelineStatus status) {
        this.orderId = Objects.requireNonNull(orderId, "orderId");
        this.items = List.copyOf(Objects.requireNonNull(items, "items"));
        if (this.items.isEmpty()) {
            throw new IllegalArgumentException("items required");
        }
        this.status = Objects.requireNonNull(status, "status");
    }

    public String getOrderId() {
        return orderId;
    }

    public List<String> getItems() {
        return items;
    }

    public PipelineStatus getStatus() {
        return status;
    }

    public void setStatus(PipelineStatus status) {
        this.status = status;
    }
}
