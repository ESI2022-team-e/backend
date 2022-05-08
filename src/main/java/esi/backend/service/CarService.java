package esi.backend.service;

import esi.backend.model.Car;
import esi.backend.model.Request;
import esi.backend.model.RequestStatus;
import esi.backend.repository.CarRepository;
import esi.backend.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private RequestRepository requestRepository;

    public ResponseEntity<List<Car>> getAllCars(LocalDateTime startTime, LocalDateTime endTime) {
        List<Car> cars = new ArrayList<>();
        carRepository.findAll().forEach(cars::add);

        if (startTime != null && endTime != null) { // TODO: refactor this logic
            List<Request> requests = new ArrayList<>();
            requestRepository.findAll().forEach(requests::add);
            for (Request r : requests) {
                if (r.getPickup_datetime().isBefore(endTime) && startTime.isBefore(r.getDropoff_datetime())) {
                    if (r.getStatus() == RequestStatus.ACCEPTED || r.getStatus() == RequestStatus.PENDING) {
                        cars.remove(r.getCar());
                    }
                }
            }
        }
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    public ResponseEntity<Car> getCar(UUID id) {
        Car car = carRepository.findById(id).orElse(null);
        return (car == null)
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(car, HttpStatus.OK);
    }

    public ResponseEntity<?> addCar(Car car) {
        carRepository.save(car);
        return ResponseEntity.ok("Car added successfully!");
    }

    public ResponseEntity<?> updateCar(Car newCar, UUID carId) {
        Car car = carRepository.findById(carId).orElse(null);
        if (car == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (newCar.getDaily_cost() >= 0) {
            car.setDaily_cost(newCar.getDaily_cost());
        }
        if (newCar.getLicence_plate() != null) {
            car.setLicence_plate(newCar.getLicence_plate());
        }
        carRepository.save(car);
        return ResponseEntity.ok("Car updated successfully!");
    }

    public ResponseEntity<?> deleteCar(UUID id) {
        carRepository.deleteById(id);
        return ResponseEntity.ok("Car deleted successfully!");
    }
}

