package pl.gunak00.rentacarbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gunak00.rentacarbackend.model.Car;

public interface CarRepository extends JpaRepository<Car, Long> {
}
