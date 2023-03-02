package pl.gunak00.rentacarbackend.car.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gunak00.rentacarbackend.car.model.Car;

public interface CarRepository extends JpaRepository<Car, Long> {
}
