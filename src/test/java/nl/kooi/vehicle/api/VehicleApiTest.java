package nl.kooi.vehicle.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.kooi.vehicle.api.dto.VehicleDTO;
import nl.kooi.vehicle.domain.LicensePlate;
import nl.kooi.vehicle.domain.Vehicle;
import nl.kooi.vehicle.domain.service.VehicleService;
import nl.kooi.vehicle.domain.service.VehicleServiceImpl;
import nl.kooi.vehicle.exception.NotFoundException;
import nl.kooi.vehicle.exception.VehicleException;
import nl.kooi.vehicle.mapper.VehicleMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static nl.kooi.vehicle.enums.VehicleType.CAR;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({VehicleApi.class, ControllerAdvice.class})
@Import({VehicleServiceImpl.class, VehicleMapperImpl.class})
class VehicleApiTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleService vehicleService;


    @Test
    void getVehicleById() throws Exception {
        when(vehicleService.getVehicleById(anyLong())).thenReturn(createVehicleWithId(1L));

        var jsonString = mockMvc.perform(get("/vehicles/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var dto = objectMapper.readValue(jsonString, VehicleDTO.class);

        assertBaseVehicleWithId(dto, 1L);
    }

    @Test
    void getVehicleById_notFound() throws Exception {
        when(vehicleService.getVehicleById(anyLong())).thenThrow(new NotFoundException("Vehicle not found"));

        var jsonString = mockMvc.perform(get("/vehicles/1"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var problem = objectMapper.readValue(jsonString, ProblemDetail.class);

        assertThat(problem.getDetail()).isEqualTo("Vehicle not found");
        assertThat(problem.getTitle()).isEqualTo("NOT FOUND");
        assertThat(problem.getStatus()).isEqualTo(404);
    }

    @Test
    void findVehicleByLicenseNumber() throws Exception {
        when(vehicleService.findByLicenseNumber(anyString())).thenReturn(createVehicleWithId(1L));

        var jsonString = mockMvc.perform(get("/vehicles/search?license-number=12345"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var dto = objectMapper.readValue(jsonString, VehicleDTO.class);

        assertBaseVehicleWithId(dto, 1L);
    }

    @Test
    void getAllVehicles_vehiclesExist() throws Exception {
        when(vehicleService.getAllVehicles())
                .thenReturn(List.of(createVehicleWithId(1L), createVehicleWithId(2L)));


        var jsonString = mockMvc.perform(get("/vehicles"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


        var typeRef = new TypeReference<List<VehicleDTO>>() {
        };

        var list = objectMapper.readValue(jsonString, typeRef);

        assertThat(list).hasSize(2);

        assertBaseVehicleWithId(list.get(0), 1L);
        assertBaseVehicleWithId(list.get(1), 2L);
    }

    @Test
    void getAllVehicles_noVehiclesExist() throws Exception {
        when(vehicleService.getAllVehicles())
                .thenReturn(Collections.emptyList());


        var jsonString = mockMvc.perform(get("/vehicles"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


        var typeRef = new TypeReference<List<VehicleDTO>>() {
        };

        var list = objectMapper.readValue(jsonString, typeRef);

        assertThat(list).isEmpty();
    }

    @Test
    void deleteVehicleById() throws Exception {
        doNothing().when(vehicleService).deleteVehicleById(anyLong());

        mockMvc.perform(delete("/vehicles/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(vehicleService, times(1)).deleteVehicleById(1L);
    }

    @Test
    void saveVehicle() throws Exception {
        when(vehicleService.saveVehicle(any(Vehicle.class))).thenReturn(createVehicleWithId(1L));

        var dtoAsString = objectMapper.writeValueAsString(createVehicleDtoWithId(1L));

        var responseAsString = mockMvc.perform(post("/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoAsString))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var dto = objectMapper.readValue(responseAsString, VehicleDTO.class);

        assertBaseVehicleWithId(dto, 1L);
    }

    @Test
    void saveVehicle_licenseNumberAlreadyExists() throws Exception {
        when(vehicleService.saveVehicle(any(Vehicle.class))).thenThrow(new VehicleException("LicenseNumber already exists"));

        var dtoAsString = objectMapper.writeValueAsString(createVehicleDtoWithId(1L));

        var responseAsString = mockMvc.perform(post("/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoAsString))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var problem = objectMapper.readValue(responseAsString, ProblemDetail.class);

        assertThat(problem.getDetail()).isEqualTo("LicenseNumber already exists");
        assertThat(problem.getTitle()).isEqualTo("INVALID VEHICLE REQUEST");
        assertThat(problem.getStatus()).isEqualTo(400);
    }


    @Test
    void updateVehicle() throws Exception {
        when(vehicleService.updateVehicle(any(Vehicle.class))).thenReturn(createVehicleWithId(1L));

        var dtoAsString = objectMapper.writeValueAsString(createVehicleDtoWithId(1L));

        var responseAsString = mockMvc.perform(put("/vehicles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoAsString))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var dto = objectMapper.readValue(responseAsString, VehicleDTO.class);

        assertBaseVehicleWithId(dto, 1L);
    }

    private void assertBaseVehicleWithId(VehicleDTO dto, Long id) {
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(id);
        assertThat(dto.brand()).isEqualTo("Volkswagen");
        assertThat(dto.model()).isEqualTo("Golf");
        assertThat(dto.vehicleType()).isEqualTo(CAR);
        assertThat(dto.licenseNumber()).isEqualTo("12345");
    }

    private static Vehicle createVehicleWithId(Long id) {
        return new Vehicle(id, "Volkswagen", "Golf", CAR, new LicensePlate(1L, "12345"));
    }

    private static VehicleDTO createVehicleDtoWithId(Long id) {
        return new VehicleDTO(id, "Volkswagen", "Golf", CAR, "12345");
    }
}