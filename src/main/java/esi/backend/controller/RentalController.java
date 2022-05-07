package esi.backend.controller;

import esi.backend.model.Rental;
import esi.backend.security.service.UserDetailsImpl;
import esi.backend.service.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }


    @RequestMapping("/rentals")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<Rental>> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @RequestMapping("/cars/{carId}/rentals")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<Rental>> getRentalsByCar(@PathVariable UUID carId) {
        return rentalService.getRentalsByCar(carId);
    }

    @RequestMapping("/cars/{carId}/rentals/{rentalId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Rental> getRental(@PathVariable UUID carId, @PathVariable UUID rentalId) {
        return rentalService.getRental(rentalId);
    }


    @RequestMapping("/customers/{customerId}/rentals")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<Rental>> getAllRentalsByCustomerId(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @PathVariable long customerId) {
        return rentalService.getAllRentalsByCustomerId(currentUser,customerId);
    }

    @RequestMapping("/customers/{customerId}/rentals/{rentalId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Rental> getRentalByCustomerId(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @PathVariable long customerId,
            @PathVariable UUID rentalId) {
        return rentalService.getRentalByCustomerId(currentUser,customerId,rentalId);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/cars/{carId}/rentals")
    @PreAuthorize("hasRole('MANAGER')")
    public void addRental(@RequestBody Rental rental, @PathVariable UUID carId) {
        rentalService.addRental(rental);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/cars/{carId}/rentals/{rentalId}")
    @PreAuthorize("hasRole('MANAGER')")
    public void updateRental(@RequestBody Rental rental, @PathVariable UUID
            carId, @PathVariable UUID rentalId) {
        rentalService.updateRental(rental, carId, rentalId);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/cars/{carId}/rentals/{rentalId}")
    @PreAuthorize("hasRole('MANAGER')")
    public void deleteRental(@PathVariable UUID rentalId, @PathVariable String carId) {
        rentalService.deleteRental(rentalId);
    }

}