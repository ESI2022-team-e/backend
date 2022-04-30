package esi.backend.repository;

import esi.backend.model.Car;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CarRepository extends CrudRepository<Car, UUID> {

}
