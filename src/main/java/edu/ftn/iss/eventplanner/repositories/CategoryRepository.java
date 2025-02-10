package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.SolutionCategory;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<SolutionCategory, Integer> {
    Optional<SolutionCategory> findByName(String name);
}
