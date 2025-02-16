package edu.ftn.iss.eventplanner.dtos;

import edu.ftn.iss.eventplanner.entities.User;
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
    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }
    private Integer userId;
    private Integer commentId;
    private Integer eventId;
}