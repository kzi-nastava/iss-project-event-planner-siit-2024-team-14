package edu.ftn.iss.eventplanner.model;

import lombok.*;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String guestEmail;
    private String status;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;
}
