package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.NotificationDTO;
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

    // Ova metoda će slati notifikaciju na određeni kanal
    @MessageMapping("/sendNotification")
    public void sendNotification(String notification) {
        // Šalje poruku na kanal "/topic/notifications"
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

    // Endpoint za preuzimanje notifikacija
    @GetMapping
    public List<NotificationDTO> getNotifications(@RequestParam("userId") Integer userId) {
        // Poziva servis koji vraća notifikacije za određenog korisnika
        return notificationService.getUserNotifications(userId);
    }

    @PutMapping("/mark-all-as-read")
    public ResponseEntity<Void> markAllAsRead(@RequestParam Integer userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }
}
