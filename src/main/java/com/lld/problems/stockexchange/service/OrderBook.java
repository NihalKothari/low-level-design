package com.lld.problems.stockexchange.service;

import com.lld.common.events.EventBus;
import com.lld.problems.stockexchange.model.Order;
import com.lld.problems.stockexchange.model.OrderSide;
import com.lld.problems.stockexchange.model.PriceTickEvent;
import com.lld.problems.stockexchange.model.Trade;
import com.lld.problems.stockexchange.model.TradeExecutedEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

public class OrderBook {

    private final String symbol;
    private final EventBus eventBus;
    private final PriorityQueue<Order> bids = new PriorityQueue<>(
            Comparator.comparing(Order::getPrice).reversed()
                    .thenComparing(Order::getOrderId)
    );
    private final PriorityQueue<Order> asks = new PriorityQueue<>(
            Comparator.comparing(Order::getPrice)
                    .thenComparing(Order::getOrderId)
    );
    private final List<Trade> trades = new ArrayList<>();
    private double lastPrice;

    public OrderBook(String symbol, EventBus eventBus) {
        this.symbol = symbol;
        this.eventBus = eventBus;
    }

    public String getSymbol() {
        return symbol;
    }

    public synchronized List<Trade> addOrder(Order order) {
        if (!symbol.equals(order.getSymbol())) {
            throw new IllegalArgumentException("symbol mismatch");
        }
        List<Trade> executed = new ArrayList<>();
        if (order.getSide() == OrderSide.BUY) {
            matchBuy(order, executed);
            if (!order.isFilled()) {
                bids.add(order);
            }
        } else {
            matchSell(order, executed);
            if (!order.isFilled()) {
                asks.add(order);
            }
        }
        for (Trade trade : executed) {
            trades.add(trade);
            eventBus.publish(new TradeExecutedEvent(trade));
            lastPrice = trade.getPrice();
            eventBus.publish(new PriceTickEvent(symbol, lastPrice, trade.getQuantity()));
        }
        return executed;
    }

    private void matchBuy(Order buy, List<Trade> executed) {
        while (!buy.isFilled() && !asks.isEmpty()) {
            Order bestAsk = asks.peek();
            if (buy.getPrice() < bestAsk.getPrice()) {
                break;
            }
            executeMatch(buy, bestAsk, executed);
        }
    }

    private void matchSell(Order sell, List<Trade> executed) {
        while (!sell.isFilled() && !bids.isEmpty()) {
            Order bestBid = bids.peek();
            if (sell.getPrice() > bestBid.getPrice()) {
                break;
            }
            executeMatch(bestBid, sell, executed);
        }
    }

    private void executeMatch(Order buy, Order sell, List<Trade> executed) {
        int fillQty = Math.min(buy.getQuantity(), sell.getQuantity());
        double price = sell.getPrice();
        Trade trade = new Trade(symbol, buy.getOrderId(), sell.getOrderId(), fillQty, price);
        executed.add(trade);
        buy.reduceQuantity(fillQty);
        sell.reduceQuantity(fillQty);
        if (sell.isFilled()) {
            asks.poll();
        }
        if (buy.isFilled()) {
            bids.poll();
        }
    }

    public Optional<Double> getBestBid() {
        return bids.isEmpty() ? Optional.empty() : Optional.of(bids.peek().getPrice());
    }

    public Optional<Double> getBestAsk() {
        return asks.isEmpty() ? Optional.empty() : Optional.of(asks.peek().getPrice());
    }

    public List<Trade> getTrades() {
        return List.copyOf(trades);
    }

    public double getLastPrice() {
        return lastPrice;
    }
}
