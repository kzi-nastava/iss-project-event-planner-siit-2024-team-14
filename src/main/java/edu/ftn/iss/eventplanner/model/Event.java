package edu.ftn.iss.eventplanner.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private int maxParticipants;
    private String privacyType;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private double budget;

    @ManyToOne
    @JoinColumn(name = "event_type_id")
    private EventType eventType;
}
