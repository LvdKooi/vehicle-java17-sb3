package nl.kooi.vehicle.infra;

import nl.kooi.vehicle.enums.VehicleType;
import nl.kooi.vehicle.infra.entity.LicensePlateEntity;
import nl.kooi.vehicle.infra.entity.VehicleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    @BeforeEach
    void setUp() {

        createVehicleEntityWithLicenseNumber("12");
        createVehicleEntityWithLicenseNumber("34");

        assertThat(vehicleRepository.findAll()).hasSize(2);
    }


    @ParameterizedTest
    @ValueSource(strings = {"12", "34"})
    void findByLicenseNumber(String licenseNumber) {

        var vehicleOptional = vehicleRepository.findVehicleEntityByLicensePlateLicenseNumber(licenseNumber);

        assertThat(vehicleOptional).isPresent();

        assertThat(vehicleOptional.map(VehicleEntity::getLicensePlate)
                .map(LicensePlateEntity::getLicenseNumber)
                .filter(licenseNumber::equals))
                .isPresent();
    }

    @ParameterizedTest
    @ValueSource(strings = {"12", "34"})
    void existsByLicenseNumber(String licenseNumber) {
        assertThat(vehicleRepository.existsVehicleEntityByLicensePlateLicenseNumber(licenseNumber)).isTrue();
    }


    @ParameterizedTest
    @ValueSource(strings = {"23", "45"})
    void findByLicenseNumber_notFound(String licenseNumber) {

        var vehicleOptional = vehicleRepository.findVehicleEntityByLicensePlateLicenseNumber(licenseNumber);

        assertThat(vehicleOptional).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"23", "45"})
    void existsByLicenseNumber_notFound(String licenseNumber) {
        assertThat(vehicleRepository.existsVehicleEntityByLicensePlateLicenseNumber(licenseNumber)).isFalse();
    }

    private void createVehicleEntityWithLicenseNumber(String licenseNumber) {
        var vehicle = new VehicleEntity();
        vehicle.setVehicleType(VehicleType.CAR);
        vehicle.setBrand("Volkswagen");
        vehicle.setModel("Golf");
        vehicle.setLicensePlate(new LicensePlateEntity());

        vehicle.getLicensePlate().setLicenseNumber(licenseNumber);

        vehicleRepository.save(vehicle);
    }
}