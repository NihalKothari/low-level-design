package com.lld.problems.stockexchange;

import com.lld.common.events.InMemoryEventBus;
import com.lld.problems.stockexchange.model.Order;
import com.lld.problems.stockexchange.model.OrderSide;
import com.lld.problems.stockexchange.model.PriceTickEvent;
import com.lld.problems.stockexchange.model.TradeExecutedEvent;
import com.lld.problems.stockexchange.service.StockExchangeService;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class StockExchangeTest {

    private StockExchangeService exchange;
    private final List<TradeExecutedEvent> trades = new CopyOnWriteArrayList<>();
    private final List<PriceTickEvent> ticks = new CopyOnWriteArrayList<>();

    @BeforeEach
    void setUp() {
        InMemoryEventBus bus = new InMemoryEventBus();
        exchange = new StockExchangeService(bus);
        bus.subscribe(TradeExecutedEvent.topicFor("AAPL"), (TradeExecutedEvent e) -> trades.add(e));
        bus.subscribe(PriceTickEvent.topicFor("AAPL"), (PriceTickEvent e) -> ticks.add(e));
    }

    @Test
    void subscribersReceiveTradeAndTickEvents() {
        exchange.placeOrder(new Order("AAPL", OrderSide.SELL, 5, 100.0));
        exchange.placeOrder(new Order("AAPL", OrderSide.BUY, 5, 100.0));

        assertEquals(1, trades.size());
        assertEquals(1, ticks.size());
        assertEquals(100.0, trades.get(0).getTrade().getPrice());
        assertEquals(100.0, ticks.get(0).getLastPrice());
    }

    @Test
    void noTradeWhenPricesDoNotCross() {
        exchange.placeOrder(new Order("AAPL", OrderSide.SELL, 5, 110.0));
        exchange.placeOrder(new Order("AAPL", OrderSide.BUY, 5, 100.0));

        assertEquals(0, trades.size());
        assertEquals(0, ticks.size());
        assertFalse(exchange.getOrderBook("AAPL").getBestBid().isEmpty());
    }
}
