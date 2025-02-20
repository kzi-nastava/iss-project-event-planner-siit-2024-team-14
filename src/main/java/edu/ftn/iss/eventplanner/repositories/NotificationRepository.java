package edu.ftn.iss.eventplanner.repositories;

import edu.ftn.iss.eventplanner.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByUserIdOrderByDateDesc(Integer userId);  // Zadržite samo jedan metod za pretragu

    List<Notification> findByUserId(Integer userId);
}
