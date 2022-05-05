package esi.backend.service;

import esi.backend.exception.ResourceNotFoundException;
import esi.backend.model.Invoice;
import esi.backend.model.Rental;
import esi.backend.repository.InvoiceRepository;
import esi.backend.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private RentalRepository rentalRepository;


    public Invoice getInvoice(UUID id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice with id " + id + " not found."));
    }

    public List<Invoice> getAllInvoices() {
        return (List<Invoice>) invoiceRepository.findAll();
    }

    public Invoice getInvoiceByRentalId(UUID rentalId) {
        // TODO: add check that the one asking has either:
        //  * ROLE: MANAGER
        //  * ROLE: CUSTOMER + the same id as the customerId in api
        return invoiceRepository.findByRentalId(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental with id " + rentalId + " not found."));
    }

    // TODO: get all invoices of one customer
    /*
    public List<Invoice> getAllInvoicesByCustomer(UUID customerId) {
        // TODO: add check that the one asking has either:
        //  * ROLE: MANAGER
        //  * ROLE: CUSTOMER + the same id as the customerId in api
        return invoiceRepository.findByCustomerId(customerId);
    }
     */

    public void addInvoice(Invoice invoice, UUID rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental with id " + rentalId + " not found."));
        // only allow one invoice per rental
        boolean invoiceExisting = invoiceRepository.findByRentalId(rentalId).isPresent();
        if (!invoiceExisting) {
            invoice.setRental(rental);
            invoiceRepository.save(invoice);
        }
    }

    public void updateInvoice(UUID invoiceId, Invoice newInvoice) {
        Invoice invoice = invoiceRepository.findById(invoiceId).
                orElseThrow(() -> new ResourceNotFoundException("Invoice with id " + invoiceId + " not found."));
        // TODO: check if currentUser.id == customerId
        // TODO: ? does this happen automatically or does it have to be explicitly checked
        // only allow changing the status of the invoice
        invoice.setStatus(newInvoice.getStatus());
        invoiceRepository.save(invoice);
    }

}
