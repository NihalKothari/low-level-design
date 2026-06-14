package com.lld.problems.vendingmachine.model;

public enum Coin {
    PENNY(1),
    NICKEL(5),
    DIME(10),
    QUARTER(25),
    DOLLAR(100);

    private final int valueCents;

    Coin(int valueCents) {
        this.valueCents = valueCents;
    }

    public int getValueCents() {
        return valueCents;
    }
}
