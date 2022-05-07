package esi.backend.controller;

import esi.backend.model.Invoice;
import esi.backend.security.service.UserDetailsImpl;
import esi.backend.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<Invoice> getInvoice(@PathVariable UUID invoiceId) {
        return invoiceService.getInvoice(invoiceId);
    }

    @RequestMapping("invoices")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @RequestMapping("customers/{customerId}/rentals/{rentalId}/invoices/{invoiceId}")
    public ResponseEntity<Invoice> getInvoiceByRentalId(@AuthenticationPrincipal final UserDetailsImpl currentUser, @PathVariable UUID rentalId) {
        return invoiceService.getInvoiceByRentalId(currentUser, rentalId);
    }

    @RequestMapping("customers/{customerId}/invoices")
    public ResponseEntity<List<Invoice>> getAllInvoicesByCustomerId(@AuthenticationPrincipal final UserDetailsImpl currentUser, @PathVariable long customerId) {
        return invoiceService.getAllInvoicesByCustomerId(currentUser, customerId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/cars/{carId}/rentals/{rentalId}/invoices")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> addInvoice(@RequestBody Invoice invoice, @PathVariable UUID rentalId) {
        return invoiceService.addInvoice(invoice, rentalId);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/customers/{customerId}/rentals/{rentalId}/invoices/{invoiceId}")
    public ResponseEntity<?> updateInvoice(@AuthenticationPrincipal final UserDetailsImpl currentUser, @PathVariable UUID invoiceId, @RequestBody Invoice invoice) {
        return invoiceService.updateInvoice(currentUser, invoiceId, invoice);
    }

}
