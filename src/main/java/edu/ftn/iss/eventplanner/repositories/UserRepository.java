package edu.ftn.iss.eventplanner.repositories;

import java.util.Optional;

import edu.ftn.iss.eventplanner.entities.Admin;
import edu.ftn.iss.eventplanner.entities.Event;
import edu.ftn.iss.eventplanner.entities.User;
import edu.ftn.iss.eventplanner.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    User findByActivationToken(String token);


}
