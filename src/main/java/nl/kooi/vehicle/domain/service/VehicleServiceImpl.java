package nl.kooi.vehicle.domain.service;


import lombok.RequiredArgsConstructor;
import nl.kooi.vehicle.domain.Vehicle;
import nl.kooi.vehicle.exception.NotFoundException;
import nl.kooi.vehicle.exception.VehicleException;
import nl.kooi.vehicle.infra.VehicleRepository;
import nl.kooi.vehicle.mapper.VehicleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository repository;
    private final VehicleMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> getAllVehicles() {
        return repository.findAll().stream().map(mapper::map).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Vehicle getVehicleById(Long id) {
        return repository
                .findById(id)
                .map(mapper::map)
                .orElseThrow(() -> new NotFoundException(String.format("Vehicle with id %d not found", id)));
    }

    @Override
    public Vehicle saveVehicle(Vehicle vehicle) {
        if (!repository.existsVehicleEntityByLicensePlateLicenseNumber(vehicle.licensePlate().licenseNumber())) {
            var entity = mapper.mapToEntity(vehicle);
            return mapper.map(repository.save(entity));
        } else {
            throw new VehicleException(String.format("Vehicle with licenseNumber %s already exists", vehicle.licensePlate().licenseNumber()));
        }
    }

    @Override
    public Vehicle updateVehicle(Vehicle vehicle) {
        var currentVehicle = getVehicleById(vehicle.id());
        var vehicleEntity = mapper.mapToEntity(new Vehicle(currentVehicle.id(), vehicle.brand(), vehicle.model(), vehicle.vehicleType(), vehicle.licensePlate()));

        return mapper.map(repository.save(vehicleEntity));
    }

    @Override
    public void deleteVehicleById(Long id) {
        getVehicleById(id);
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Vehicle findByLicenseNumber(String licenseNumber) {
        return repository.findVehicleEntityByLicensePlateLicenseNumber(licenseNumber)
                .map(mapper::map)
                .orElseThrow(() -> new NotFoundException(String.format("Vehicle with licensenumber %s not found", licenseNumber)));
    }
}
