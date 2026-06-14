package com.lld.problems.movieticket.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Show {

    private final String id;
    private final String movieName;
    private final Map<String, Seat> seats = new HashMap<>();

    public Show(String id, String movieName, List<Seat> seatList) {
        this.id = id;
        this.movieName = movieName;
        for (Seat seat : seatList) {
            seats.put(seat.getId(), seat);
        }
    }

    public String getId() {
        return id;
    }

    public String getMovieName() {
        return movieName;
    }

    public Seat getSeat(String seatId) {
        return seats.get(seatId);
    }

    public List<Seat> getAvailableSeats() {
        return seats.values().stream().filter(Seat::isAvailable).collect(Collectors.toList());
    }
}
