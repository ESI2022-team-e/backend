package esi.backendnew.service;

import esi.backendnew.model.Car;
import esi.backendnew.repository.CarRepository;
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

    public void updateCar(UUID id, Car car) {
        carRepository.save(car);
    }

    public void deleteCar(UUID id) {
        carRepository.deleteById(id);
    }
}

