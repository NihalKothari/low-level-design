package com.lld.problems.parkinglot.strategy;

import com.lld.problems.parkinglot.model.VehicleType;
import java.time.Duration;
import java.time.Instant;

public interface FeeStrategy {
    double calculateFee(VehicleType type, Instant entry, Instant exit);
}
