package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findFirst5ByLocationOrderByStartDateDesc(String location);

    Optional<Event> findByName(String name);

    List<Event> findByLocation(String location);

    @Query("SELECT DISTINCT e.location FROM Event e")
    List<String> findAllLocations();

    @Query("SELECT DISTINCT e.eventType.name FROM Event e")
    List<String> findAllCategories();

    List<Event> findByOrganizerId(Integer organizerId);

    @Query("SELECT e FROM Event e WHERE e.organizer.id = :organizerId")
    List<Event> findAllByOrganizerId(@Param("organizerId") Integer organizerId);

}
