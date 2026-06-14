package com.lld.problems.ridehailing;

import com.lld.problems.ridehailing.model.Driver;
import com.lld.problems.ridehailing.model.Location;
import com.lld.problems.ridehailing.model.Rider;
import com.lld.problems.ridehailing.model.Trip;
import com.lld.problems.ridehailing.model.TripStatus;
import com.lld.problems.ridehailing.service.RideService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RideHailingTest {

    private RideService rideService;
    private Driver driver;
    private Trip trip;

    @BeforeEach
    void setUp() {
        rideService = new RideService();
        rideService.registerRider(new Rider("R1", "Alice", new Location(1.0, 1.0)));
        driver = new Driver("D1", "Bob", new Location(1.1, 1.1));
        rideService.registerDriver(driver);
        trip = rideService.requestRide("R1", new Location(1.0, 1.0), new Location(2.0, 2.0))
                .getValue().orElseThrow();
    }

    @Test
    void fullTripLifecycle() {
        rideService.acceptRide(trip.getId(), "D1").getValue().orElseThrow();
        rideService.startTrip(trip.getId()).getValue().orElseThrow();
        Trip completed = rideService.completeTrip(trip.getId()).getValue().orElseThrow();
        assertEquals(TripStatus.COMPLETED, completed.getStatus());
        assertTrue(driver.isAvailable());
    }

    @Test
    void rejectAcceptWhenDriverBusy() {
        rideService.acceptRide(trip.getId(), "D1").getValue().orElseThrow();
        Trip secondTrip = rideService.requestRide("R1", new Location(1.0, 1.0), new Location(3.0, 3.0))
                .getValue().orElseThrow();
        assertTrue(rideService.acceptRide(secondTrip.getId(), "D1").getError().isPresent());
    }
}
