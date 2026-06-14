package com.lld.problems.carrental;

import com.lld.common.ErrorCode;
import com.lld.problems.carrental.model.Reservation;
import com.lld.problems.carrental.model.RentalStatus;
import com.lld.problems.carrental.model.Vehicle;
import com.lld.problems.carrental.model.VehicleType;
import com.lld.problems.carrental.service.CarRentalService;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CarRentalTest {

    private CarRentalService service;
    private LocalDate start;
    private LocalDate end;

    @BeforeEach
    void setUp() {
        service = new CarRentalService();
        service.addVehicle(new Vehicle("V1", "Honda", "Civic", VehicleType.ECONOMY, 40.0));
        service.addVehicle(new Vehicle("V2", "BMW", "X5", VehicleType.LUXURY, 120.0));
        start = LocalDate.of(2026, 6, 10);
        end = LocalDate.of(2026, 6, 12);
    }

    @Test
    void reservePickUpAndReturn() {
        Reservation reservation = service.reserve("U1", "V1", start, end).getValue().orElseThrow();
        service.pickUp(reservation.getReservationId());
        double cost = service.returnVehicle(reservation.getReservationId()).getValue().orElseThrow();

        assertEquals(120.0, cost, 0.001);
        assertEquals(
                RentalStatus.COMPLETED,
                service.getReservation(reservation.getReservationId()).orElseThrow().getStatus()
        );
    }

    @Test
    void searchAvailableExcludesBookedVehicle() {
        service.reserve("U1", "V1", start, end);
        assertEquals(1, service.searchAvailable(start, end).size());
        assertTrue(service.searchAvailable(start, end).stream()
                .noneMatch(v -> v.getVehicleId().equals("V1")));
    }

    @Test
    void rejectOverlappingReservation() {
        service.reserve("U1", "V1", start, end);
        assertEquals(
                ErrorCode.CONFLICT,
                service.reserve("U2", "V1", start.plusDays(1), end.plusDays(1)).getError().orElseThrow()
        );
    }

    @Test
    void cancelReservedBooking() {
        Reservation reservation = service.reserve("U1", "V2", start, end).getValue().orElseThrow();
        Reservation cancelled = service.cancelReservation(reservation.getReservationId())
                .getValue().orElseThrow();
        assertEquals(RentalStatus.CANCELLED, cancelled.getStatus());
        assertEquals(2, service.searchAvailable(start, end).size());
    }

    @Test
    void rejectInvalidDateRange() {
        assertEquals(
                ErrorCode.INVALID_INPUT,
                service.reserve("U1", "V1", end, start).getError().orElseThrow()
        );
    }
}
