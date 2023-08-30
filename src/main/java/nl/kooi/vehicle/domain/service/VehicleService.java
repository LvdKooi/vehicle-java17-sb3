package nl.kooi.vehicle.domain.service;

import nl.kooi.vehicle.domain.Vehicle;
import nl.kooi.vehicle.enums.VehicleType;

import java.util.List;

public interface VehicleService {

    List<Vehicle> getAllVehicles();

    List<Vehicle> getAllVehiclesByType(VehicleType vehicleType);

    Vehicle getVehicleById(Long id);

    Vehicle saveVehicle(Vehicle vehicle);

    Vehicle updateVehicle(Vehicle vehicle);

    void deleteVehicleById(Long id);
}
