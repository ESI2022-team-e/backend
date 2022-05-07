package esi.backend.controller;

import esi.backend.model.Request;
import esi.backend.service.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @RequestMapping("requests")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<Request>> getAllRequests() {
        return requestService.getAllRequests();
    }

    @RequestMapping("cars/{carId}/requests")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<Request>> getAllRequestsByCarId(@PathVariable UUID carId) {
        return requestService.getAllRequestsByCarId(carId);
    }

    @RequestMapping("cars/{carId}/requests/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Request> getRequest(@PathVariable UUID id) {
        return requestService.getRequest(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/cars/{carId}/requests")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> addRequest(@AuthenticationPrincipal final UserDetails currentUser, @RequestBody Request request, @PathVariable UUID carId) {
        requestService.addRequest(currentUser, request, carId);
        return ResponseEntity.ok("Request added successfully!");
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/cars/{carId}/requests/{id}")
    public ResponseEntity<?> updateRequest(@AuthenticationPrincipal final UserDetails currentUser, @RequestBody Request request, @PathVariable UUID carId, @PathVariable UUID id) {
        requestService.updateRequest(currentUser, request, carId);
        return ResponseEntity.ok("Request updated successfully!");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/cars/{carId}/requests/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable UUID id) {
        requestService.deleteRequest(id);
        return ResponseEntity.ok("Request deleted successfully!");
    }

    @GetMapping("/customers/{customerId}/requests")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<Request>> getCustomerRequests(@AuthenticationPrincipal final UserDetails currentUser, @PathVariable Long customerId) {
        return requestService.getAllRequestsByCustomerId(currentUser, customerId);
    }
}
