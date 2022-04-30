package esi.backendnew.repository;

import java.util.List;
import java.util.UUID;

import esi.backendnew.model.Rental;
import org.springframework.data.repository.CrudRepository;

public interface RentalRepository extends CrudRepository<Rental, UUID> {
    List<Rental> findByCarId(UUID carId);
}