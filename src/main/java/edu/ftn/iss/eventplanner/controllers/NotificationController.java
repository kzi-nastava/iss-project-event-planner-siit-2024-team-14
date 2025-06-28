package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.notifications.NotificationDTO;
import edu.ftn.iss.eventplanner.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/sendNotification")
    public void sendNotification(String notification) {
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }

    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(
            @RequestParam Integer userId,
            @RequestParam String message,
            @RequestParam(required = false) Integer commentId,
            @RequestParam(required = false) Integer eventId) {

        NotificationDTO notificationDTO = notificationService.createNotification(userId, message, commentId, eventId);
        return ResponseEntity.ok(notificationDTO);
    }

    @GetMapping
    public List<NotificationDTO> getNotifications(@RequestParam("userId") Integer userId) {
        return notificationService.getUserNotifications(userId);
    }

    @PutMapping("/mark-all-as-read")
    public ResponseEntity<Void> markAllAsRead(@RequestParam Integer userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/mute-notifications")
    public ResponseEntity<Void> toggleMuteNotifications(@RequestParam Integer userId, @RequestParam Boolean muted) {
        notificationService.toggleMuteNotifications(userId, muted);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/mute-notifications/status")
    public ResponseEntity<Boolean> getMuteNotificationsStatus(@RequestParam Integer userId) {
        boolean isMuted = notificationService.getMuteNotificationsStatus(userId);
        return ResponseEntity.ok(isMuted);
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Integer> getUnreadNotificationsCount(@RequestParam Integer userId) {
        Integer unreadCount = notificationService.getUnreadNotificationCount(userId);
        return ResponseEntity.ok(unreadCount);
    }
}
