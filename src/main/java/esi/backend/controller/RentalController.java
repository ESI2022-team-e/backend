package esi.backend.controller;

import esi.backend.model.Car;
import esi.backend.model.Rental;
import esi.backend.service.RentalService;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public List<Rental> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @RequestMapping("/cars/{carId}/rentals")
    @PreAuthorize("hasRole('MANAGER')")
    public List<Rental> getAllRentals(@PathVariable UUID carId) {
        return rentalService.getAllRentals(carId);
    }

    @RequestMapping("/cars/{carId}/rentals/{rentalId}")
    @PreAuthorize("hasRole('MANAGER')")
    public Optional<Rental> getRental(@PathVariable UUID carId, @PathVariable UUID rentalId) {
        return rentalService.getRental(rentalId);
    }

    /*
    @RequestMapping("/customers/{customerId}/rentals")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<Rental> getAllCustomerRentals(@PathVariable Integer customerId) {
        return rentalService.getAllCustomerRentals(customerId);
    }

    @RequestMapping("/customers/{customerId}/rentals/{rentalId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Optional<Rental> getCustomerRental(@PathVariable UUID customerId, @PathVariable UUID rentalId) {
        return rentalService.getRental(rentalId);
    }*/


    @RequestMapping(method = RequestMethod.POST, value = "/cars/{carId}/rentals")
    @PreAuthorize("hasRole('MANAGER')")
    public void addRental(@RequestBody Rental rental, @PathVariable UUID carId) {
        rental.setCar(new Car());
        rentalService.addRental(rental);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/cars/{carId}/rentals/{rentalId}")
    @PreAuthorize("hasRole('MANAGER')")
    public void updateRental(@RequestBody Rental rental, @PathVariable UUID
            carId, @PathVariable UUID rentalId) {
        rental.setCar(new Car());
        rentalService.updateRental(rental);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/cars/{carId}/rentals/{rentalId}")
    @PreAuthorize("hasRole('MANAGER')")
    public void deleteRental(@PathVariable UUID rentalId, @PathVariable String carId) {
        rentalService.deleteRental(rentalId);
    }

}