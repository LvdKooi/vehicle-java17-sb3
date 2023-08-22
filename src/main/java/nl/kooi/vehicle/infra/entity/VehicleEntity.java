package nl.kooi.vehicle.infra.entity;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import nl.kooi.vehicle.enums.VehicleType;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VehicleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String brand;
    private String model;

    @Enumerated(value = EnumType.STRING)
    private VehicleType vehicleType;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private LicensePlateEntity licensePlate;
}
