package com.lld.problems.movieticket.service;

import com.lld.common.ErrorCode;
import com.lld.common.Result;
import com.lld.problems.movieticket.model.Seat;
import com.lld.problems.movieticket.model.SeatLock;
import com.lld.problems.movieticket.model.Show;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MovieBookingService {

    private final Map<String, Show> shows = new HashMap<>();
    private final Map<String, SeatLock> locks = new HashMap<>();

    public void createShow(Show show) {
        shows.put(show.getId(), show);
    }

    public Result<SeatLock> lockSeats(String showId, List<String> seatIds, String userId) {
        Show show = shows.get(showId);
        if (show == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        for (String seatId : seatIds) {
            Seat seat = show.getSeat(seatId);
            if (seat == null || !seat.isAvailable()) {
                return Result.failure(ErrorCode.CONFLICT);
            }
        }
        for (String seatId : seatIds) {
            show.getSeat(seatId).lock(userId);
        }
        SeatLock lock = new SeatLock(
                UUID.randomUUID().toString(),
                showId,
                seatIds,
                userId,
                Instant.now().plus(5, ChronoUnit.MINUTES)
        );
        locks.put(lock.getLockId(), lock);
        return Result.success(lock);
    }

    public Result<Void> confirmBooking(String lockId) {
        SeatLock lock = locks.get(lockId);
        if (lock == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        if (lock.isExpired()) {
            releaseLock(lockId);
            return Result.failure(ErrorCode.OPERATION_FAILED);
        }
        Show show = shows.get(lock.getShowId());
        for (String seatId : lock.getSeatIds()) {
            Seat seat = show.getSeat(seatId);
            if (!seat.isLocked() || !lock.getUserId().equals(seat.getLockedBy())) {
                return Result.failure(ErrorCode.CONFLICT);
            }
            seat.book();
        }
        locks.remove(lockId);
        return Result.success(null);
    }

    public Result<Void> releaseLock(String lockId) {
        SeatLock lock = locks.get(lockId);
        if (lock == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        Show show = shows.get(lock.getShowId());
        for (String seatId : lock.getSeatIds()) {
            Seat seat = show.getSeat(seatId);
            if (seat != null && seat.isLocked()) {
                seat.unlock();
            }
        }
        locks.remove(lockId);
        return Result.success(null);
    }

    public List<Seat> getAvailableSeats(String showId) {
        Show show = shows.get(showId);
        if (show == null) {
            return List.of();
        }
        return show.getAvailableSeats();
    }
}
