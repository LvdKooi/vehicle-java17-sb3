package nl.kooi.vehicle.mapper;


import nl.kooi.vehicle.api.dto.VehicleDTO;
import nl.kooi.vehicle.domain.LicensePlate;
import nl.kooi.vehicle.domain.Vehicle;
import nl.kooi.vehicle.infra.entity.LicensePlateEntity;
import nl.kooi.vehicle.infra.entity.VehicleEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@org.mapstruct.Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VehicleMapper {

    @Mapping(target = "licenseNumber", source = "vehicle.licensePlate.licenseNumber")
    VehicleDTO map(Vehicle vehicle);


    @Mapping(target = "licensePlate", expression = "java(mapLicense(dto.licenseNumber()))")
    Vehicle map(VehicleDTO dto);

    @Mapping(target = "licensePlate", expression = "java(mapLicense(dto.licenseNumber()))")
    @Mapping(target = "id", source = "currentId")
    Vehicle map(VehicleDTO dto, Long currentId);
    LicensePlate mapLicense(String licenseNumber);

    VehicleEntity mapToEntity(Vehicle vehicle);

    Vehicle map(VehicleEntity entity);

    LicensePlate map(LicensePlateEntity entity);

    LicensePlateEntity map(LicensePlate licensePlate);
}
