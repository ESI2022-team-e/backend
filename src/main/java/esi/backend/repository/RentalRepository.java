package esi.backend.repository;

import esi.backend.model.Rental;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface RentalRepository extends CrudRepository<Rental, UUID> {
    List<Rental> findByCarId(UUID carId);
    // List<Rental> findByCustomerId(Integer customerId);
}