package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, SolutionSearchRepositoryMixin<Product> {
    @Query("SELECT p FROM Product p WHERE p.isAvailable") // TODO: need to change isAvailable to available
    List<Product> getByAvailableTrue();
}
