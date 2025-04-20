package id.lariss.store.repository;

import id.lariss.store.domain.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByPhoneNumber(String phoneNumber);

    Optional<Customer> findByPhoneNumberOrEmailAddress(String phoneNumber, String emailAddress);
}
