package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Integer> {
    Optional<EventType> findByName(String name);

    EventType findById(int id);

    @Query("SELECT e FROM EventType e LEFT JOIN FETCH e.solutionCategories")
    List<EventType> findAllWithCategories();
}