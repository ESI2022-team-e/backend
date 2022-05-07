package esi.backend.service;

import esi.backend.exception.ResourceNotFoundException;
import esi.backend.exception.UnauthorizedException;
import esi.backend.model.Car;
import esi.backend.model.Customer;
import esi.backend.model.Invoice;
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

    public ResponseEntity<List<Customer>> getAllCustomers(){
        List<Customer> customers = new ArrayList<>();
        customerRepository.findAll().forEach(customers::add);
        return new ResponseEntity<>(customers,HttpStatus.OK);
    }

    public ResponseEntity<Customer> getCustomer(long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        return (customer == null)
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(customer, HttpStatus.OK);
    }

    public ResponseEntity<Customer> authenticateCustomer(UserDetailsImpl currentUser, long customerId){
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (!currentUser.isManager() && !currentUser.getId().equals(customer.getId()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }
}
