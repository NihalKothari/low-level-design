package com.lld.problems.fooddelivery.model;

import java.util.EnumSet;
import java.util.Set;

public enum OrderStatus {
    PLACED,
    CONFIRMED,
    PREPARING,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED;

    private static final Set<OrderStatus> CANCELLABLE = EnumSet.of(PLACED, CONFIRMED);

    public boolean canTransitionTo(OrderStatus next) {
        if (this == CANCELLED || this == DELIVERED) {
            return false;
        }
        return switch (this) {
            case PLACED -> next == CONFIRMED || next == CANCELLED;
            case CONFIRMED -> next == PREPARING || next == CANCELLED;
            case PREPARING -> next == OUT_FOR_DELIVERY;
            case OUT_FOR_DELIVERY -> next == DELIVERED;
            default -> false;
        };
    }

    public boolean isCancellable() {
        return CANCELLABLE.contains(this);
    }
}
