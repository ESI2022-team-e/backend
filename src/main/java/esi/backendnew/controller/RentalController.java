package esi.backendnew.controller;

import esi.backendnew.model.Car;
import esi.backendnew.model.Rental;
import esi.backendnew.service.RentalService;
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

    @RequestMapping("/cars/{carId}/rentals")
    public List<Rental> getAllRentals(@PathVariable UUID carId) {
        return rentalService.getAllRentals(carId);
    }

    @RequestMapping("/cars/rentals/{id}")
    public Optional<Rental> getRental(@PathVariable UUID id) {
        return rentalService.getRental(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/cars/{carId}/rentals")
    public void addRental(@RequestBody Rental rental, @PathVariable UUID carId) {
        rental.setCar(new Car());
        rentalService.addRental(rental);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/cars/{carId}/rentals/{id}")
    public void updateRental(@RequestBody Rental rental, @PathVariable UUID
            carId, @PathVariable UUID id) {
        rental.setCar(new Car());
        rentalService.updateRental(rental);
    }

    @RequestMapping(method = RequestMethod.DELETE, value
            = "/cars/rentals/{id}")
    public void deleteRental(@PathVariable UUID id) {
        rentalService.deleteRental(id);
    }


}