package esi.backend.service;

import esi.backend.model.Car;
import esi.backend.model.Rental;
import esi.backend.model.RentalStatus;
import esi.backend.repository.CarRepository;
import esi.backend.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CarRepository carRepository;

    public List<Rental> getAllRentals(){
        return (List<Rental>) rentalRepository.findAll();
    }

    public List<Rental> getRentalsByCar(UUID carId) {
        return rentalRepository.findByCarId(carId);
    }

    public Optional<Rental> getRental(UUID id) {
        return rentalRepository.findById(id);
    }

    /*
    public List<Rental> getAllCustomerRentals(Integer customerId) {
        return rentalRepository.findByCustomerId(customerId);
    }
    */

    public void addRental(Rental rental) {
        rentalRepository.save(rental);
    }

    public void updateRental(Rental rental, UUID carId, UUID rentalId) {
        Optional<Car> car = carRepository.findById(carId);
        Rental existingRental = rentalRepository.findById(rentalId).get();

        if (rental.getStatus() != null)
            existingRental.setStatus(rental.getStatus());

        if (rental.getCar() != null && car.isPresent())
            existingRental.setCar(car.get());

        if (rental.getPickup_location() != null)
            existingRental.setPickup_location(rental.getPickup_location());

        if (rental.getPickup_datetime() != null)
            existingRental.setPickup_datetime(rental.getPickup_datetime());

        if (rental.getDropoff_location() != null)
            existingRental.setDropoff_location(rental.getDropoff_location());

        if (rental.getDropoff_datetime() != null)
            existingRental.setDropoff_datetime(rental.getDropoff_datetime());

        rentalRepository.save(rental);
    }

    public void deleteRental(UUID id) {
        rentalRepository.deleteById(id);
    }

}