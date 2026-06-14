package com.lld.problems.stockexchange.model;

import com.lld.common.events.Event;

public class TradeExecutedEvent extends Event {

    public static String topicFor(String symbol) {
        return "trade." + symbol;
    }

    private final Trade trade;

    public TradeExecutedEvent(Trade trade) {
        super(topicFor(trade.getSymbol()));
        this.trade = trade;
    }

    public Trade getTrade() {
        return trade;
    }
}
