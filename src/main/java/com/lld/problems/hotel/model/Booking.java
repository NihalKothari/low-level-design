package com.lld.problems.hotel.model;

import java.time.LocalDate;

public class Booking {

    private final String id;
    private final Room room;
    private final String guestName;
    private final LocalDate checkIn;
    private final LocalDate checkOut;

    public Booking(String id, Room room, String guestName, LocalDate checkIn, LocalDate checkOut) {
        this.id = id;
        this.room = room;
        this.guestName = guestName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public String getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    public String getGuestName() {
        return guestName;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public boolean overlaps(LocalDate start, LocalDate end) {
        return checkIn.isBefore(end) && start.isBefore(checkOut);
    }
}
