package esi.backend.controller;

import esi.backend.exception.ResourceNotFoundException;
import esi.backend.model.*;
import esi.backend.service.CarService;
import esi.backend.service.RentalService;
import esi.backend.service.RequestService;
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

    public RequestController(RequestService requestService, CarService carService, RentalService rentalService) {
        this.requestService = requestService;
        this.carService = carService;
        this.rentalService = rentalService;
    }

    @RequestMapping("cars/{carId}/requests")
    public List<Request> getAllRequests(@PathVariable UUID carId) {
        return requestService.getAllRequests(carId);
    }

    @RequestMapping("cars/requests/{id}")
    public Optional<Request> getRequest(@PathVariable UUID id) {
        return requestService.getRequest(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/cars/{carId}/requests")
    public void addRequest(@RequestBody Request request, @PathVariable UUID carId) {
        Optional<Car> optionalCar = carService.getCar(carId);
        if (optionalCar.isPresent()) {
            request.setCar(optionalCar.get());
        } else throw new ResourceNotFoundException("Car with id" + carId + "not found");
        requestService.addRequest(request);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/cars/{carId}/requests/{id}")
    public void updateRequest(@RequestBody Request request, @PathVariable UUID carId, @PathVariable UUID id) {
        Request req = requestService.getRequest(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request with id " + id + "not found"));
        if (request.getPickup_datetime() != null) {
            req.setPickup_datetime(request.getPickup_datetime());
        }
        if (request.getDropoff_datetime() != null) {
            req.setDropoff_datetime(request.getDropoff_datetime());
        }
        if (request.getStatus() != null) {
            req.setStatus(request.getStatus());
            if (request.getStatus().equals(RequestStatus.ACCEPTED)) { // ei saa seda kaasa anda, sellel on ainult üks value olemas
                createRental(req);
            }
        }
        requestService.updateRequest(req);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/cars/requests/{id}")
    public void deleteRequest(@PathVariable UUID id) {
        requestService.deleteRequest(id);
    }

    public void createRental(Request request) {
        Rental rental = new Rental(request.getId(),
                request.getPickup_datetime(),
                request.getDropoff_datetime(),
                request.getPickup_location(),
                request.getDropoff_location(),
                RentalStatus.UPCOMING,
                request.getCar()
        );
        rentalService.addRental(rental);
    }
}
