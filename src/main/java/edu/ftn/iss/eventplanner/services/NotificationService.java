package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.NotificationDTO;
import edu.ftn.iss.eventplanner.entities.Notification;
import edu.ftn.iss.eventplanner.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public List<NotificationDTO> getNotificationsForUser(Integer userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndIsRead(userId, false);
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setMessage(notification.getMessage());
        dto.setDate(notification.getDate());
        dto.setRead(notification.isRead());
        dto.setUser(notification.getUser());
        return dto;
    }

    public void markNotificationAsRead(Integer notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow();
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
