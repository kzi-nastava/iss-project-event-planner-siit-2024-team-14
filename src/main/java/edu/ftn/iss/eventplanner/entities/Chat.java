package edu.ftn.iss.eventplanner.entities;

import lombok.*;
import jakarta.persistence.*;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;                // who sent a message

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;             // who got a message

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("timestamp DESC")
    private List<Message> messages = new ArrayList<>();     // list of messages in the chat



    public Chat(User sender, User recipient) {
        this.sender = Objects.requireNonNull(sender);
        this.recipient = Objects.requireNonNull(recipient);
    }


    public boolean isParticipant(User user) {
        return Set.of(sender.getId(), recipient.getId())
                .contains(Objects.requireNonNull(user).getId());
    }


    public Message sendMessage(int fromId, String content) {

        if (sender.getId().equals(fromId)) {
            return sendMessage(sender, content);
        } else if (recipient.getId().equals(fromId)) {
            return sendMessage(recipient, content);
        }

        throw new IllegalStateException("User not in chat");
    }


    public Message sendMessage(User from, String content) {
        if (!isParticipant(from))
            throw new IllegalStateException("User not in chat");

        var message = new Message(this, from, content);
        messages.add(message);
        return message;
    }
}
