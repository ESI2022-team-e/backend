package esi.backend.controller;

import esi.backend.model.Car;
import esi.backend.service.CarService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

//@CrossOrigin(origins = "http://localhost:8081")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/cars")
    public ResponseEntity<List<Car>> getAllCars(@RequestParam(value = "startTime", required = false) LocalDateTime startTime, @RequestParam(value = "endTime", required = false) LocalDateTime endTime) {
        return carService.getAllCars(startTime, endTime);
    }

    @GetMapping("/cars/{carId}")
    public ResponseEntity<Car> getCar(@PathVariable UUID carId) {
        return carService.getCar(carId);
    }

    @PostMapping("/cars")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> addCar(@RequestBody Car car) {
        return carService.addCar(car);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/cars/{carId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> updateCar(@RequestBody Car car, @PathVariable UUID carId) {
        return carService.updateCar(car, carId);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/cars/{carId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> deleteCar(@PathVariable UUID carId) {
        return carService.deleteCar(carId);
    }
}
