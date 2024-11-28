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
    private Long id;

    private String category;
    private double amount;
    private boolean purchased;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;
}
