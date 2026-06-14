package com.lld.problems.elevator.service;

import com.lld.common.ErrorCode;
import com.lld.common.Result;
import com.lld.problems.elevator.model.Elevator;
import com.lld.problems.elevator.model.ElevatorState;
import com.lld.problems.elevator.model.FloorRequest;
import com.lld.problems.elevator.strategy.ScanScheduler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ElevatorSystemService {

    private final Map<Integer, Elevator> elevators = new HashMap<>();
    private final List<FloorRequest> pendingRequests = new ArrayList<>();
    private final ScanScheduler scheduler = new ScanScheduler();

    public void registerElevator(Elevator elevator) {
        elevators.put(elevator.getId(), elevator);
    }

    public Result<Void> requestFloor(FloorRequest request) {
        if (request.getFloor() < 0) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }
        pendingRequests.add(request);
        return Result.success(null);
    }

    public Optional<Elevator> getElevator(int id) {
        return Optional.ofNullable(elevators.get(id));
    }

    public void step() {
        for (Elevator elevator : elevators.values()) {
            Optional<Integer> nextStop = scheduler.selectNextStop(elevator, pendingRequests);
            if (nextStop.isEmpty()) {
                elevator.setIdle();
                continue;
            }

            int target = nextStop.get();
            elevator.moveToward(target);

            if (elevator.getCurrentFloor() == target && elevator.getState() == ElevatorState.DOORS_OPEN) {
                pendingRequests.removeIf(r -> r.getFloor() == target);
                elevator.setIdle();
            }
        }
    }

    public int getPendingRequestCount() {
        return pendingRequests.size();
    }
}
