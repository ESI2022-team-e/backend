package esi.backend.controller;

import esi.backend.model.Car;
import esi.backend.service.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<List<Car>> getAllCars(@RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime) {
        return carService.getAllCars(startTime, endTime);
    }

    @GetMapping("/cars/{carId}")
    public ResponseEntity<Car> getCar(@PathVariable UUID carId) {
        return carService.getCar(carId);
    }

    @PostMapping("/cars")
    @PreAuthorize("hasRole('MANAGER')")
    public void addCar(@RequestBody Car car) {
        carService.addCar(car);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/cars/{carId}")
    @PreAuthorize("hasRole('MANAGER')")
    public void updateCar(@RequestBody Car car, @PathVariable UUID carId) {
        carService.updateCar(car, carId);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/cars/{carId}")
    @PreAuthorize("hasRole('MANAGER')")
    public void deleteCar(@PathVariable UUID carId) {
        carService.deleteCar(carId);
    }
}
