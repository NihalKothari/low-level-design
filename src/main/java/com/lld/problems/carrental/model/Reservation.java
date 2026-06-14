package com.lld.problems.carrental.model;

import java.time.LocalDate;

public class Reservation {

    private final String reservationId;
    private final String userId;
    private final String vehicleId;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private RentalStatus status;

    public Reservation(
            String reservationId,
            String userId,
            String vehicleId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = RentalStatus.RESERVED;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getUserId() {
        return userId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public RentalStatus getStatus() {
        return status;
    }

    public void setStatus(RentalStatus status) {
        this.status = status;
    }

    public long rentalDays() {
        return endDate.toEpochDay() - startDate.toEpochDay() + 1;
    }

    public boolean overlaps(LocalDate otherStart, LocalDate otherEnd) {
        return !otherEnd.isBefore(startDate) && !otherStart.isAfter(endDate);
    }
}
