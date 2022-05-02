package esi.backend.controller;

import esi.backend.exception.ResourceNotFoundException;
import esi.backend.model.Car;
import esi.backend.model.Request;
import esi.backend.service.CarService;
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

    public RequestController(RequestService requestService, CarService carService) {
        this.requestService = requestService;
        this.carService = carService;
    }

    // TODO get all requests no matter the car?
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
        request.setCar(optionalCar.get());
        requestService.addRequest(request);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/cars/{carId}/requests/{id}")
    public void updateRequest(@RequestBody Request request, @PathVariable UUID carId, @PathVariable UUID id) {
        Request req = requestService.getRequest(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request with id " + id + "not found"));
        if (request.getStatus() != req.getStatus()) {
            req.setStatus(request.getStatus());
        }
        if (request.getPickup_date() != req.getPickup_date()) {
            req.setPickup_date(request.getPickup_date());
        }
        // TODO what else needs to be added here?
        requestService.updateRequest(req);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/cars/requests/{id}")
    public void deleteRequest(@PathVariable UUID id) {
        requestService.deleteRequest(id);
    }
}
