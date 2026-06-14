package com.lld.problems.ridehailing.demo;

import com.lld.problems.ridehailing.model.Driver;
import com.lld.problems.ridehailing.model.Location;
import com.lld.problems.ridehailing.model.Rider;
import com.lld.problems.ridehailing.model.Trip;
import com.lld.problems.ridehailing.service.RideService;

public class RideHailingDemo {

    public static void main(String[] args) {
        RideService rideService = new RideService();
        rideService.registerRider(new Rider("R1", "Alice", new Location(12.97, 77.59)));
        rideService.registerDriver(new Driver("D1", "Bob", new Location(12.98, 77.60)));

        Trip trip = rideService.requestRide("R1", new Location(12.97, 77.59), new Location(13.0, 77.7))
                .getValue().orElseThrow();
        rideService.acceptRide(trip.getId(), "D1").getValue().orElseThrow();
        rideService.startTrip(trip.getId()).getValue().orElseThrow();
        Trip completed = rideService.completeTrip(trip.getId()).getValue().orElseThrow();
        System.out.println("Trip status: " + completed.getStatus());
    }
}
