CREATE TABLE license_plate_entity (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      license_number VARCHAR(255) NOT NULL
);

CREATE TABLE vehicle_entity (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                brand VARCHAR(255) NOT NULL,
                                model VARCHAR(255) NOT NULL,
                                vehicle_type VARCHAR(50) NOT NULL,
                                license_plate_id BIGINT,
                                FOREIGN KEY (license_plate_id) REFERENCES license_plate_entity(id)
);