package com.lld.problems.ridehailing.model;

public class Driver {

    private final String id;
    private final String name;
    private Location location;
    private boolean available;

    public Driver(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.available = true;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
