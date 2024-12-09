package edu.ftn.iss.eventplanner.entities;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /*
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
     */

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
}
