package esi.backend.controller;

import esi.backend.model.Car;
import esi.backend.model.Request;
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

    public RequestController (RequestService requestService){this.requestService=requestService;}

    @RequestMapping("cars/{carId}/requests")
    public List<Request> getAllRequests(@PathVariable UUID carId){return  requestService.getAllRequests(carId);}

    @RequestMapping("cars/requests/{id}")
    public Optional<Request> getRequest (@PathVariable UUID id){return requestService.getRequest(id);}

    @RequestMapping(method = RequestMethod.POST, value = "/cars/{carId}/requests")
    public void addRequest(@RequestBody Request request, @PathVariable UUID carId){
        request.setCar(new Car());
        requestService.addRequest(request);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/cars/{carId}/requests/{id}")
    public void updateRequest(@RequestBody Request request, @PathVariable UUID carId, @PathVariable UUID id){
        request.setCar(new Car());
        requestService.updateRequest(request);
    }

    @RequestMapping(method = RequestMethod.DELETE, value="/cars/requests/{id}")
    public void deleteRequest(@PathVariable UUID id){requestService.deleteRequest(id);}
}
