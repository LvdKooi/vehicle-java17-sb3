package nl.kooi.vehicle.infra;

import nl.kooi.vehicle.enums.VehicleType;
import nl.kooi.vehicle.infra.entity.LicensePlateEntity;
import nl.kooi.vehicle.infra.entity.VehicleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static nl.kooi.vehicle.enums.VehicleType.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    @BeforeEach
    void setUp() {

        createVehicleEntityWithLicenseNumberAndType("12", CAR);
        createVehicleEntityWithLicenseNumberAndType("34", TRUCK);

        assertThat(vehicleRepository.findAll()).hasSize(2);
    }


    @ParameterizedTest
    @EnumSource(value = VehicleType.class, names = {"CAR", "TRUCK"})
    void findByType(VehicleType type) {

        var list = vehicleRepository.findVehicleEntityByVehicleType(type);

        assertThat(list).hasSize(1);

        assertThat(list.get(0).getVehicleType()).isEqualTo(type);
    }

    @Test
    void findByType_nothingReturned() {
        var list = vehicleRepository.findVehicleEntityByVehicleType(BUS);

        assertThat(list).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"12", "34"})
    void existsByLicenseNumber(String licenseNumber) {
        assertThat(vehicleRepository.existsVehicleEntityByLicensePlateLicenseNumber(licenseNumber)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"23", "45"})
    void existsByLicenseNumber_notFound(String licenseNumber) {
        assertThat(vehicleRepository.existsVehicleEntityByLicensePlateLicenseNumber(licenseNumber)).isFalse();
    }

    private void createVehicleEntityWithLicenseNumberAndType(String licenseNumber, VehicleType type) {
        var vehicle = new VehicleEntity();
        vehicle.setVehicleType(type);
        vehicle.setBrand("Volkswagen");
        vehicle.setModel("Golf");
        vehicle.setLicensePlate(new LicensePlateEntity());

        vehicle.getLicensePlate().setLicenseNumber(licenseNumber);

        vehicleRepository.save(vehicle);
    }
}