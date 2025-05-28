package edu.ftn.iss.eventplanner.entities;

import edu.ftn.iss.eventplanner.enums.PrivacyType;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private EventOrganizer organizer;
    private String name;
    private String description;
    private int maxParticipants;
    private PrivacyType privacyType;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "event_type_id")
    private EventType eventType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "event_category",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<SolutionCategory> selectedCategories = new ArrayList<>();

}