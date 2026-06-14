package com.lld.problems.stockexchange.model;

import com.lld.common.events.Event;

public class PriceTickEvent extends Event {

    public static String topicFor(String symbol) {
        return "tick." + symbol;
    }

    private final String symbol;
    private final double lastPrice;
    private final int volume;

    public PriceTickEvent(String symbol, double lastPrice, int volume) {
        super(topicFor(symbol));
        this.symbol = symbol;
        this.lastPrice = lastPrice;
        this.volume = volume;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public int getVolume() {
        return volume;
    }
}
