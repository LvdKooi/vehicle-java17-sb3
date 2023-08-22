package nl.kooi.vehicle.domain;

import nl.kooi.vehicle.exception.VehicleException;
import org.junit.jupiter.api.Test;

import static nl.kooi.vehicle.enums.VehicleType.CAR;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VehicleTest {


    @Test
    void happyFlow() {
        assertDoesNotThrow(() -> new Vehicle(1L, "VW", "Golf", CAR, new LicensePlate(1L, "12345")));
    }

    @Test
    void brandShouldNotBeNull() {
        var message = assertThrows(VehicleException.class,
                () -> new Vehicle(1L, null, "VW", CAR, new LicensePlate(1L, "123456"))).getMessage();

        assertThat(message).isEqualTo("Brand should contain characters");
    }

    @Test
    void licenseShouldNotBeNull() {
        var message = assertThrows(VehicleException.class,
                () -> new Vehicle(1L, "VW", "Golf", CAR, null)).getMessage();

        assertThat(message).isEqualTo("LicensePlate should not be null");
    }

}