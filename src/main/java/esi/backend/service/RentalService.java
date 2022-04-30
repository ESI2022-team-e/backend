package esi.backend.service;

import esi.backend.model.Rental;
import esi.backend.repository.RentalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RentalService {

    private RentalRepository rentalRepository;

    public List<Rental> getAllRentals(UUID carId) {
        return rentalRepository.findByCarId(carId);
    }

    public Optional<Rental> getRental(UUID id) {
        return rentalRepository.findById(id);
    }

    public void addRental(Rental rental) {
        rentalRepository.save(rental);
    }

    public void updateRental(Rental rental) {
        rentalRepository.save(rental);
    }

    public void deleteRental(UUID id) {
        rentalRepository.deleteById(id);
    }

}