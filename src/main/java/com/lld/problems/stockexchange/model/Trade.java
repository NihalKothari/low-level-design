package com.lld.problems.stockexchange.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Trade {

    private final String tradeId;
    private final String symbol;
    private final String buyOrderId;
    private final String sellOrderId;
    private final int quantity;
    private final double price;
    private final Instant executedAt;

    public Trade(String symbol, String buyOrderId, String sellOrderId, int quantity, double price) {
        this.tradeId = UUID.randomUUID().toString();
        this.symbol = Objects.requireNonNull(symbol, "symbol");
        this.buyOrderId = Objects.requireNonNull(buyOrderId, "buyOrderId");
        this.sellOrderId = Objects.requireNonNull(sellOrderId, "sellOrderId");
        this.quantity = quantity;
        this.price = price;
        this.executedAt = Instant.now();
    }

    public String getTradeId() {
        return tradeId;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getBuyOrderId() {
        return buyOrderId;
    }

    public String getSellOrderId() {
        return sellOrderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public Instant getExecutedAt() {
        return executedAt;
    }
}
