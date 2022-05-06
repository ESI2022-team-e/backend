package esi.backend.service;

import esi.backend.exception.ResourceNotFoundException;
import esi.backend.model.*;
import esi.backend.repository.CarRepository;
import esi.backend.repository.CustomerRepository;
import esi.backend.repository.RentalRepository;
import esi.backend.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public ResponseEntity<Object> getAllRequests() {
        return new ResponseEntity<>(requestRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<List<Request>> getAllRequestsByCarId(UUID carId) {
        Car car = carRepository.findById(carId).orElse(null);
        if (car == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(requestRepository.findByCarId(carId), HttpStatus.OK);
    }

    public ResponseEntity<Request> getRequest(UUID id) {
        Request request = requestRepository.findById(id).orElse(null);
        return (request == null) ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(request, HttpStatus.OK);
    }

    public void addRequest(UserDetails currentUser, Request request, UUID carId) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        if (optionalCar.isPresent()) {
            request.setCar(optionalCar.get());
        } else throw new ResourceNotFoundException("Car with id" + carId + "not found");
        Optional<Customer> optionalCustomer = customerRepository.findByUsername(currentUser.getUsername());
        optionalCustomer.ifPresent(request::setCustomer);
        requestRepository.save(request);
    }

    public void updateRequest(UserDetails user, Request request, UUID id) {
        Request req = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request with id " + id + " not found"));
        if (request.getPickup_datetime() != null) {
            req.setPickup_datetime(request.getPickup_datetime());
        }
        if (request.getDropoff_datetime() != null) {
            req.setDropoff_datetime(request.getDropoff_datetime());
        }
        if (request.getDropoff_location() != null) {
            req.setDropoff_location(request.getDropoff_location());
        }
        if (request.getStatus() != null) {
            if (request.getStatus().equals(RequestStatus.CANCELLED) || request.getStatus().equals(RequestStatus.REJECTED) || request.getStatus().equals(RequestStatus.PENDING)) {
                req.setStatus(request.getStatus());
            }
            if (request.getStatus().equals(RequestStatus.ACCEPTED) && user.getAuthorities().contains(new SimpleGrantedAuthority(ERole.ROLE_MANAGER.name()))) {
                req.setStatus(request.getStatus());
                createRental(req);
            }
        }
        requestRepository.save(req);
    }

    public void deleteRequest(UUID id) {
        requestRepository.deleteById(id);
    }

    public ResponseEntity<List<Request>> getAllRequestsByCustomerId(UserDetails currentUser, long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            //throw new ResourceNotFoundException("Customer with id" + customerId + "not found");
        }
        if (!currentUser.getUsername().equals(customer.getUsername())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<Request> requests = new ArrayList<>(customer.getRequests());
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    public void createRental(Request request) {
        Rental rental = new Rental(request.getId(), request.getPickup_datetime(), request.getDropoff_datetime(), request.getPickup_location(), request.getDropoff_location(), RentalStatus.UPCOMING, request.getCar(), request.getCustomer());
        rentalRepository.save(rental);
    }
}
