package com.lld.problems.hotel.demo;

import com.lld.problems.hotel.model.Booking;
import com.lld.problems.hotel.model.Room;
import com.lld.problems.hotel.model.RoomType;
import com.lld.problems.hotel.service.HotelBookingService;
import java.time.LocalDate;

public class HotelDemo {

    public static void main(String[] args) {
        HotelBookingService hotel = new HotelBookingService();
        Room room = new Room(101, RoomType.DOUBLE);
        hotel.addRoom(room);

        LocalDate checkIn = LocalDate.of(2026, 6, 10);
        LocalDate checkOut = LocalDate.of(2026, 6, 12);
        Booking booking = hotel.bookRoom(room, "Alice", checkIn, checkOut).getValue().orElseThrow();
        System.out.println("Booked room " + room.getRoomNumber() + " for " + booking.getGuestName());
    }
}
