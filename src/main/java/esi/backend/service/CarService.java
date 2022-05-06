package esi.backend.service;

import esi.backend.model.Car;
import esi.backend.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        carRepository.findAll().forEach(cars::add);
        return cars;
    }

    public Optional<Car> getCar(UUID id) {
        return carRepository.findById(id);
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

