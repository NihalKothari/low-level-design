package com.lld.problems.stockexchange.demo;

import com.lld.common.events.InMemoryEventBus;
import com.lld.problems.stockexchange.model.Order;
import com.lld.problems.stockexchange.model.OrderSide;
import com.lld.problems.stockexchange.model.PriceTickEvent;
import com.lld.problems.stockexchange.model.TradeExecutedEvent;
import com.lld.problems.stockexchange.service.StockExchangeService;

public class StockExchangeDemo {

    public static void main(String[] args) {
        InMemoryEventBus bus = new InMemoryEventBus();
        StockExchangeService exchange = new StockExchangeService(bus);

        bus.subscribe(TradeExecutedEvent.topicFor("AAPL"), (TradeExecutedEvent e) ->
                System.out.println("Trade: " + e.getTrade().getQuantity() + " @ " + e.getTrade().getPrice()));
        bus.subscribe(PriceTickEvent.topicFor("AAPL"), (PriceTickEvent e) ->
                System.out.println("Tick: " + e.getLastPrice()));

        exchange.placeOrder(new Order("AAPL", OrderSide.SELL, 10, 150.0));
        exchange.placeOrder(new Order("AAPL", OrderSide.BUY, 10, 150.0));
    }
}
