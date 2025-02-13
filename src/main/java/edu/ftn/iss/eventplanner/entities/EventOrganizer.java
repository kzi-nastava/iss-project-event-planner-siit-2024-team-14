package edu.ftn.iss.eventplanner.entities;

import lombok.*;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("ORGANIZER") // Maps to role = "ORGANIZER"
public class EventOrganizer extends User {

    private String profilePicture;
    private String firstName;
    private String lastName;
}