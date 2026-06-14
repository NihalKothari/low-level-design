package com.lld.problems.carrental.demo;

import com.lld.problems.carrental.model.RentalStatus;
import com.lld.problems.carrental.model.Vehicle;
import com.lld.problems.carrental.model.VehicleType;
import com.lld.problems.carrental.model.Reservation;
import com.lld.problems.carrental.service.CarRentalService;
import java.time.LocalDate;

public class CarRentalDemo {

    public static void main(String[] args) {
        CarRentalService service = new CarRentalService();
        service.addVehicle(new Vehicle("V1", "Toyota", "Corolla", VehicleType.COMPACT, 45.0));
        service.addVehicle(new Vehicle("V2", "Ford", "Explorer", VehicleType.SUV, 75.0));

        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = LocalDate.now().plusDays(3);

        System.out.println("Available: " + service.searchAvailable(start, end).size());

        Reservation reservation = service.reserve("U1", "V1", start, end).getValue().orElseThrow();
        service.pickUp(reservation.getReservationId());
        double cost = service.returnVehicle(reservation.getReservationId()).getValue().orElseThrow();

        System.out.println("Reservation " + reservation.getReservationId()
                + " completed. Cost: $" + cost);
        System.out.println("Status: " + service.getReservation(reservation.getReservationId())
                .map(r -> r.getStatus() == RentalStatus.COMPLETED)
                .orElse(false));
    }
}
