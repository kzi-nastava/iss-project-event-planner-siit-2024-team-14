package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.Category;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(@Nonnull String name);
}
