package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.Event;
import edu.ftn.iss.eventplanner.entities.EventOrganizer;
import edu.ftn.iss.eventplanner.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventOrganizerRepository extends JpaRepository<EventOrganizer, Integer> {
    Optional<EventOrganizer> findByEmail(String email);
    EventOrganizer findByActivationToken(String token);

}
