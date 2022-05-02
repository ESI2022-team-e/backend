package esi.backend.service;

import esi.backend.model.Request;
import esi.backend.repository.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RequestService {

    private RequestRepository requestRepository;

    public List<Request> getAllRequests(UUID carId) {
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
