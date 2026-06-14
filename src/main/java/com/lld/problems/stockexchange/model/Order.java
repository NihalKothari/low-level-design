package com.lld.problems.stockexchange.model;

import java.util.Objects;
import java.util.UUID;

public class Order {

    private final String orderId;
    private final String symbol;
    private final OrderSide side;
    private int quantity;
    private final double price;

    public Order(String symbol, OrderSide side, int quantity, double price) {
        this(UUID.randomUUID().toString(), symbol, side, quantity, price);
    }

    public Order(String orderId, String symbol, OrderSide side, int quantity, double price) {
        this.orderId = Objects.requireNonNull(orderId, "orderId");
        this.symbol = Objects.requireNonNull(symbol, "symbol");
        this.side = Objects.requireNonNull(side, "side");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("price must be positive");
        }
        this.quantity = quantity;
        this.price = price;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getSymbol() {
        return symbol;
    }

    public OrderSide getSide() {
        return side;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public void reduceQuantity(int fillQty) {
        if (fillQty <= 0 || fillQty > quantity) {
            throw new IllegalArgumentException("invalid fill quantity");
        }
        quantity -= fillQty;
    }

    public boolean isFilled() {
        return quantity == 0;
    }
}
