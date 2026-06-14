package com.lld.problems.carrental.model;

public class Vehicle {

    private final String vehicleId;
    private final String make;
    private final String model;
    private final VehicleType type;
    private final double dailyRate;

    public Vehicle(String vehicleId, String make, String model, VehicleType type, double dailyRate) {
        this.vehicleId = vehicleId;
        this.make = make;
        this.model = model;
        this.type = type;
        this.dailyRate = dailyRate;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public VehicleType getType() {
        return type;
    }

    public double getDailyRate() {
        return dailyRate;
    }
}
