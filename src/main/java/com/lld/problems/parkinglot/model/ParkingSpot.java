package com.lld.problems.parkinglot.model;

public class ParkingSpot {

    private final String id;
    private final VehicleType spotType;
    private Vehicle parkedVehicle;

    public ParkingSpot(String id, VehicleType spotType) {
        this.id = id;
        this.spotType = spotType;
    }

    public boolean isAvailable() {
        return parkedVehicle == null;
    }

    public boolean canFit(Vehicle vehicle) {
        return spotType == vehicle.getType();
    }

    public void park(Vehicle vehicle) {
        this.parkedVehicle = vehicle;
    }

    public void unpark() {
        this.parkedVehicle = null;
    }

    public String getId() {
        return id;
    }

    public VehicleType getSpotType() {
        return spotType;
    }
}
