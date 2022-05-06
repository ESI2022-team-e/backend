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
    private CustomerRepository customerRepository;


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
        if (!currentUser.getId().equals(invoice.getCustomer().getId()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        // TODO: should any ROLE_MANAGER also be able to access this api?
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    public ResponseEntity<List<Invoice>> getAllInvoicesByCustomerId(UserDetailsImpl currentUser, long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (!currentUser.getId().equals(customer.getId()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        // TODO: should any ROLE_MANAGER also be able to access this api?
        return new ResponseEntity<>(invoiceRepository.findByCustomerId(customerId), HttpStatus.OK);
    }

    public void addInvoice(Invoice invoice, UUID rentalId) {
        Rental rental = rentalRepository.findById(rentalId).orElse(null);
        if (rental == null) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return;
        }
        // only allow one invoice per rental
        boolean invoiceExisting = invoiceRepository.findByRentalId(rentalId).isPresent();
        if (invoiceExisting) {
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return;
        }
        invoice.setRental(rental);
        invoice.setCustomer(rental.getCustomer());
        invoiceRepository.save(invoice);
        new ResponseEntity<>(HttpStatus.OK);

    }

    public void updateInvoice(UserDetailsImpl currentUser, UUID invoiceId, Invoice newInvoice) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElse(null);
        if (invoice == null) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return;
        }
        if (!currentUser.getId().equals(invoice.getCustomer().getId())) {
            new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            return;
        }
        // TODO: should manager be able to change the invoice status as well? - I propose no.
        // only allow changing the status of the invoice
        invoice.setStatus(newInvoice.getStatus());
        invoiceRepository.save(invoice);
        new ResponseEntity<>(HttpStatus.OK);
    }

}
