package nl.kooi.vehicle.domain;

import nl.kooi.vehicle.exception.VehicleException;

public record LicensePlate(
        Long id,
        String licenseNumber) {

    public LicensePlate {
        if (licenseNumber == null || licenseNumber.isEmpty()) {
            throw new VehicleException("LicenseNumber should contain characters");
        }
    }
}
