package esi.backend.controller;

import esi.backend.model.Customer;
import esi.backend.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8082")
@RestController
@RequestMapping("/api")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping("/customers")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @RequestMapping("/customers/{customerId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<Customer> getCustomer(@PathVariable long customerId) {
        return customerService.getCustomer(customerId);
    }

}
