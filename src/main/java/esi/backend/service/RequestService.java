package esi.backend.service;

import esi.backend.exception.ResourceNotFoundException;
import esi.backend.model.Customer;
import esi.backend.model.Request;
import esi.backend.repository.CustomerRepository;
import esi.backend.repository.RequestRepository;
import esi.backend.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private CustomerRepository customerRepository;

    public List<Request> getAllRequests() {
        List<Request> requests = new ArrayList<>();
        requestRepository.findAll().forEach(requests::add);
        return requests;
    }

    public List<Request> getAllRequestsByCarId(UUID carId) {
        return requestRepository.findByCarId(carId);
    }

    public Optional<Request> getRequest(UUID id) {
        return requestRepository.findById(id);
    }

    public void addRequest(Request request) {
        requestRepository.save(request);
    }

    public void updateRequest(Request request) {
        requestRepository.save(request);
    }

    public void deleteRequest(UUID id) {
        requestRepository.deleteById(id);
    }

    public ResponseEntity<List<Request>> getAllRequestsByCustomerId(UserDetailsImpl currentUser, long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            //throw new ResourceNotFoundException("Customer with id" + customerId + "not found");

        if (currentUser.isCustomer() && !currentUser.getUsername().equals(customer.getUsername()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);


        List<Request> requests = new ArrayList<>(customer.getRequests());
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    public ResponseEntity<Request> getRequestByCustomerId(UserDetailsImpl currentUser, long customerId, UUID requestId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        //throw new ResourceNotFoundException("Customer with id" + customerId + "not found");

        if (currentUser.isCustomer() && !currentUser.getUsername().equals(customer.getUsername()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Request request = customer.getRequests().stream().filter(req -> req.getId().equals(requestId)).findFirst().
                orElseThrow(() -> new ResourceNotFoundException("Request with id " + requestId + "not found"));
        return new ResponseEntity<>(request, HttpStatus.OK);
    }
}
