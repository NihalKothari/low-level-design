package com.lld.problems.fooddelivery.model;

public class Restaurant {

    private final String restaurantId;
    private final String name;
    private boolean open;

    public Restaurant(String restaurantId, String name, boolean open) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.open = open;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
