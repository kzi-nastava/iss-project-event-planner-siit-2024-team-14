package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.SolutionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SolutionCategoryRepository extends JpaRepository<SolutionCategory, Integer> {
    Optional<SolutionCategory> findByName(String name);
    List<SolutionCategory> findByNameIn(List<String> names);
}
