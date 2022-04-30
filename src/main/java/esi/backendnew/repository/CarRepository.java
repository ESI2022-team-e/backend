package esi.backendnew.repository;

import esi.backendnew.model.Car;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CarRepository extends CrudRepository<Car, UUID> {

}
