package edu.ftn.iss.eventplanner.entities;

import lombok.*;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("ORGANIZER") // Maps to role = "ORGANIZER"
public class EventOrganizer extends User {

    private String profilePhoto;
    private String name;
    private String surname;

    @ManyToMany
    @JoinTable(
            name = "organizers_events",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> myEvents = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_ORGANIZER"));
    }


    public boolean organizesEvent(Event event) {
        return organizesEvent(event.getId());
    }

    public boolean organizesEvent(int eventId) {
        return myEvents.stream().anyMatch(e -> e.getId().equals(eventId));
    }

}