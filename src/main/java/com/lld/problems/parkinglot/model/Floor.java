package com.lld.problems.parkinglot.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Floor {

    private final int floorNumber;
    private final List<ParkingSpot> spots = new ArrayList<>();

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public void addSpot(ParkingSpot spot) {
        spots.add(spot);
    }

    public Optional<ParkingSpot> findAvailableSpot(Vehicle vehicle) {
        return spots.stream()
                .filter(ParkingSpot::isAvailable)
                .filter(s -> s.canFit(vehicle))
                .findFirst();
    }

    public long countAvailable(VehicleType type) {
        return spots.stream()
                .filter(ParkingSpot::isAvailable)
                .filter(s -> s.getSpotType() == type)
                .count();
    }

    public int getFloorNumber() {
        return floorNumber;
    }
}
