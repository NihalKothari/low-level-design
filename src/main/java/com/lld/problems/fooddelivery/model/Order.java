package com.lld.problems.fooddelivery.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {

    private final String orderId;
    private final String customerId;
    private final String restaurantId;
    private final List<String> items;
    private final double totalAmount;
    private final Instant createdAt;
    private OrderStatus status;
    private String deliveryAgentId;

    public Order(
            String orderId,
            String customerId,
            String restaurantId,
            List<String> items,
            double totalAmount,
            Instant createdAt
    ) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.items = new ArrayList<>(items);
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.status = OrderStatus.PLACED;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public List<String> getItems() {
        return Collections.unmodifiableList(items);
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getDeliveryAgentId() {
        return deliveryAgentId;
    }

    public void setDeliveryAgentId(String deliveryAgentId) {
        this.deliveryAgentId = deliveryAgentId;
    }
}
