package com.lld.problems.parkinglot.demo;

import com.lld.problems.parkinglot.model.Floor;
import com.lld.problems.parkinglot.model.ParkingSpot;
import com.lld.problems.parkinglot.model.Ticket;
import com.lld.problems.parkinglot.model.Vehicle;
import com.lld.problems.parkinglot.model.VehicleType;
import com.lld.problems.parkinglot.service.ParkingLotService;
import com.lld.problems.parkinglot.strategy.HourlyFeeStrategy;

public class ParkingLotDemo {

    public static void main(String[] args) {
        ParkingLotService lot = new ParkingLotService(new HourlyFeeStrategy());
        Floor floor = new Floor(1);
        floor.addSpot(new ParkingSpot("S1", VehicleType.CAR));
        floor.addSpot(new ParkingSpot("S2", VehicleType.MOTORCYCLE));
        lot.addFloor(floor);

        Vehicle car = new Vehicle("ABC-123", VehicleType.CAR);
        Ticket ticket = lot.parkVehicle(car).getValue().orElseThrow();
        System.out.println("Parked: " + ticket.getTicketId());
        double fee = lot.unparkVehicle(ticket).getValue().orElseThrow();
        System.out.println("Fee: $" + fee);
    }
}
