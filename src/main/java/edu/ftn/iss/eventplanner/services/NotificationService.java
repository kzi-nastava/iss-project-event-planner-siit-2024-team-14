package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.NotificationDTO;
import edu.ftn.iss.eventplanner.entities.Comment;
import edu.ftn.iss.eventplanner.entities.Event;
import edu.ftn.iss.eventplanner.entities.Notification;
import edu.ftn.iss.eventplanner.entities.User;
import edu.ftn.iss.eventplanner.repositories.CommentRepository;
import edu.ftn.iss.eventplanner.repositories.EventRepository;
import edu.ftn.iss.eventplanner.repositories.NotificationRepository;
import edu.ftn.iss.eventplanner.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public NotificationDTO createNotification(Integer userId, String message, Integer commentId, Integer eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Comment comment = (commentId != null) ? commentRepository.findById(commentId).orElse(null) : null;
        Event event = (eventId != null) ? eventRepository.findById(eventId).orElse(null) : null;

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setDate(LocalDate.now());
        notification.setRead(false);
        notification.setComment(comment);
        notification.setEvent(event);

        Notification savedNotification = notificationRepository.save(notification);

        // Slanje WebSocket notifikacije
        String destination = "/topic/notifications/" + userId;
        messagingTemplate.convertAndSend(destination, new NotificationDTO(
                savedNotification.getId(),
                savedNotification.getMessage(),
                savedNotification.getDate(),
                savedNotification.isRead(),
                savedNotification.getUser().getId(),
                savedNotification.getComment() != null ? savedNotification.getComment().getId() : null,
                savedNotification.getEvent() != null ? savedNotification.getEvent().getId() : null
        ));

        return new NotificationDTO(
                savedNotification.getId(),
                savedNotification.getMessage(),
                savedNotification.getDate(),
                savedNotification.isRead(),
                savedNotification.getUser().getId(),
                savedNotification.getComment() != null ? savedNotification.getComment().getId() : null,
                savedNotification.getEvent() != null ? savedNotification.getEvent().getId() : null
        );
    }

    public List<NotificationDTO> getUserNotifications(Integer userId) {
        return notificationRepository.findByUserIdOrderByDateDesc(userId)
                .stream()
                .map(n -> new NotificationDTO(
                        n.getId(),
                        n.getMessage(),
                        n.getDate(),
                        n.isRead(),
                        n.getUser().getId(),
                        n.getComment() != null ? n.getComment().getId() : null,
                        n.getEvent() != null ? n.getEvent().getId() : null
                ))
                .collect(Collectors.toList());
    }

    public void markAllAsRead(Integer userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        for (Notification notification : notifications) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(notifications);
    }



}
