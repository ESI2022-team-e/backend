package esi.backend.repository;

import esi.backend.model.Invoice;
import esi.backend.model.Rental;
import org.apache.tomcat.jni.Local;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentalRepository extends CrudRepository<Rental, UUID> {
    List<Rental> findByCarId(UUID carId);
    List<Rental> findAll();
    Optional<Rental> findRentalByIdAndCarId(UUID rentalId, UUID carId);
    List<Rental> getRentalByPickupDatetimeBeforeAndDropoffDatetimeAfter(LocalDateTime dropoffDateTime, LocalDateTime pickupDateTime);
    // List<Rental> findByCustomerId(Integer customerId);
}