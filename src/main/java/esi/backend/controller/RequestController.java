package esi.backend.controller;

import esi.backend.model.Request;
import esi.backend.security.service.UserDetailsImpl;
import esi.backend.service.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8082")
@RestController
@RequestMapping("/api")
public class RequestController {

    private final RequestService requestService;


    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @RequestMapping("/requests")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<Request>> getAllRequests() {
        return requestService.getAllRequests();
    }

    @RequestMapping("/cars/{carId}/requests")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<Request>> getAllRequestsByCarId(@PathVariable UUID carId) {
        return requestService.getAllRequestsByCarId(carId);
    }

    @RequestMapping("/cars/{carId}/requests/{requestId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Request> getRequest(@PathVariable UUID requestId, @PathVariable UUID carId) {
        return requestService.getRequest(requestId);
    }

    @RequestMapping("/requests/{requestId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Request> getRequestById(@PathVariable UUID requestId) {
        return requestService.getRequest(requestId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/cars/{carId}/requests")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> addRequest(@AuthenticationPrincipal final UserDetailsImpl currentUser, @RequestBody Request request, @PathVariable UUID carId) {
        requestService.addRequest(currentUser, request, carId);
        return ResponseEntity.ok("Request added successfully!");
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/cars/{carId}/requests/{requestId}")
    public ResponseEntity<?> updateRequest(@AuthenticationPrincipal final UserDetailsImpl currentUser, @RequestBody Request request, @PathVariable UUID carId, @PathVariable UUID requestId) {
        return requestService.updateRequest(currentUser, request, requestId);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/cars/{carId}/requests/{requestId}")
    public ResponseEntity<?> deleteRequest(@PathVariable UUID requestId, @PathVariable String carId) {
        return requestService.deleteRequest(requestId);
    }

    @GetMapping("/customers/{customerId}/requests")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MANAGER')")
    public ResponseEntity<List<Request>> getCustomerRequests(
            @AuthenticationPrincipal final UserDetailsImpl currentUser,
            @PathVariable Long customerId) {
        return requestService.getAllRequestsByCustomerId(currentUser, customerId);
    }

    @GetMapping("/customers/{customerId}/requests/{requestId}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MANAGER')")
    public ResponseEntity<Request> getCustomerRequest(
            @AuthenticationPrincipal final UserDetailsImpl currentUser,
            @PathVariable long customerId,
            @PathVariable UUID requestId) {
        return requestService.getRequestByCustomerId(currentUser, customerId, requestId);
    }
}
