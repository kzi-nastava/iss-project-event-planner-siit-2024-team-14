package edu.ftn.iss.eventplanner.entities;

import lombok.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "event_type_category",  // Join table name
            joinColumns = @JoinColumn(name = "event_type_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<SolutionCategory> solutionCategories = new ArrayList<>();
}
