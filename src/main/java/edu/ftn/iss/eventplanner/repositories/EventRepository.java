package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findFirst5ByLocationOrderByStartDateDesc(String location);
}
