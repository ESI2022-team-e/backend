package esi.backend.controller;

import esi.backend.exception.ResourceNotFoundException;
import esi.backend.model.*;
import esi.backend.service.CarService;
import esi.backend.service.RequestService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    public RequestController(RequestService requestService, CarService carService) {
        this.requestService = requestService;
        this.carService = carService;
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
    public void addRequest(@RequestBody Request request, @PathVariable UUID carId) {
        requestService.addRequest(request, carId);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/cars/{carId}/requests/{id}")
    public void updateRequest(@AuthenticationPrincipal final UserDetails currentUser, @RequestBody Request request, @PathVariable UUID id) {
        requestService.updateRequest(currentUser, request, id);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/cars/{carId}/requests/{id}")
    public void deleteRequest(@PathVariable UUID id) {
        requestService.deleteRequest(id);
    }
}
