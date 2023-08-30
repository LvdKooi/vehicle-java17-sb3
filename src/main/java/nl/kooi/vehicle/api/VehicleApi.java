package nl.kooi.vehicle.api;

import lombok.RequiredArgsConstructor;
import nl.kooi.vehicle.api.dto.VehicleDTO;
import nl.kooi.vehicle.domain.service.VehicleService;
import nl.kooi.vehicle.enums.VehicleType;
import nl.kooi.vehicle.mapper.VehicleMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicles")
public class VehicleApi {

    private final VehicleService vehicleService;
    private final VehicleMapper mapper;

    @GetMapping
    public List<VehicleDTO> getAllVehicles(@RequestParam(required = false) VehicleType vehicleType) {
        return Optional.ofNullable(vehicleType)
                .map(vehicleService::getAllVehiclesByType)
                .orElseGet(vehicleService::getAllVehicles)
                .stream()
                .map(mapper::map)
                .toList();
    }

    @GetMapping("/{id}")
    public VehicleDTO getVehicleById(@PathVariable Long id) {
        return mapper.map(vehicleService.getVehicleById(id));
    }

    @PostMapping
    public VehicleDTO saveVehicle(@RequestBody VehicleDTO dto) {
        var savedVehicle = vehicleService.saveVehicle(mapper.map(dto));

        return mapper.map(savedVehicle);
    }

    @PutMapping("/{id}")
    public VehicleDTO updateVehicle(@PathVariable Long id,
                                    @RequestBody VehicleDTO dto) {
        var updatedVehicle = vehicleService.updateVehicle(mapper.map(dto, id));

        return mapper.map(updatedVehicle);
    }

    @DeleteMapping("/{id}")
    public void deleteVehicleById(@PathVariable Long id) {
        vehicleService.deleteVehicleById(id);
    }
}