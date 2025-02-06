package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.ServiceAndProductProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceAndProductProviderRepository extends JpaRepository<ServiceAndProductProvider, Long> {

    Optional<ServiceAndProductProvider> findByCompanyName(String name);
}
