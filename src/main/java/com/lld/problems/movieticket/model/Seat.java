package com.lld.problems.movieticket.model;

public class Seat {

    private final String id;
    private boolean locked;
    private boolean booked;
    private String lockedBy;

    public Seat(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isAvailable() {
        return !locked && !booked;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isBooked() {
        return booked;
    }

    public String getLockedBy() {
        return lockedBy;
    }

    public void lock(String userId) {
        this.locked = true;
        this.lockedBy = userId;
    }

    public void unlock() {
        this.locked = false;
        this.lockedBy = null;
    }

    public void book() {
        this.booked = true;
        this.locked = false;
        this.lockedBy = null;
    }
}
