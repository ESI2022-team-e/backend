package esi.backendnew.controller;
import esi.backendnew.model.Car;
import esi.backendnew.service.CarService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/cars")
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @RequestMapping("/cars/{id}")
    public Optional<Car> getCar(@PathVariable UUID id) {
        return carService.getCar(id);
    }

    @PostMapping("/cars")
    public void addCar(@RequestBody Car car) {
        carService.addCar(car);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/cars/{id}")
    public void updateCar(@RequestBody Car car, @PathVariable UUID id) {
        carService.updateCar(id, car);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/cars/{id}")
    public void deleteCar(@PathVariable UUID id) {
        carService.deleteCar(id);
    }
}
