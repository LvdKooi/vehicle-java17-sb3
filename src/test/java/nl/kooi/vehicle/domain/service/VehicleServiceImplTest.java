package nl.kooi.vehicle.domain.service;

import nl.kooi.vehicle.domain.LicensePlate;
import nl.kooi.vehicle.domain.Vehicle;
import nl.kooi.vehicle.exception.NotFoundException;
import nl.kooi.vehicle.exception.VehicleException;
import nl.kooi.vehicle.infra.VehicleRepository;
import nl.kooi.vehicle.infra.entity.LicensePlateEntity;
import nl.kooi.vehicle.infra.entity.VehicleEntity;
import nl.kooi.vehicle.mapper.VehicleMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static nl.kooi.vehicle.enums.VehicleType.CAR;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(VehicleServiceImpl.class)
@Import(VehicleMapperImpl.class)
class VehicleServiceImplTest {

    @Autowired
    private VehicleService service;

    @MockBean
    private VehicleRepository repository;


    @Test
    void getById_found() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(getVehicleEntityWithId(1L)));

        var result = assertDoesNotThrow(() -> service.getVehicleById(1L));

        assertVehicle(result);
    }

    @Test
    void getById_notFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        var message = assertThrows(NotFoundException.class, () -> service.getVehicleById(1L)).getMessage();

        assertThat(message).isEqualTo("Vehicle with id 1 not found");
    }

    @Test
    void findByLicenseNumber_found() {
        when(repository.findVehicleEntityByLicensePlateLicenseNumber(anyString())).thenReturn(Optional.of(getVehicleEntityWithId(1L)));

        var result = assertDoesNotThrow(() -> service.findByLicenseNumber("12"));

        assertVehicle(result);
    }

    @Test
    void findByLicenseNumber_notFound() {
        when(repository.findVehicleEntityByLicensePlateLicenseNumber(anyString())).thenReturn(Optional.empty());

        var message = assertThrows(NotFoundException.class, () -> service.findByLicenseNumber("1")).getMessage();

        assertThat(message).isEqualTo("Vehicle with licensenumber 1 not found");
    }


    @Test
    void saveVehicle() {
        when(repository.existsVehicleEntityByLicensePlateLicenseNumber(anyString())).thenReturn(false);
        when(repository.save(any(VehicleEntity.class))).thenReturn(getVehicleEntityWithId(1L));

        assertVehicle(service.saveVehicle(createVehicleWithId(1L)));
    }

    @Test
    void saveVehicle_vehicleWithLicenseNumberAlreadyExists() {
        when(repository.existsVehicleEntityByLicensePlateLicenseNumber(anyString())).thenReturn(true);
        when(repository.save(any(VehicleEntity.class))).thenReturn(getVehicleEntityWithId(1L));

        var message = assertThrows(VehicleException.class, () -> service.saveVehicle(createVehicleWithId(1L))).getMessage();

        assertThat(message).isEqualTo("Vehicle with licenseNumber 12345 already exists");
    }

    @Test
    void updateVehicle_existingVehicle() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(getVehicleEntityWithId(1L)));
        when(repository.save(any(VehicleEntity.class))).thenReturn(getVehicleEntityWithId(1L));

        assertVehicle(service.updateVehicle(createVehicleWithId(1L)));
    }

    @Test
    void updateVehicle_nonExistingVehicle() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        when(repository.save(any(VehicleEntity.class))).thenReturn(getVehicleEntityWithId(1L));

        var message = assertThrows(NotFoundException.class, () -> service.getVehicleById(1L)).getMessage();

        assertThat(message).isEqualTo("Vehicle with id 1 not found");
    }

    @Test
    void deleteVehicle_existingVehicle() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(getVehicleEntityWithId(1L)));
        doNothing().when(repository).deleteById(anyLong());

        service.deleteVehicleById(1L);
    }

    @Test
    void deleteVehicle_nonExistingVehicle() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        doNothing().when(repository).deleteById(anyLong());

        var message = assertThrows(NotFoundException.class, () -> service.deleteVehicleById(1L)).getMessage();

        assertThat(message).isEqualTo("Vehicle with id 1 not found");
    }

    private static void assertVehicle(Vehicle vehicle) {
        assertThat(vehicle).isNotNull();
        assertThat(vehicle.id()).isEqualTo((Long) 1L);
        assertThat(vehicle.vehicleType()).isEqualTo(CAR);
        assertThat(vehicle.brand()).isEqualTo("Volkswagen");
        assertThat(vehicle.model()).isEqualTo("Golf");
        assertThat(vehicle.licensePlate()).isNotNull();
        assertThat(vehicle.licensePlate().id()).isEqualTo(1L);
        assertThat(vehicle.licensePlate().licenseNumber()).isEqualTo("12345");
    }

    private static Vehicle createVehicleWithId(Long id) {
        return new Vehicle(id, "Volkswagen", "Golf", CAR, new LicensePlate(1L, "12345"));
    }

    private static VehicleEntity getVehicleEntityWithId(Long id) {
        var vehicleEntity = new VehicleEntity();

        vehicleEntity.setVehicleType(CAR);
        vehicleEntity.setId(id);
        vehicleEntity.setBrand("Volkswagen");
        vehicleEntity.setModel("Golf");

        var licensePlate = new LicensePlateEntity();
        licensePlate.setId(1L);
        licensePlate.setLicenseNumber("12345");

        vehicleEntity.setLicensePlate(licensePlate);

        return vehicleEntity;
    }
}