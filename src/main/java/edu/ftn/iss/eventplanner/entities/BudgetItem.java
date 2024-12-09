package edu.ftn.iss.eventplanner.entities;

import lombok.*;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BudgetItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private SolutionCategory category;

    private double amount;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
