package esi.backend.service;

import esi.backend.exception.ResourceNotFoundException;
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
        customerRepository.findCustomerByUsername(currentUser.getUsername()).ifPresent(request::setCustomer);
        requestRepository.save(request);
    }

    public void updateRequest(UserDetailsImpl currentUser, Request request, UUID requestId) {
        Request currentRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request with id " + requestId + " not found"));
        long customerId = currentRequest.getCustomer().getId();
        ResponseEntity<Customer> customerResponseEntity = customerService.authenticateCustomer(currentUser, customerId);
        if (customerResponseEntity.getBody() == null) {
            new ResponseEntity<>(customerResponseEntity.getStatusCode());
            return;
        }
        if (request.getPickupDatetime() != null) {
            currentRequest.setPickupDatetime(request.getPickupDatetime());
        }
        if (request.getDropoffDatetime() != null) {
            currentRequest.setDropoffDatetime(request.getDropoffDatetime());
        }
        if (request.getDropoffLocation() != null) {
            currentRequest.setDropoffLocation(request.getDropoffLocation());
        }
        if (request.getStatus() != null) {
            if (request.getStatus().equals(RequestStatus.CANCELLED) || request.getStatus().equals(RequestStatus.REJECTED) || request.getStatus().equals(RequestStatus.PENDING)) {
                currentRequest.setStatus(request.getStatus());
            }
            if (request.getStatus().equals(RequestStatus.ACCEPTED) && currentUser.isManager()) {
                currentRequest.setStatus(request.getStatus());
                createRental(currentRequest);
            }
        }
        requestRepository.save(currentRequest);
    }

    public void deleteRequest(UUID id) {
        requestRepository.deleteById(id);
    }


    public void createRental(Request request) {
        Rental rental = new Rental(request.getId(), request.getPickupDatetime(), request.getDropoffDatetime(), request.getPickupLocation(), request.getDropoffLocation(), RentalStatus.UPCOMING, request.getCar(), request.getCustomer());
        rentalRepository.save(rental);
    }


}
