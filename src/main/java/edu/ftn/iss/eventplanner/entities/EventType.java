package edu.ftn.iss.eventplanner.entities;

import lombok.*;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EventType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;
    private boolean isActive;

    // added because of Solution
    @ManyToOne
    @JoinColumn(name = "solution_category_id")
    private SolutionCategory solutionCategory;
}

