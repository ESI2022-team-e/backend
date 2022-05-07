package esi.backend.repository;

import esi.backend.model.Request;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RequestRepository extends CrudRepository<Request, UUID> {
    List<Request> findByCarId(UUID carId);

    List<Request> findAll();
}
