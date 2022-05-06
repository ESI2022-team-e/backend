package esi.backend.controller;

import esi.backend.exception.ResourceNotFoundException;
import esi.backend.model.*;
import esi.backend.repository.CustomerRepository;
import esi.backend.security.service.UserDetailsImpl;
import esi.backend.service.CarService;
import esi.backend.service.CustomerService;
import esi.backend.service.RentalService;
import esi.backend.service.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class RequestController {

    private final RequestService requestService;
    private final CarService carService;
    private final RentalService rentalService;
    private final CustomerService customerService;

    public RequestController(RequestService requestService, CarService carService, RentalService rentalService, CustomerService customerService) {
        this.requestService = requestService;
        this.carService = carService;
        this.rentalService = rentalService;
        this.customerService = customerService;
    }

    @RequestMapping("requests")
    @PreAuthorize("hasRole('MANAGER')")
    public List<Request> getAllRequests() {
        return requestService.getAllRequests();
    }

    @RequestMapping("cars/{carId}/requests")
    @PreAuthorize("hasRole('MANAGER')")
    public List<Request> getAllRequestsByCarId(@PathVariable UUID carId) {
        return requestService.getAllRequestsByCarId(carId);
    }

    @RequestMapping("cars/{carId}/requests/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public Optional<Request> getRequest(@PathVariable UUID id) {
        return requestService.getRequest(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/cars/{carId}/requests")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> addRequest(
            @AuthenticationPrincipal final UserDetailsImpl currentUser,
            @RequestBody Request request,
            @PathVariable UUID carId) {
        Optional<Car> optionalCar = carService.getCar(carId);
        if (optionalCar.isPresent()) {
            request.setCar(optionalCar.get());
        } else throw new ResourceNotFoundException("Car with id" + carId + "not found");
        Optional<Customer> optionalCustomer = customerService.getCustomer(currentUser.getId());
        optionalCustomer.ifPresent(request::setCustomer);
        requestService.addRequest(request);
        return ResponseEntity.ok("Request created successfully!");
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/cars/{carId}/requests/{id}")
    public ResponseEntity<?> updateRequest(
            @AuthenticationPrincipal final UserDetailsImpl currentUser,
            @RequestBody Request request,
            @PathVariable UUID carId,
            @PathVariable UUID id) {
        Request req = requestService.getRequest(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request with id " + id + "not found"));
        if (request.getPickup_datetime() != null) {
            req.setPickup_datetime(request.getPickup_datetime());
        }
        if (request.getDropoff_datetime() != null) {
            req.setDropoff_datetime(request.getDropoff_datetime());
        }
        if (request.getDropoff_location() != null) {
            req.setDropoff_location(request.getDropoff_location());
        }
        if (request.getStatus() != null) {
            if (request.getStatus().equals(RequestStatus.CANCELLED) || request.getStatus().equals(RequestStatus.REJECTED) || request.getStatus().equals(RequestStatus.PENDING)) {
                req.setStatus(request.getStatus());
            }
            if (request.getStatus().equals(RequestStatus.ACCEPTED) && currentUser.isManager()) {
                req.setStatus(request.getStatus());
                createRental(req);
            }
        }
        requestService.updateRequest(req);
        return ResponseEntity.ok("Request updated successfully!");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/cars/{carId}/requests/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable UUID id) {
        requestService.deleteRequest(id);
        return ResponseEntity.ok("Request deleted successfully!");
    }

    @GetMapping("/customers/{customerId}/requests")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MANAGER')")
    public ResponseEntity<List<Request>> getCustomerRequests(
            @AuthenticationPrincipal final UserDetailsImpl currentUser,
            @PathVariable Long customerId) {
        return customerService.getAllRequestsByCustomerId(currentUser, customerId);
    }

    @GetMapping("/customers/{customerId}/requests/{requestId}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MANAGER')")
    public ResponseEntity<Request> getCustomerRequest(
            @AuthenticationPrincipal final UserDetailsImpl currentUser,
            @PathVariable long customerId,
            @PathVariable UUID requestId) {
        return customerService.getRequestByCustomerId(currentUser, customerId, requestId);
    }

    public void createRental(Request request) {
        Rental rental = new Rental(request.getId(),
                request.getPickup_datetime(),
                request.getDropoff_datetime(),
                request.getPickup_location(),
                request.getDropoff_location(),
                RentalStatus.UPCOMING,
                request.getCar(),
                request.getCustomer()
        );
        rentalService.addRental(rental);
    }
}
