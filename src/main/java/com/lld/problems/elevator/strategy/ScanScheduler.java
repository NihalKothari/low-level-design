package com.lld.problems.elevator.strategy;

import com.lld.problems.elevator.model.Direction;
import com.lld.problems.elevator.model.Elevator;
import com.lld.problems.elevator.model.FloorRequest;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * SCAN (elevator scan) scheduling stub: pick the nearest request in the
 * elevator's current travel direction, otherwise the closest request overall.
 */
public class ScanScheduler {

    public Optional<Integer> selectNextStop(Elevator elevator, List<FloorRequest> pending) {
        if (pending.isEmpty()) {
            return Optional.empty();
        }

        int current = elevator.getCurrentFloor();
        Direction travel = elevator.getDirection();

        if (travel == Direction.UP) {
            return pending.stream()
                    .map(FloorRequest::getFloor)
                    .filter(f -> f >= current)
                    .min(Comparator.naturalOrder());
        }
        if (travel == Direction.DOWN) {
            return pending.stream()
                    .map(FloorRequest::getFloor)
                    .filter(f -> f <= current)
                    .max(Comparator.naturalOrder());
        }

        return pending.stream()
                .min(Comparator.comparingInt(r -> Math.abs(r.getFloor() - current)))
                .map(FloorRequest::getFloor);
    }
}
