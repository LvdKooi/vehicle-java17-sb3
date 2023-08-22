package nl.kooi.vehicle.domain;

import nl.kooi.vehicle.enums.VehicleType;
import nl.kooi.vehicle.exception.VehicleException;

public record Vehicle(Long id,
                      String brand,
                      String model,
                      VehicleType vehicleType,
                      LicensePlate licensePlate
) {

    public Vehicle {
        if (brand == null || brand.isEmpty()) {
            throw new VehicleException("Brand should contain characters");
        }

        if (model == null || model.isEmpty()) {
            throw new VehicleException("Model should contain characters");
        }

        if (vehicleType == null) {
            throw new VehicleException("VehicleType should not be null");
        }

        if (licensePlate == null) {
            throw new VehicleException("LicensePlate should not be null");
        }
    }

}
