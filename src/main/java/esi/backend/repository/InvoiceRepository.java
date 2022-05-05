package esi.backend.repository;

import esi.backend.model.Invoice;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository extends CrudRepository<Invoice, UUID> {

    Optional<Invoice> findByRentalId(UUID rentalId);


    // TODO: findByCustomerId
    /*
    List<Invoice> findByCustomerId(UUID customerId);
     */

}
