package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.ServiceAndProductProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceAndProductProviderRepository extends JpaRepository<ServiceAndProductProvider, Integer> {
    Optional<ServiceAndProductProvider> findByEmail(String email);
    ServiceAndProductProvider findByActivationToken(String token);
    ServiceAndProductProvider findById(int id);

    default Optional<ServiceAndProductProvider> findByIdAsOptional(int id) {
        return Optional.ofNullable(findById(id));
    }
}
