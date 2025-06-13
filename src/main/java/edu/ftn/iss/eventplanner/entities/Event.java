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

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PurchaseProduct> purchases = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BookingService> reservations = new ArrayList<>();

    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Budget budget = new Budget(this);


    public Event(Integer id, EventOrganizer organizer, String name, String description, int maxParticipants, PrivacyType privacyType, String location, LocalDate startDate, LocalDate endDate, String imageUrl, EventType eventType) {
        this.id = id;
        this.organizer = organizer;
        this.name = name;
        this.description = description;
        this.maxParticipants = maxParticipants;
        this.privacyType = privacyType;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageUrl = imageUrl;
        this.eventType = eventType;
    }

}