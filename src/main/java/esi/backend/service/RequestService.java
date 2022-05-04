package esi.backend.service;

import esi.backend.model.Car;
import esi.backend.model.Request;
import esi.backend.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

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

    public void updateRequest(Request request) {
        requestRepository.save(request);
    }

    public void deleteRequest(UUID id) {
        requestRepository.deleteById(id);
    }
}
