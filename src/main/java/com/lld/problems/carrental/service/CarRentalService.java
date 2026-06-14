package com.lld.problems.carrental.service;

import com.lld.common.ErrorCode;
import com.lld.common.Result;
import com.lld.common.repository.InMemoryRepository;
import com.lld.problems.carrental.model.RentalStatus;
import com.lld.problems.carrental.model.Reservation;
import com.lld.problems.carrental.model.Vehicle;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CarRentalService {

    private final InMemoryRepository<Vehicle> vehicles = new InMemoryRepository<>(Vehicle::getVehicleId);
    private final InMemoryRepository<Reservation> reservations =
            new InMemoryRepository<>(Reservation::getReservationId);

    public void addVehicle(Vehicle vehicle) {
        vehicles.save(vehicle);
    }

    public List<Vehicle> searchAvailable(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            return List.of();
        }

        List<Vehicle> available = new ArrayList<>();
        for (Vehicle vehicle : vehicles.findAll()) {
            if (isVehicleAvailable(vehicle.getVehicleId(), startDate, endDate)) {
                available.add(vehicle);
            }
        }
        return available;
    }

    public Result<Reservation> reserve(
            String userId,
            String vehicleId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (endDate.isBefore(startDate)) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }
        if (vehicles.findById(vehicleId).isEmpty()) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        if (!isVehicleAvailable(vehicleId, startDate, endDate)) {
            return Result.failure(ErrorCode.CONFLICT);
        }

        Reservation reservation = new Reservation(
                UUID.randomUUID().toString(),
                userId,
                vehicleId,
                startDate,
                endDate
        );
        reservations.save(reservation);
        return Result.success(reservation);
    }

    public Result<Reservation> pickUp(String reservationId) {
        return transition(reservationId, RentalStatus.ACTIVE);
    }

    public Result<Double> returnVehicle(String reservationId) {
        Reservation reservation = reservations.findById(reservationId).orElse(null);
        if (reservation == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        if (!reservation.getStatus().canTransitionTo(RentalStatus.COMPLETED)) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }

        Vehicle vehicle = vehicles.findById(reservation.getVehicleId()).orElseThrow();
        double cost = vehicle.getDailyRate() * reservation.rentalDays();
        reservation.setStatus(RentalStatus.COMPLETED);
        return Result.success(cost);
    }

    public Result<Reservation> cancelReservation(String reservationId) {
        return transition(reservationId, RentalStatus.CANCELLED);
    }

    public Optional<Reservation> getReservation(String reservationId) {
        return reservations.findById(reservationId);
    }

    private Result<Reservation> transition(String reservationId, RentalStatus next) {
        Reservation reservation = reservations.findById(reservationId).orElse(null);
        if (reservation == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        if (!reservation.getStatus().canTransitionTo(next)) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }
        reservation.setStatus(next);
        return Result.success(reservation);
    }

    private boolean isVehicleAvailable(String vehicleId, LocalDate startDate, LocalDate endDate) {
        for (Reservation reservation : reservations.findAll()) {
            if (!reservation.getVehicleId().equals(vehicleId)) {
                continue;
            }
            if (reservation.getStatus() == RentalStatus.CANCELLED
                    || reservation.getStatus() == RentalStatus.COMPLETED) {
                continue;
            }
            if (reservation.overlaps(startDate, endDate)) {
                return false;
            }
        }
        return true;
    }
}
