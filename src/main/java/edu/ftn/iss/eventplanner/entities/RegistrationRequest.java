package edu.ftn.iss.eventplanner.entities;

import edu.ftn.iss.eventplanner.enums.InvitationStatus;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RegistrationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate requestDate;
    private boolean isVerified; // Da li je zahtev verifikovan?

    @Enumerated(EnumType.STRING)
    private InvitationStatus status; // Status pozivnice (npr. PENDING, ACCEPTED, REJECTED)

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Ko je poslao zahtev
}

