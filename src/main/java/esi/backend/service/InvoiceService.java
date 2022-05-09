package esi.backend.service;

import esi.backend.model.Customer;
import esi.backend.model.Invoice;
import esi.backend.model.Rental;
import esi.backend.repository.CustomerRepository;
import esi.backend.repository.InvoiceRepository;
import esi.backend.repository.RentalRepository;
import esi.backend.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CustomerService customerService;


    public ResponseEntity<Invoice> getInvoice(UUID id) {
        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        return (invoice == null)
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    public ResponseEntity<List<Invoice>> getAllInvoices() {
        return new ResponseEntity<>(invoiceRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Invoice> getInvoiceByRentalId(UserDetailsImpl currentUser, UUID rentalId) {
        Invoice invoice = invoiceRepository.findByRentalId(rentalId).orElse(null);
        if (invoice == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        long customerId = invoice.getCustomer().getId();
        ResponseEntity<Customer> customerResponseEntity = customerService.authenticateCustomer(currentUser, customerId);
        if (customerResponseEntity.getBody() == null) {
            return new ResponseEntity<>(customerResponseEntity.getStatusCode());
        }
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    public ResponseEntity<List<Invoice>> getAllInvoicesByCustomerId(UserDetailsImpl currentUser, long customerId) {
        ResponseEntity<Customer> customerResponseEntity = customerService.authenticateCustomer(currentUser, customerId);
        if (customerResponseEntity.getBody() == null) {
            return new ResponseEntity<>(customerResponseEntity.getStatusCode());
        }
        return new ResponseEntity<>(invoiceRepository.findByCustomerId(customerId), HttpStatus.OK);
    }

    public ResponseEntity<?> addInvoice(Invoice invoice, UUID rentalId) {
        Rental rental = rentalRepository.findById(rentalId).orElse(null);
        if (rental == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // only allow one invoice per rental
        boolean invoiceExisting = invoiceRepository.findByRentalId(rentalId).isPresent();
        if (invoiceExisting) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        invoice.setRental(rental);
        invoice.setCustomer(rental.getCustomer());
        invoiceRepository.save(invoice);
        return ResponseEntity.ok("Invoice added successfully!");

    }

    public ResponseEntity<?> updateInvoice(UserDetailsImpl currentUser, UUID invoiceId, Invoice newInvoice) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElse(null);
        if (invoice == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        long customerId = invoice.getCustomer().getId();
        ResponseEntity<Customer> customerResponseEntity = customerService.authenticateCustomer(currentUser, customerId);
        if (customerResponseEntity.getBody() == null) {
            return new ResponseEntity<>(customerResponseEntity.getStatusCode());
        }
        // only allow changing the status of the invoice
        invoice.setStatus(newInvoice.getStatus());
        invoiceRepository.save(invoice);
        return ResponseEntity.ok("Invoice updated successfully!");
    }

}