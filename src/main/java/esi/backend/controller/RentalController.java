package esi.backend.controller;

import esi.backend.model.Car;
import esi.backend.model.Rental;
import esi.backend.service.CarService;
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
    private final CarService carService;

    public RentalController(RentalService rentalService, CarService carService) {
        this.rentalService = rentalService;
        this.carService = carService;
    }


    @RequestMapping("/rentals")
    public List<Rental> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @RequestMapping("/cars/{carId}/rentals")
    @PreAuthorize("hasRole('MANAGER')")
    public List<Rental> getRentalsByCar(@PathVariable UUID carId) {
        return rentalService.getRentalsByCar(carId);
    }

    @RequestMapping("/cars/{carId}/rentals/{rentalId}")
    @PreAuthorize("hasRole('MANAGER')")
    public Optional<Rental> getRental(@PathVariable UUID carId, @PathVariable UUID rentalId) {
        return rentalService.getRental(rentalId);
    }

    // TODO: customer is yet to be added
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
    }
    */

    @RequestMapping(method = RequestMethod.POST, value = "/cars/{carId}/rentals")
    @PreAuthorize("hasRole('MANAGER')")
    public void addRental(@RequestBody Rental rental, @PathVariable UUID carId) {
        Optional<Car> car = carService.getCar(carId);
        Rental newRental = new Rental();
        // TODO: I guess request information has to be taken

        if (car.isPresent())
            newRental.setCar(car.get());
        // TODO: not sure if all the other things should be added right-away as well

        rentalService.addRental(newRental);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/cars/{carId}/rentals/{rentalId}")
    @PreAuthorize("hasRole('MANAGER')")
    public void updateRental(@RequestBody Rental rental, @PathVariable UUID
            carId, @PathVariable UUID rentalId) {
        Optional<Car> car = carService.getCar(carId);
        Rental existingRental = rentalService.getRental(rentalId).get();

        if (rental.getStatus() != null)
            existingRental.setStatus(rental.getStatus());

        if (rental.getCar() != null && car.isPresent())
            existingRental.setCar(car.get());

        if (rental.getDropoff_location() != null)
            existingRental.setDropoff_location(rental.getDropoff_location());

        if (rental.getDropoff_date() != null)
            existingRental.setDropoff_date(rental.getDropoff_date());

        if (rental.getDropoff_time() != null)
            existingRental.setDropoff_time(rental.getDropoff_time());

        rentalService.updateRental(existingRental);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/cars/{carId}/rentals/{rentalId}")
    @PreAuthorize("hasRole('MANAGER')")
    public void deleteRental(@PathVariable UUID rentalId, @PathVariable String carId) {
        rentalService.deleteRental(rentalId);
    }

}