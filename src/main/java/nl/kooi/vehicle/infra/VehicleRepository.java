package nl.kooi.vehicle.infra;

import nl.kooi.vehicle.infra.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {

    Optional<VehicleEntity> findVehicleEntityByLicensePlateLicenseNumber(String licenseNumber);

    boolean existsVehicleEntityByLicensePlateLicenseNumber(String licenseNumber);
}
