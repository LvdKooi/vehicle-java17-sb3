package nl.kooi.vehicle.api.dto;

import nl.kooi.vehicle.enums.VehicleType;

public record VehicleDTO(Long id, String brand, String model, VehicleType vehicleType, String licenseNumber) {
}
