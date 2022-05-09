package esi.backend.repository;

import esi.backend.model.Customer;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface CustomerRepository extends UserBaseRepository<Customer> {
    Optional<Customer> findById(Long id);

    Optional<Customer> findCustomerByUsername(String username);
}
