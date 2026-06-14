package com.lld.problems.ridehailing.service;

import com.lld.common.ErrorCode;
import com.lld.common.Result;
import com.lld.problems.ridehailing.model.Driver;
import com.lld.problems.ridehailing.model.Location;
import com.lld.problems.ridehailing.model.Rider;
import com.lld.problems.ridehailing.model.Trip;
import com.lld.problems.ridehailing.model.TripStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RideService {

    private final Map<String, Rider> riders = new HashMap<>();
    private final Map<String, Driver> drivers = new HashMap<>();
    private final Map<String, Trip> trips = new HashMap<>();

    public void registerRider(Rider rider) {
        riders.put(rider.getId(), rider);
    }

    public void registerDriver(Driver driver) {
        drivers.put(driver.getId(), driver);
    }

    public Result<Trip> requestRide(String riderId, Location pickup, Location dropoff) {
        Rider rider = riders.get(riderId);
        if (rider == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        Trip trip = new Trip(UUID.randomUUID().toString(), rider, pickup, dropoff);
        trips.put(trip.getId(), trip);
        return Result.success(trip);
    }

    public Result<Trip> acceptRide(String tripId, String driverId) {
        Trip trip = trips.get(tripId);
        Driver driver = drivers.get(driverId);
        if (trip == null || driver == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        if (trip.getStatus() != TripStatus.REQUESTED) {
            return Result.failure(ErrorCode.CONFLICT);
        }
        if (!driver.isAvailable()) {
            return Result.failure(ErrorCode.CONFLICT);
        }
        driver.setAvailable(false);
        trip.assignDriver(driver);
        return Result.success(trip);
    }

    public Result<Trip> startTrip(String tripId) {
        Trip trip = trips.get(tripId);
        if (trip == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        if (trip.getStatus() != TripStatus.ACCEPTED) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }
        trip.start();
        return Result.success(trip);
    }

    public Result<Trip> completeTrip(String tripId) {
        Trip trip = trips.get(tripId);
        if (trip == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        if (trip.getStatus() != TripStatus.IN_PROGRESS) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }
        trip.complete();
        if (trip.getDriver() != null) {
            trip.getDriver().setAvailable(true);
        }
        return Result.success(trip);
    }

    public Result<Trip> getTrip(String tripId) {
        Trip trip = trips.get(tripId);
        if (trip == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        return Result.success(trip);
    }
}
