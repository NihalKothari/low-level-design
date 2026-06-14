package com.lld.problems.parkinglot.strategy;

import com.lld.problems.parkinglot.model.VehicleType;
import java.time.Duration;
import java.time.Instant;

public class HourlyFeeStrategy implements FeeStrategy {

    @Override
    public double calculateFee(VehicleType type, Instant entry, Instant exit) {
        long hours = Math.max(1, Duration.between(entry, exit).toHours());
        return switch (type) {
            case MOTORCYCLE -> hours * 10.0;
            case CAR -> hours * 20.0;
            case TRUCK -> hours * 30.0;
        };
    }
}
