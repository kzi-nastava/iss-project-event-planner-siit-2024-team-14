package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.NotificationDTO;
import edu.ftn.iss.eventplanner.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{userId}")
    public List<NotificationDTO> getNotifications(@PathVariable Integer userId) {
        return notificationService.getNotificationsForUser(userId);
    }

    @PostMapping("/markAsRead/{notificationId}")
    public void markAsRead(@PathVariable Integer notificationId) {
        notificationService.markNotificationAsRead(notificationId);
    }


}
