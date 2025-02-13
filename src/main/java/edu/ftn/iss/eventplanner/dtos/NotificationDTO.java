package edu.ftn.iss.eventplanner.dtos;

import edu.ftn.iss.eventplanner.entities.User;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NotificationDTO {
    private Integer id;
    private String message;
    private LocalDate date;
    private boolean isRead;
    private User user;

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

}
