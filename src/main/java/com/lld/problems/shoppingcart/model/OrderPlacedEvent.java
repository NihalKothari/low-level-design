package com.lld.problems.shoppingcart.model;

import com.lld.common.events.Event;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderPlacedEvent extends Event {

    public static final String TOPIC = "order.placed";

    private final String orderId;
    private final String userId;
    private final Map<String, Integer> lineItems;
    private final double subtotal;
    private final double discount;
    private final double total;

    public OrderPlacedEvent(
            String orderId,
            String userId,
            Map<String, Integer> lineItems,
            double subtotal,
            double discount,
            double total
    ) {
        super(TOPIC);
        this.orderId = orderId;
        this.userId = userId;
        this.lineItems = lineItems;
        this.subtotal = subtotal;
        this.discount = discount;
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public Map<String, Integer> getLineItems() {
        return Collections.unmodifiableMap(lineItems);
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getDiscount() {
        return discount;
    }

    public double getTotal() {
        return total;
    }

    public List<String> getProductIds() {
        return lineItems.keySet().stream().sorted().collect(Collectors.toList());
    }
}
