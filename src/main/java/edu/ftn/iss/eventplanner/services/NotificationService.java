package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.notifications.NotificationDTO;
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
import org.springframework.transaction.annotation.Transactional;
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

    /**
     * Creates a notification for a user, optionally linked to a comment or event,
     * stores it in the database, and sends it via WebSocket.
     */
    public NotificationDTO createNotification(Integer userId, String message, Integer commentId, Integer eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Find optional comment and event
        Comment comment = (commentId != null) ? commentRepository.findById(commentId).orElse(null) : null;
        Event event = (eventId != null) ? eventRepository.findById(eventId).orElse(null) : null;

        // Create and save notification
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setDate(LocalDate.now());
        notification.setRead(false);
        notification.setComment(comment);
        notification.setEvent(event);

        Notification savedNotification = notificationRepository.save(notification);

        // Send WebSocket message to the user
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

        // Return DTO
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

    /**
     * Retrieves all notifications for a given user, ordered by date descending.
     */
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

    /**
     * Marks all notifications for a user as read.
     */
    public void markAllAsRead(Integer userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        for (Notification notification : notifications) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(notifications);
    }

    /**
     * Toggles the user's mute setting for notifications.
     * This method is transactional to ensure consistency.
     */
    @Transactional
    public void toggleMuteNotifications(Integer userId, boolean isMuted) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setMuted(isMuted);
        userRepository.save(user);
    }

    /**
     * Returns whether the user has muted notifications.
     */
    public boolean getMuteNotificationsStatus(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.isMuted();
    }

    /**
     * Returns the number of unread notifications for a user.
     */
    public Integer getUnreadNotificationCount(Integer userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        return (int) notifications.stream().filter(n -> !n.isRead()).count();
    }
}
