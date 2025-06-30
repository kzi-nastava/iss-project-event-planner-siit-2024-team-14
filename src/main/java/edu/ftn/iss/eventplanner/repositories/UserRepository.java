package edu.ftn.iss.eventplanner.repositories;

import java.util.Optional;

import edu.ftn.iss.eventplanner.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    User findByActivationToken(String token);
    boolean existsByEmail(String trim);
}
