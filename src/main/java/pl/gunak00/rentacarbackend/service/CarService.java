package pl.gunak00.rentacarbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.gunak00.rentacarbackend.exceptions.CarNotFoundException;
import pl.gunak00.rentacarbackend.model.Car;
import pl.gunak00.rentacarbackend.repository.CarRepository;

import java.util.List;

@Service
public class CarService {
    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car addCar(Car car){
        return carRepository.save(car);
    }

    public List<Car> findAllCars(){
        return carRepository.findAll();
    }

    public Car updateCar(Car car){
        return carRepository.save(car);
    }

    public Car findCarById(Long id){
        return carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("Car by id " + id + " was not found"));
    }

    public void deleteCar(Long id){
        carRepository.deleteById(id);
    }
}
