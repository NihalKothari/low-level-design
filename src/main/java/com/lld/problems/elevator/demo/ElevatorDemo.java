package com.lld.problems.elevator.demo;

import com.lld.problems.elevator.model.Direction;
import com.lld.problems.elevator.model.Elevator;
import com.lld.problems.elevator.model.FloorRequest;
import com.lld.problems.elevator.service.ElevatorSystemService;

public class ElevatorDemo {

    public static void main(String[] args) {
        ElevatorSystemService system = new ElevatorSystemService();
        system.registerElevator(new Elevator(1, 0));

        if (!system.requestFloor(new FloorRequest(3, Direction.UP)).isSuccess()) {
            throw new IllegalStateException("Floor request failed");
        }

        while (system.getPendingRequestCount() > 0) {
            system.step();
        }

        Elevator elevator = system.getElevator(1).orElseThrow();
        System.out.println("Elevator arrived at floor: " + elevator.getCurrentFloor());
    }
}
