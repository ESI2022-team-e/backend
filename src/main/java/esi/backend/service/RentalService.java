package esi.backend.service;

import esi.backend.model.*;
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
    private CustomerRepository customerRepository;

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
        ResponseEntity<Customer> customerResponseEntity = authenticateCustomer(currentUser, customerId);
        if (customerResponseEntity.getBody() == null)
            return new ResponseEntity<>(customerResponseEntity.getStatusCode());
        List<Rental> rentals = new ArrayList<>(customerResponseEntity.getBody().getRentals());
        return new ResponseEntity<>(rentals, HttpStatus.OK);
    }

    public ResponseEntity<Rental> getRentalByCustomerId(UserDetailsImpl currentUser,long customerId,UUID rentalId){
        ResponseEntity<Customer> customerResponseEntity = authenticateCustomer(currentUser, customerId);
        if (customerResponseEntity.getBody() == null)
            return new ResponseEntity<>(customerResponseEntity.getStatusCode());
        Rental rental = customerResponseEntity.getBody().getRentals().stream().filter(
                req -> req.getId().equals(rentalId)).findFirst().orElse(null);
        return (rental == null)
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(rental, HttpStatus.OK);
    }


    public void addRental(Rental rental) {
        rentalRepository.save(rental);
    }

    public void updateRental(Rental rental, UUID carId, UUID rentalId) {
        Car car = carRepository.findById(carId).orElse(null);
        Rental existingRental = rentalRepository.findById(rentalId).orElse(null);

        if (existingRental == null) return;
        if (existingRental.getStatus().equals(RentalStatus.DONE)) return;

        if (rental.getCar() != null && car != null)
            existingRental.setCar(car);

        if (rental.getPickup_location() != null)
            existingRental.setPickup_location(rental.getPickup_location());

        if (rental.getPickup_datetime() != null)
            existingRental.setPickup_datetime(rental.getPickup_datetime());

        if (rental.getDropoff_location() != null)
            existingRental.setDropoff_location(rental.getDropoff_location());

        if (rental.getDropoff_datetime() != null)
            existingRental.setDropoff_datetime(rental.getDropoff_datetime());

        if (rental.getStatus() != null){
            if (rental.getStatus().equals(RentalStatus.DONE) && (existingRental.getStatus().equals(RentalStatus.CURRENT)))
                createInvoice(existingRental);
            existingRental.setStatus(rental.getStatus());
            }

        rentalRepository.save(existingRental);
    }


    public void deleteRental(UUID id) {
        rentalRepository.deleteById(id);
    }

    public void createInvoice(Rental rental) {
        Invoice invoice = new Invoice(
                rental.getId(),
                rental.getPickup_datetime(),
                InvoiceStatus.UNPAID,
                rental,
                rental.getCustomer()
        );
        invoiceRepository.save(invoice);
    }

    // TODO find a way to avoid duplication of this method
    public ResponseEntity<Customer> authenticateCustomer(UserDetailsImpl currentUser, long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        // TODO change SimpleGrantedAuthority to isManager()
        if (!currentUser.getAuthorities().contains(new SimpleGrantedAuthority(ERole.ROLE_MANAGER.name())) && !currentUser.getId().equals(customer.getId()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

}