package com.lld.problems.ridehailing.model;

public class Trip {

    private final String id;
    private final Rider rider;
    private final Location pickup;
    private final Location dropoff;
    private Driver driver;
    private TripStatus status;

    public Trip(String id, Rider rider, Location pickup, Location dropoff) {
        this.id = id;
        this.rider = rider;
        this.pickup = pickup;
        this.dropoff = dropoff;
        this.status = TripStatus.REQUESTED;
    }

    public String getId() {
        return id;
    }

    public Rider getRider() {
        return rider;
    }

    public Location getPickup() {
        return pickup;
    }

    public Location getDropoff() {
        return dropoff;
    }

    public Driver getDriver() {
        return driver;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void assignDriver(Driver driver) {
        this.driver = driver;
        this.status = TripStatus.ACCEPTED;
    }

    public void start() {
        this.status = TripStatus.IN_PROGRESS;
    }

    public void complete() {
        this.status = TripStatus.COMPLETED;
    }
}
