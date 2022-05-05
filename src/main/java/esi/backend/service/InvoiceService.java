package esi.backend.service;

import esi.backend.model.Invoice;
import esi.backend.repository.InvoiceRepository;
import esi.backend.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private RentalRepository rentalRepository;


    public Optional<Invoice> getInvoice(UUID id) {
        return invoiceRepository.findById(id);
    }

    public List<Invoice> getAllInvoices() {
        return (List<Invoice>) invoiceRepository.findAll();
    }

    public Optional<Invoice> getInvoiceByRental(UUID rentalId) {
        return invoiceRepository.findByRentalId(rentalId);
    }

    // TODO: get all invoices of one customer
    /*
    public List<Invoice> getInvoicesByCustomer(UUID customerId) {
        return invoiceRepository.findByCustomerId(customerId);
    }
     */

    public void addInvoice(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    public void updateInvoice(Invoice invoice) {
        // only allow changing the status of the invoice
        Invoice existingInvoice = invoiceRepository.findById(invoice.getId()).orElse(null);
        if (existingInvoice != null) {
            existingInvoice.setStatus(invoice.getStatus());
            invoiceRepository.save(existingInvoice);
        }

    }

}
