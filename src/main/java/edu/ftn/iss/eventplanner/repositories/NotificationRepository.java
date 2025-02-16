package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserIdAndIsRead(Integer userId, boolean isRead);

    List<Notification> findByUserIdOrderByDateDesc(Integer userId);
}