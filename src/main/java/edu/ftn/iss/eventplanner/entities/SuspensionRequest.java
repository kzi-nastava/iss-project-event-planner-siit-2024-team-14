package edu.ftn.iss.eventplanner.entities;

import edu.ftn.iss.eventplanner.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SuspensionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime timestamp;
    private Status status;

    // who sent request
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User sender;

    // who is suspended
    @ManyToOne
    @JoinColumn(name = "suspended_user_id")
    private User suspendedUser;

    private String reason;
}
