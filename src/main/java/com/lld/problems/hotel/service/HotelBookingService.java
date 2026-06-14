package com.lld.problems.hotel.service;

import com.lld.common.ErrorCode;
import com.lld.common.Result;
import com.lld.problems.hotel.model.Booking;
import com.lld.problems.hotel.model.Room;
import com.lld.problems.hotel.model.RoomType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class HotelBookingService {

    private final Map<Integer, Room> rooms = new HashMap<>();
    private final Map<String, Booking> bookings = new HashMap<>();

    public void addRoom(Room room) {
        rooms.put(room.getRoomNumber(), room);
    }

    public List<Room> findAvailableRooms(RoomType type, LocalDate checkIn, LocalDate checkOut) {
        return rooms.values().stream()
                .filter(r -> r.getType() == type)
                .filter(r -> r.isAvailableFor(checkIn, checkOut))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Result<Booking> bookRoom(Room room, String guestName, LocalDate checkIn, LocalDate checkOut) {
        if (!checkIn.isBefore(checkOut)) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }
        if (!room.isAvailableFor(checkIn, checkOut)) {
            return Result.failure(ErrorCode.CONFLICT);
        }
        Booking booking = new Booking(UUID.randomUUID().toString(), room, guestName, checkIn, checkOut);
        room.addBooking(booking);
        bookings.put(booking.getId(), booking);
        return Result.success(booking);
    }

    public Result<Void> cancelBooking(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        booking.getRoom().removeBooking(booking);
        bookings.remove(bookingId);
        return Result.success(null);
    }

    public Result<Booking> getBooking(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        return Result.success(booking);
    }
}
