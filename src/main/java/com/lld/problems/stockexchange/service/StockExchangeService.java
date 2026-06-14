package com.lld.problems.stockexchange.service;

import com.lld.common.events.EventBus;
import com.lld.problems.stockexchange.model.Order;
import com.lld.problems.stockexchange.model.Trade;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StockExchangeService {

    private final EventBus eventBus;
    private final Map<String, OrderBook> orderBooks = new ConcurrentHashMap<>();

    public StockExchangeService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public OrderBook getOrderBook(String symbol) {
        return orderBooks.computeIfAbsent(symbol, s -> new OrderBook(s, eventBus));
    }

    public List<Trade> placeOrder(Order order) {
        return getOrderBook(order.getSymbol()).addOrder(order);
    }
}
