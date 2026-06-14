package com.lld.problems.elevator.model;

public class FloorRequest {

    private final int floor;
    private final Direction direction;

    public FloorRequest(int floor, Direction direction) {
        this.floor = floor;
        this.direction = direction;
    }

    public int getFloor() {
        return floor;
    }

    public Direction getDirection() {
        return direction;
    }
}
