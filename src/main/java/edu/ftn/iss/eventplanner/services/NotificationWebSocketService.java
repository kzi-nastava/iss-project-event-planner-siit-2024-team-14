package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.NotificationDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationWebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendNotificationToUser(Integer userId, String message) {
        String destination = "/topic/notifications/" + userId;
        messagingTemplate.convertAndSend(destination, message);
    }

    public void sendWebSocketNotification(Integer userId, NotificationDTO notification) {
        String destination = "/topic/notifications/" + userId;
        messagingTemplate.convertAndSend(destination, notification);
    }
}