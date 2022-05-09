package esi.backend.service;

import esi.backend.model.*;
import esi.backend.repository.CarRepository;
import esi.backend.repository.CustomerRepository;
import esi.backend.repository.RentalRepository;
import esi.backend.repository.RequestRepository;
import esi.backend.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    @Autowired
    private CustomerService customerService;

    public ResponseEntity<List<Request>> getAllRequests() {
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
        return (request == null)
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(request, HttpStatus.OK);
    }

    public ResponseEntity<List<Request>> getAllRequestsByCustomerId(UserDetailsImpl currentUser, long customerId) {
        ResponseEntity<Customer> customerResponseEntity = customerService.authenticateCustomer(currentUser, customerId);
        if (customerResponseEntity.getBody() == null)
            return new ResponseEntity<>(customerResponseEntity.getStatusCode());
        List<Request> requests = new ArrayList<>(customerResponseEntity.getBody().getRequests());
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    public ResponseEntity<Request> getRequestByCustomerId(UserDetailsImpl currentUser, long customerId, UUID requestId) {
        ResponseEntity<Customer> customerResponseEntity = customerService.authenticateCustomer(currentUser, customerId);
        if (customerResponseEntity.getBody() == null)
            return new ResponseEntity<>(customerResponseEntity.getStatusCode());
        Request request = requestRepository.findRequestByIdAndCustomerId(requestId, customerId).orElse(null);
        return (request == null)
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(request, HttpStatus.OK);
    }

    public void addRequest(UserDetails currentUser, Request request, UUID carId) {
        Car car = carRepository.findById(carId).orElse(null);
        if (car == null) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return;
        }
        request.setCar(car);
        customerRepository.findByUsername(currentUser.getUsername()).ifPresent(request::setCustomer);
        requestRepository.save(request);
    }

    public ResponseEntity<?> updateRequest(UserDetailsImpl currentUser, Request newData, UUID requestId) {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        long customerId = request.getCustomer().getId();
        ResponseEntity<Customer> customerResponseEntity = customerService.authenticateCustomer(currentUser, customerId);
        if (customerResponseEntity.getBody() == null) {
            return new ResponseEntity<>(customerResponseEntity.getStatusCode());
        }
        if (newData.getPickup_datetime() != null) {
            request.setPickup_datetime(newData.getPickup_datetime());
        }
        if (newData.getDropoff_datetime() != null) {
            request.setDropoff_datetime(newData.getDropoff_datetime());
        }
        if (newData.getDropoff_location() != null) {
            request.setDropoff_location(newData.getDropoff_location());
        }
        if (newData.getStatus() != null) {
            if (newData.getStatus().equals(RequestStatus.CANCELLED) || newData.getStatus().equals(RequestStatus.REJECTED) || newData.getStatus().equals(RequestStatus.PENDING)) {
                request.setStatus(newData.getStatus());
            }
            if (newData.getStatus().equals(RequestStatus.ACCEPTED) && currentUser.isManager()) {
                request.setStatus(newData.getStatus());
                createRental(request);
                requestRepository.save(request);
                return ResponseEntity.ok("Request updated successfully and new rental created!");
            }
        }
        requestRepository.save(request);
        return ResponseEntity.ok("Request updated successfully!");
    }

    public ResponseEntity<?> deleteRequest(UUID id) {
        requestRepository.deleteById(id);
        return ResponseEntity.ok("Request deleted successfully!");
    }


    public void createRental(Request request) {
        Rental rental = new Rental(request.getId(), request.getPickup_datetime(), request.getDropoff_datetime(), request.getPickup_location(), request.getDropoff_location(), RentalStatus.UPCOMING, request.getCar(), request.getCustomer());
        rentalRepository.save(rental);
    }


}
