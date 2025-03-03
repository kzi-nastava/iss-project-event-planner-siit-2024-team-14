package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.BlockedUser;
import edu.ftn.iss.eventplanner.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockedUserRepository extends JpaRepository<BlockedUser, Integer> {

    boolean existsByBlockerAndBlocked(User blocker, User blocked);
}
