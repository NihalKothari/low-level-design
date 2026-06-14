package com.lld.problems.hotel;

import com.lld.problems.hotel.model.Booking;
import com.lld.problems.hotel.model.Room;
import com.lld.problems.hotel.model.RoomType;
import com.lld.problems.hotel.service.HotelBookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HotelTest {

    private HotelBookingService hotel;
    private Room room;
    private LocalDate checkIn;
    private LocalDate checkOut;

    @BeforeEach
    void setUp() {
        hotel = new HotelBookingService();
        room = new Room(101, RoomType.SINGLE);
        hotel.addRoom(room);
        checkIn = LocalDate.of(2026, 7, 1);
        checkOut = LocalDate.of(2026, 7, 3);
    }

    @Test
    void bookAvailableRoom() {
        Booking booking = hotel.bookRoom(room, "Carol", checkIn, checkOut).getValue().orElseThrow();
        assertEquals("Carol", booking.getGuestName());
        assertEquals(0, hotel.findAvailableRooms(RoomType.SINGLE, checkIn, checkOut).size());
    }

    @Test
    void rejectOverlappingBooking() {
        hotel.bookRoom(room, "Dave", checkIn, checkOut).getValue().orElseThrow();
        assertTrue(hotel.bookRoom(room, "Eve", checkIn.plusDays(1), checkOut.plusDays(1)).getError().isPresent());
    }
}
