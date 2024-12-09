package edu.ftn.iss.eventplanner.entities;

import edu.ftn.iss.eventplanner.enums.Status;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RegistrationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private Status status;    // PENDING, ACCEPTED, REJECTED

    /*
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;                  // who sent request
     */
}

