package com.lld.problems.splitwise.model;

import java.util.Map;

public class Expense {

    private final String id;
    private final String paidByUserId;
    private final long totalCents;
    private final Map<String, Long> splits;

    public Expense(String id, String paidByUserId, long totalCents, Map<String, Long> splits) {
        this.id = id;
        this.paidByUserId = paidByUserId;
        this.totalCents = totalCents;
        this.splits = Map.copyOf(splits);
    }

    public String getId() {
        return id;
    }

    public String getPaidByUserId() {
        return paidByUserId;
    }

    public long getTotalCents() {
        return totalCents;
    }

    public Map<String, Long> getSplits() {
        return splits;
    }
}
