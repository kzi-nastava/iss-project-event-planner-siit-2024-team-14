package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
