package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.Event;
import edu.ftn.iss.eventplanner.entities.Notification;
import edu.ftn.iss.eventplanner.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByUserIdOrderByDateDesc(Integer userId);

    List<Notification> findByUserId(Integer userId);

    boolean existsByUserAndEventAndMessageContaining(User user, Event event, String message);
}
