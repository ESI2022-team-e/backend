package esi.backend.controller;

import esi.backend.model.Invoice;
import esi.backend.service.InvoiceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @RequestMapping("invoices/{invoiceId}")
    @PreAuthorize("hasRole('MANAGER')")
    public Invoice getInvoice(@AuthenticationPrincipal final UserDetails currentUser, @PathVariable UUID invoiceId) {
        return invoiceService.getInvoice(invoiceId);
    }

    @RequestMapping("customers/{customerId}/rentals/{rentalId}/invoices/{invoiceId}")
    public Invoice getInvoiceByRentalId(@AuthenticationPrincipal final UserDetails currentUser, @PathVariable UUID rentalId) {
        return invoiceService.getInvoiceByRentalId(rentalId);
    }

    // TODO: customer mappings
    /*
    @RequestMapping("customers/{customerId}/invoices")
    public List<Invoice> getAllInvoicesByCustomerId(@PathVariable UUID customerId) {
        return invoiceService.getAllInvoicesByCustomer(customerId);
    }
     */

    @RequestMapping("invoices")
    @PreAuthorize("hasRole('MANAGER')")
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/cars/{carId}/rentals/{rentalId}/invoices")
    @PreAuthorize("hasRole('MANAGER')")
    public void addInvoice(@RequestBody Invoice invoice, @PathVariable UUID rentalId) {
        invoiceService.addInvoice(invoice, rentalId);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/customers/{customerId}/rentals/{rentalId}/invoices/{invoiceId}")
    public void updateInvoice(@AuthenticationPrincipal final UserDetails currentUser, @PathVariable UUID invoiceId, @RequestBody Invoice invoice) {
        invoiceService.updateInvoice(invoiceId, invoice);
    }

}
