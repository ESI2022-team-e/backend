package esi.backend.service;

import esi.backend.exception.ResourceNotFoundException;
import esi.backend.exception.UnauthorizedException;
import esi.backend.model.Car;
import esi.backend.model.Customer;
import esi.backend.model.Request;
import esi.backend.repository.CustomerRepository;
import esi.backend.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public List<Customer> getAllCustomers(){
        List<Customer> customers = new ArrayList<>();
        customerRepository.findAll().forEach(customers::add);
        return customers;
    }

    public Optional<Customer> getCustomer(long id) {
        return customerRepository.findById(id);
    }

    public ResponseEntity<List<Request>> getAllRequestsByCustomerId(UserDetailsImpl currentUser, long customerId) {
        List<Request> requests = new ArrayList<>(findCustomer(currentUser,customerId).getRequests());
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    public ResponseEntity<Request> getRequestByCustomerId(UserDetailsImpl currentUser, long customerId, UUID requestId) {
        Request request = findCustomer(currentUser,customerId).getRequests().stream().filter(
                req -> req.getId().equals(requestId)).findFirst().
                orElseThrow(() -> new ResourceNotFoundException("Request with id " + requestId + "not found"));
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    private Customer findCustomer(UserDetailsImpl currentUser, long customerId){
        Customer customer = customerRepository.findById(customerId).
                orElseThrow(() -> new ResourceNotFoundException("Customer with id " + customerId + "not found"));
        if (!currentUser.isManager() && !currentUser.getId().equals(customer.getId()))
            throw new UnauthorizedException("Unauthorized access to customer " +customerId + " data.");
        return customer;
    }
}
