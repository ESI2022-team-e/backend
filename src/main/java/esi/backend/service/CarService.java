package esi.backend.service;

import esi.backend.model.Car;
import esi.backend.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public ResponseEntity<List<Car>> getAllCars(LocalDateTime startTime, LocalDateTime endTime) {
        List<Car> cars = new ArrayList<>();
        carRepository.findAll().forEach(cars::add);
        if (startTime != null && endTime != null) {

        }
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    public ResponseEntity<Car> getCar(UUID id) {
        Car car = carRepository.findById(id).orElse(null);
        return (car == null)
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(car, HttpStatus.OK);
    }

    public void addCar(Car car) {
        carRepository.save(car);
    }

    public void updateCar(Car car, UUID carId) {
        Optional<Car> carInService = carRepository.findById(carId);

        if (carInService.isPresent())
            if (car.getDaily_cost() >= 0) {
                carInService.get().setDaily_cost(car.getDaily_cost());
            }
        if (car.getLicence_plate() != null) {
            carInService.get().setLicence_plate(car.getLicence_plate());
        }
        carRepository.save(carInService.get());
    }

    public void deleteCar(UUID id) {
        carRepository.deleteById(id);
    }
}

