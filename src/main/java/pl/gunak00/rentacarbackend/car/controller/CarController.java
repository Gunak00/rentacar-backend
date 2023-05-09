package pl.gunak00.rentacarbackend.car.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.gunak00.rentacarbackend.car.model.Car;
import pl.gunak00.rentacarbackend.car.service.CarService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/car")
//@CrossOrigin(origins="http://localhost:4200", allowedHeaders="*", methods={RequestMethod.POST, RequestMethod.DELETE, RequestMethod.GET, RequestMethod.OPTIONS})
public class CarController {
    public final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Car>> getAllCars(){
        List<Car> cars = carService.findAllCars();
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Car> getCar(@PathVariable("id") Long id){
        Car car = carService.findCarById(id);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Car> addCar(@RequestBody Car car){
        Car newCar = carService.addCar(car);
        return new ResponseEntity<>(newCar, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Car> updateCar(@RequestBody Car car){
        Car updateCar = carService.updateCar(car);
        return new ResponseEntity<>(updateCar, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable("id") Long id){
        carService.deleteCar(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadImage(@RequestParam("carId") Long id,
                                         @RequestParam("file") MultipartFile file) throws IOException {
        File dir = new File("src/main/resources/carImages/" + id);
        dir.mkdirs();

        File[] files = dir.listFiles();

        if ((files != null ? files.length : 0) != 0){
            for (File f: files)
                f.delete();
        }

        String fileName = file.getOriginalFilename();

        Car car = carService.findCarById(id);
        car.setImageUrl(fileName);
        carService.updateCar(car);

        File uploadedFile = new File(dir.getAbsolutePath() + "/" + fileName);

        file.transferTo(uploadedFile);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/image/{carId}/{imageName}")
    public ResponseEntity<byte[]> getImage(@PathVariable("carId") Long carId,
                                           @PathVariable("imageName") String imageName) throws IOException {
        String imagePath = "src/main/resources/carImages/" + carId + "/" + imageName;
        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(imageBytes.length);

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
}
