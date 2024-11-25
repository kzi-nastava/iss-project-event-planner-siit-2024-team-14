package edu.ftn.iss.eventplanner.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User sender; // Who sent a message

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat; // In whose chat is this message sent to
}
