package com.lld.problems.parkinglot;

import com.lld.problems.parkinglot.model.Floor;
import com.lld.problems.parkinglot.model.ParkingSpot;
import com.lld.problems.parkinglot.model.Ticket;
import com.lld.problems.parkinglot.model.Vehicle;
import com.lld.problems.parkinglot.model.VehicleType;
import com.lld.problems.parkinglot.service.ParkingLotService;
import com.lld.problems.parkinglot.strategy.HourlyFeeStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParkingLotTest {

    private ParkingLotService lot;

    @BeforeEach
    void setUp() {
        lot = new ParkingLotService(new HourlyFeeStrategy());
        Floor floor = new Floor(1);
        floor.addSpot(new ParkingSpot("C1", VehicleType.CAR));
        floor.addSpot(new ParkingSpot("M1", VehicleType.MOTORCYCLE));
        lot.addFloor(floor);
    }

    @Test
    void parkAndUnparkCar() {
        Vehicle car = new Vehicle("XYZ", VehicleType.CAR);
        Ticket ticket = lot.parkVehicle(car).getValue().orElseThrow();
        assertEquals(0, lot.getAvailableSpots(VehicleType.CAR));
        double fee = lot.unparkVehicle(ticket).getValue().orElseThrow();
        assertTrue(fee >= 20.0);
        assertEquals(1, lot.getAvailableSpots(VehicleType.CAR));
    }

    @Test
    void rejectWhenFull() {
        lot.parkVehicle(new Vehicle("A", VehicleType.CAR));
        assertTrue(lot.parkVehicle(new Vehicle("B", VehicleType.CAR)).getError().isPresent());
    }
}
