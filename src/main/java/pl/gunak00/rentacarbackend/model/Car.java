package pl.gunak00.rentacarbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.gunak00.rentacarbackend.enums.CarDriveType;
import pl.gunak00.rentacarbackend.enums.CarFuelType;
import pl.gunak00.rentacarbackend.enums.CarGearboxType;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Car implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String name;
    private String model;
    private String category;
    private Long priceForDay;
    private Long manufactureYear;
    private Integer numberOfPeople;
    private Boolean airCon;
    @Enumerated(EnumType.STRING)
    private CarFuelType fuel;
    @Enumerated(EnumType.STRING)
    private CarGearboxType gearbox;
    private Integer numberOfDoor;
    private Integer engineSize;
    @Enumerated(EnumType.STRING)
    private CarDriveType driveType;
    private String imageUrl;
    private Boolean isAvailable;
}
