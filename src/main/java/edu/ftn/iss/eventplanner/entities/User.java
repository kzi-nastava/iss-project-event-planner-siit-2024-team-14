package edu.ftn.iss.eventplanner.entities;
import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Inheritance in Database
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("USER")  // Add this to set the discriminator value for the base class

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;
    private String password;
    private String address;
    private String city;
    private boolean isActive;   // for deactivation
    private int phoneNumber;
    private boolean isVerified;     // for account activation
    private boolean isSuspended;
    @Column(nullable = false)
    private boolean muted;  //for notifications

    @OneToMany(mappedBy = "blocker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlockedUser> blockedUsers;

    @ManyToMany
    @JoinTable(
            name = "user_favorite_solutions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "solution_id")
    )
    private List<Solution> favoriteSolutions;

    @ManyToMany
    @JoinTable(
            name = "user_favorite_events",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> favouriteEvents;

    @ManyToMany
    @JoinTable(
            name = "user_joined_events",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> joinedEvents;

    // Add the activationToken field
    private String activationToken;
    private LocalDateTime tokenCreationDate;

    // Optionally, a method to generate a token
    public void generateActivationToken() {
        this.activationToken = UUID.randomUUID().toString();
    }
}