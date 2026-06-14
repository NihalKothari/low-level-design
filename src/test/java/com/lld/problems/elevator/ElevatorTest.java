package com.lld.problems.elevator;

import com.lld.problems.elevator.model.Direction;
import com.lld.problems.elevator.model.Elevator;
import com.lld.problems.elevator.model.ElevatorState;
import com.lld.problems.elevator.model.FloorRequest;
import com.lld.problems.elevator.service.ElevatorSystemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ElevatorTest {

    private ElevatorSystemService system;

    @BeforeEach
    void setUp() {
        system = new ElevatorSystemService();
        system.registerElevator(new Elevator(1, 0));
    }

    @Test
    void elevatorReachesRequestedFloor() {
        system.requestFloor(new FloorRequest(2, Direction.UP));
        while (system.getPendingRequestCount() > 0) {
            system.step();
        }
        Elevator elevator = system.getElevator(1).orElseThrow();
        assertEquals(2, elevator.getCurrentFloor());
        assertEquals(ElevatorState.IDLE, elevator.getState());
    }

    @Test
    void rejectInvalidFloorRequest() {
        assertTrue(system.requestFloor(new FloorRequest(-1, Direction.UP)).getError().isPresent());
    }
}
