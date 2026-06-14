package com.lld.problems.splitwise.model;

public class Settlement {

    private final String fromUserId;
    private final String toUserId;
    private final long amountCents;

    public Settlement(String fromUserId, String toUserId, long amountCents) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amountCents = amountCents;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public long getAmountCents() {
        return amountCents;
    }
}
