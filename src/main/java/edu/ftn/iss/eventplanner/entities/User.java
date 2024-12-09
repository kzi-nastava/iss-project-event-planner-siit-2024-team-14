package edu.ftn.iss.eventplanner.entities;

import edu.ftn.iss.eventplanner.enums.Role;
import lombok.*;
import jakarta.persistence.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Inheritance in Database
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Role role;
    private String email;
    private String password;
    private String address;
    private int phoneNumber;
    private boolean isVerified;
    private boolean isSuspended;

    /*
    @ManyToMany
    @JoinTable(
            name = "user_blocked_users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "blocked_user_id")
    )
    private List<User> blockedUsers;

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
     */
}
