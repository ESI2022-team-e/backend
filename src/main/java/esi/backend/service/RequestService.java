package esi.backend.service;

import esi.backend.exception.ResourceNotFoundException;
import esi.backend.model.*;
import esi.backend.repository.RentalRepository;
import esi.backend.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private RentalRepository rentalRepository;

    public List<Request> getAllRequests() {
        List<Request> requests = new ArrayList<>();
        requestRepository.findAll().forEach(requests::add);
        return requests;
    }

    public List<Request> getAllRequestsByCarId(UUID carId) {
        return requestRepository.findByCarId(carId);
    }

    public Optional<Request> getRequest(UUID id) {
        return requestRepository.findById(id);
    }

    public void addRequest(Request request) {
        requestRepository.save(request);
    }

    public void updateRequest(UserDetails user, Request request, UUID id) {
        Request req = requestRepository.findById(id)
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
            if (request.getStatus().equals(RequestStatus.ACCEPTED) && user.getAuthorities().contains(ERole.ROLE_MANAGER)) {
                req.setStatus(request.getStatus());
                createRental(req);
            }
        }
        requestRepository.save(req);
    }

    public void deleteRequest(UUID id) {
        requestRepository.deleteById(id);
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
        rentalRepository.save(rental);
    }
}
