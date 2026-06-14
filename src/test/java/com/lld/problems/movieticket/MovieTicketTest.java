package com.lld.problems.movieticket;

import com.lld.problems.movieticket.model.Seat;
import com.lld.problems.movieticket.model.SeatLock;
import com.lld.problems.movieticket.model.Show;
import com.lld.problems.movieticket.service.MovieBookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MovieTicketTest {

    private MovieBookingService booking;

    @BeforeEach
    void setUp() {
        booking = new MovieBookingService();
        booking.createShow(new Show("S1", "Matrix", List.of(new Seat("A1"), new Seat("A2"))));
    }

    @Test
    void lockAndConfirmBooking() {
        SeatLock lock = booking.lockSeats("S1", List.of("A1"), "user1").getValue().orElseThrow();
        assertTrue(booking.confirmBooking(lock.getLockId()).isSuccess());
        assertEquals(1, booking.getAvailableSeats("S1").size());
    }

    @Test
    void rejectDoubleLockOnSameSeat() {
        booking.lockSeats("S1", List.of("A1"), "user1").getValue().orElseThrow();
        assertTrue(booking.lockSeats("S1", List.of("A1"), "user2").getError().isPresent());
    }
}
