package esi.backend.service;

import esi.backend.model.*;
import esi.backend.payload.response.MessageResponse;
import esi.backend.repository.CarRepository;
import esi.backend.repository.CustomerRepository;
import esi.backend.repository.InvoiceRepository;
import esi.backend.repository.RentalRepository;
import esi.backend.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private InvoiceRepository invoiceRepository;


    public ResponseEntity<List<Rental>> getAllRentals() {
        return new ResponseEntity<>(rentalRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<List<Rental>> getRentalsByCar(UUID carId) {
        Car car = carRepository.findById(carId).orElse(null);
        return (car == null)
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(rentalRepository.findByCarId(carId),HttpStatus.OK);
    }

    public ResponseEntity<Rental> getRental(UUID rentalId) {
        Rental rental = rentalRepository.findById(rentalId).orElse(null);
        return (rental == null)
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(rental, HttpStatus.OK);
    }


    public ResponseEntity<List<Rental>> getAllRentalsByCustomerId(UserDetailsImpl currentUser, long customerId) {
        ResponseEntity<Customer> customerResponseEntity = customerService.authenticateCustomer(currentUser, customerId);
        if (customerResponseEntity.getBody() == null)
            return new ResponseEntity<>(customerResponseEntity.getStatusCode());
        List<Rental> rentals = new ArrayList<>(customerResponseEntity.getBody().getRentals());
        return new ResponseEntity<>(rentals, HttpStatus.OK);
    }

    public ResponseEntity<Rental> getRentalByCustomerId(UserDetailsImpl currentUser,long customerId,UUID rentalId){
        ResponseEntity<Customer> customerResponseEntity = customerService.authenticateCustomer(currentUser, customerId);
        if (customerResponseEntity.getBody() == null)
            return new ResponseEntity<>(customerResponseEntity.getStatusCode());
        Rental rental = customerResponseEntity.getBody().getRentals().stream().filter(
                req -> req.getId().equals(rentalId)).findFirst().orElse(null);
        return (rental == null)
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(rental, HttpStatus.OK);
    }


    public ResponseEntity<?> addRental(Rental rental) {
        rentalRepository.save(rental);
        return ResponseEntity.ok("Rental added successfully!");
    }

    public ResponseEntity<?> updateRental(Rental rental, UUID carId, UUID rentalId) {
        Rental existingRental = rentalRepository.findRentalByIdAndCarId(rentalId,carId).orElse(null);

        if (existingRental == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (existingRental.getStatus().equals(RentalStatus.DONE)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (rental.getPickupDatetime() != null || rental.getDropoffDatetime() != null)
            existingRental = extendRental(rental, existingRental);
        if (existingRental == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (rental.getPickupLocation() != null)
            existingRental.setPickupLocation(rental.getPickupLocation());

        if (rental.getDropoffLocation() != null)
            existingRental.setDropoffLocation(rental.getDropoffLocation());

        if (rental.getStatus() != null){
            if (rental.getStatus().equals(RentalStatus.DONE) && (existingRental.getStatus().equals(RentalStatus.CURRENT)))
                createInvoice(existingRental);
            existingRental.setStatus(rental.getStatus());
            }

        rentalRepository.save(existingRental);
        return ResponseEntity.ok("Rental updated successfully!");
    }

    public ResponseEntity<?> deleteRental(UUID id) {
        rentalRepository.deleteById(id);
        return ResponseEntity.ok("Rental deleted successfully!");
    }

    private Rental extendRental(Rental rental, Rental existingRental){
        if (rental.getDropoffDatetime() != null) existingRental.setDropoffDatetime(rental.getDropoffDatetime());
        if (rental.getPickupDatetime() != null) {
            if (existingRental.getStatus().equals(RentalStatus.UPCOMING))
                existingRental.setPickupDatetime(rental.getPickupDatetime());
        }
        return rentalRepository.getRentalByPickupDatetimeBeforeAndDropoffDatetimeAfter(
                existingRental.getDropoffDatetime(),existingRental.getPickupDatetime()).isEmpty()
                ? existingRental
                : null;
    }


    public void createInvoice(Rental rental) {
        Invoice invoice = new Invoice(
                rental.getId(),
                rental.getPickupDatetime(),
                InvoiceStatus.UNPAID,
                rental,
                rental.getCustomer()
        );
        invoiceRepository.save(invoice);
    }


}