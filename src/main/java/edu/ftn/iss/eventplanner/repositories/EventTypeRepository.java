package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Long> {
    // Ovde možeš dodati dodatne metode po potrebi
    Optional<EventType> findByName(String name);
}
