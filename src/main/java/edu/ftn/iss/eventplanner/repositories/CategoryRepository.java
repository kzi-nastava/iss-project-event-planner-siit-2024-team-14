package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.EventType;
import edu.ftn.iss.eventplanner.entities.SolutionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<SolutionCategory, Integer> {
    Optional<SolutionCategory> findByName(String name);

    @Query("SELECT et FROM EventType et LEFT JOIN FETCH et.solutionCategories WHERE et.name = :eventTypeName")
    EventType findByNameWithCategories(@Param("eventTypeName") String eventTypeName);
}
