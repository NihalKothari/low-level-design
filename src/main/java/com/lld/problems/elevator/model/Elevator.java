package com.lld.problems.elevator.model;

public class Elevator {

    private final int id;
    private int currentFloor;
    private ElevatorState state;
    private Direction direction;

    public Elevator(int id, int startFloor) {
        this.id = id;
        this.currentFloor = startFloor;
        this.state = ElevatorState.IDLE;
        this.direction = Direction.NONE;
    }

    public int getId() {
        return id;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public ElevatorState getState() {
        return state;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void moveToward(int targetFloor) {
        if (currentFloor < targetFloor) {
            currentFloor++;
            direction = Direction.UP;
            state = ElevatorState.MOVING;
        } else if (currentFloor > targetFloor) {
            currentFloor--;
            direction = Direction.DOWN;
            state = ElevatorState.MOVING;
        } else {
            direction = Direction.NONE;
            state = ElevatorState.DOORS_OPEN;
        }
    }

    public void setIdle() {
        state = ElevatorState.IDLE;
        direction = Direction.NONE;
    }
}
