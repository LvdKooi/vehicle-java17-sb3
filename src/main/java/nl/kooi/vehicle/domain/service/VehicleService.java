package nl.kooi.vehicle.domain.service;

import nl.kooi.vehicle.domain.Vehicle;

import java.util.List;

public interface VehicleService {

    List<Vehicle> getAllVehicles();

    Vehicle getVehicleById(Long id);

    Vehicle saveVehicle(Vehicle vehicle);

    Vehicle updateVehicle(Vehicle vehicle);

    void deleteVehicleById(Long id);

    Vehicle findByLicenseNumber(String licenseNumber);
}
