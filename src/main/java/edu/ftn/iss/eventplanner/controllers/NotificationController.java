package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.NotificationDTO;
import edu.ftn.iss.eventplanner.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(
            @RequestParam Integer userId,
            @RequestParam String message,
            @RequestParam(required = false) Integer commentId,
            @RequestParam(required = false) Integer eventId) {

        NotificationDTO notificationDTO = notificationService.createNotification(userId, message, commentId, eventId);
        return ResponseEntity.ok(notificationDTO);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(@PathVariable Integer userId) {
        List<NotificationDTO> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/markAsRead/{notificationId}")
    public void markAsRead(@PathVariable Integer notificationId) {
        notificationService.markNotificationAsRead(notificationId);
    }


}