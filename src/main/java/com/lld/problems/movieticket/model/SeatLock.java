package com.lld.problems.movieticket.model;

import java.time.Instant;
import java.util.List;

public class SeatLock {

    private final String lockId;
    private final String showId;
    private final List<String> seatIds;
    private final String userId;
    private final Instant expiresAt;

    public SeatLock(String lockId, String showId, List<String> seatIds, String userId, Instant expiresAt) {
        this.lockId = lockId;
        this.showId = showId;
        this.seatIds = List.copyOf(seatIds);
        this.userId = userId;
        this.expiresAt = expiresAt;
    }

    public String getLockId() {
        return lockId;
    }

    public String getShowId() {
        return showId;
    }

    public List<String> getSeatIds() {
        return seatIds;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}
