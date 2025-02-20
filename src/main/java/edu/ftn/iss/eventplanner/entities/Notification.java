package edu.ftn.iss.eventplanner.entities;

import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String message;
    private LocalDate date;
    private boolean isRead;

    // to whom is sent?
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // one of these will be null
    @ManyToOne
    @JoinColumn(name = "comment_id")    // notification about new comment
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "event_id")      // notification about event changes
    private Event event;

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

}

