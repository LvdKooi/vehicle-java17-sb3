package nl.kooi.vehicle.infra;

import nl.kooi.vehicle.enums.VehicleType;
import nl.kooi.vehicle.infra.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {

    List<VehicleEntity> findVehicleEntityByVehicleType(VehicleType vehicleType);

    boolean existsVehicleEntityByLicensePlateLicenseNumber(String licenseNumber);
}
