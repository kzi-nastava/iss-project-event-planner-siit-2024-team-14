package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {
}
