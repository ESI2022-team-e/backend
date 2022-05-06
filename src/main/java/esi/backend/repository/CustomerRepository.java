package esi.backend.repository;

import esi.backend.model.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Optional<Customer> findById(Long id);

    Optional<Customer> findByUsername(String username);
}
