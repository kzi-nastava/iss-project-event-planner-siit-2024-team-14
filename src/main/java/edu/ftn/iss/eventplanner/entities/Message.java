package edu.ftn.iss.eventplanner.entities;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;
    @CreationTimestamp
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;            // who sent a message

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;              // in which chat is this message sent to



    public Message(Chat chat, User sender) { this(chat, sender, ""); }

    public Message(Chat chat, User sender, String content) {
        if (!Objects.requireNonNull(chat).isParticipant(sender)) {
            throw new IllegalArgumentException("Sender is not in chat");
        }

        this.sender = Objects.requireNonNull(sender);
        this.chat = Objects.requireNonNull(chat);
        this.content = content;
    }

}
