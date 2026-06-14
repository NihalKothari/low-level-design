package com.lld.problems.movieticket.demo;

import com.lld.problems.movieticket.model.Seat;
import com.lld.problems.movieticket.model.SeatLock;
import com.lld.problems.movieticket.model.Show;
import com.lld.problems.movieticket.service.MovieBookingService;
import java.util.List;

public class MovieTicketDemo {

    public static void main(String[] args) {
        MovieBookingService booking = new MovieBookingService();
        Show show = new Show("S1", "Inception", List.of(new Seat("A1"), new Seat("A2")));
        booking.createShow(show);

        SeatLock lock = booking.lockSeats("S1", List.of("A1"), "user1").getValue().orElseThrow();
        if (!booking.confirmBooking(lock.getLockId()).isSuccess()) {
            throw new IllegalStateException("Booking confirmation failed");
        }
        System.out.println("Booked seat A1. Remaining: " + booking.getAvailableSeats("S1").size());
    }
}
