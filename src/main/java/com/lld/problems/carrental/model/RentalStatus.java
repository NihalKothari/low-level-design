package com.lld.problems.carrental.model;

public enum RentalStatus {
    RESERVED,
    ACTIVE,
    COMPLETED,
    CANCELLED;

    public boolean canTransitionTo(RentalStatus next) {
        if (this == CANCELLED || this == COMPLETED) {
            return false;
        }
        return switch (this) {
            case RESERVED -> next == ACTIVE || next == CANCELLED;
            case ACTIVE -> next == COMPLETED;
            default -> false;
        };
    }
}
