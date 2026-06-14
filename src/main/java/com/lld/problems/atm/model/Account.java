package com.lld.problems.atm.model;

public class Account {

    private final String id;
    private long balanceCents;

    public Account(String id, long initialBalanceCents) {
        this.id = id;
        this.balanceCents = initialBalanceCents;
    }

    public String getId() {
        return id;
    }

    public long getBalanceCents() {
        return balanceCents;
    }

    public boolean debit(long amountCents) {
        if (amountCents <= 0 || balanceCents < amountCents) {
            return false;
        }
        balanceCents -= amountCents;
        return true;
    }

    public void credit(long amountCents) {
        if (amountCents > 0) {
            balanceCents += amountCents;
        }
    }
}
