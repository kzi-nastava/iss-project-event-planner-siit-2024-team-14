package edu.ftn.iss.eventplanner.entities;

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
    private Integer id;

    private String content;
    private LocalDateTime timestamp;

    /*
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;            // who sent a message

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;              // in which chat is this message sent to
     */
}
