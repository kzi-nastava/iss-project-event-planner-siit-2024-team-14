package edu.ftn.iss.eventplanner.model;

import lombok.*;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EventOrganizer extends User {

    private String firstName;
    private String lastName;
}
