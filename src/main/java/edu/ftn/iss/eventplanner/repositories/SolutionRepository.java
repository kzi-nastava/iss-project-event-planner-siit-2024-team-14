package edu.ftn.iss.eventplanner.repositories;
import edu.ftn.iss.eventplanner.entities.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {
    // Pronaći prva 5 događaja po lokaciji
    List<Solution> findFirst5ByLocation(String location);

    // Pronaći događaj po imenu
    Optional<Solution> findByName(String name);

    // Pronaći događaje po lokaciji
    List<Solution> findByLocation(String location);
}
