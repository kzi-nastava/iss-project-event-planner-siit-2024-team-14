package edu.ftn.iss.eventplanner.entities;

import lombok.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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
    private List<Message> messages = new ArrayList<>();     // list of messages in the chat
}

