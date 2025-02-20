package edu.ftn.iss.eventplanner.dtos.notifications;

import lombok.Data;

import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NotificationDTO {
    private Integer id;
    private String message;
    private LocalDate date;
    private boolean isRead;
    private Integer userId;
    private Integer commentId;
    private Integer eventId;
}