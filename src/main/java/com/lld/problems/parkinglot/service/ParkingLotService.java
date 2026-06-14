package com.lld.problems.parkinglot.service;

import com.lld.common.ErrorCode;
import com.lld.common.Result;
import com.lld.problems.parkinglot.model.Floor;
import com.lld.problems.parkinglot.model.ParkingSpot;
import com.lld.problems.parkinglot.model.Ticket;
import com.lld.problems.parkinglot.model.Vehicle;
import com.lld.problems.parkinglot.model.VehicleType;
import com.lld.problems.parkinglot.strategy.FeeStrategy;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ParkingLotService {

    private final List<Floor> floors = new ArrayList<>();
    private final FeeStrategy feeStrategy;

    public ParkingLotService(FeeStrategy feeStrategy) {
        this.feeStrategy = feeStrategy;
    }

    public void addFloor(Floor floor) {
        floors.add(floor);
    }

    public Result<Ticket> parkVehicle(Vehicle vehicle) {
        for (Floor floor : floors) {
            Optional<ParkingSpot> spot = floor.findAvailableSpot(vehicle);
            if (spot.isPresent()) {
                spot.get().park(vehicle);
                Ticket ticket = new Ticket(
                        UUID.randomUUID().toString(),
                        vehicle,
                        spot.get(),
                        Instant.now()
                );
                return Result.success(ticket);
            }
        }
        return Result.failure(ErrorCode.CONFLICT);
    }

    public Result<Double> unparkVehicle(Ticket ticket) {
        ParkingSpot spot = ticket.getSpot();
        if (!spot.isAvailable()) {
            spot.unpark();
            double fee = feeStrategy.calculateFee(
                    ticket.getVehicle().getType(),
                    ticket.getEntryTime(),
                    Instant.now()
            );
            return Result.success(fee);
        }
        return Result.failure(ErrorCode.INVALID_INPUT);
    }

    public long getAvailableSpots(VehicleType type) {
        return floors.stream().mapToLong(f -> f.countAvailable(type)).sum();
    }
}
