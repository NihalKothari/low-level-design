package com.lld.problems.hotel.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Room {

    private final int roomNumber;
    private final RoomType type;
    private final List<Booking> bookings = new ArrayList<>();

    public Room(int roomNumber, RoomType type) {
        this.roomNumber = roomNumber;
        this.type = type;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomType getType() {
        return type;
    }

    public boolean isAvailableFor(LocalDate checkIn, LocalDate checkOut) {
        return bookings.stream().noneMatch(b -> b.overlaps(checkIn, checkOut));
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public void removeBooking(Booking booking) {
        bookings.remove(booking);
    }
}
