package edu.ftn.iss.eventplanner.entities;

import edu.ftn.iss.eventplanner.enums.Status;
import lombok.*;
import jakarta.persistence.*;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BookingService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate bookingDate;
    private Status confirmed;
    private Time startTime;
    private Duration duration;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;


    @PostPersist
    @PostUpdate
    protected void addBudgetItemIfNecessary() {
        if (confirmed != Status.APPROVED)
            return;
        if (service == null || service.getCategory() == null || event == null || event.getBudget() == null)
            return;

        var category = service.getCategory();
        var budget = Optional.ofNullable(event.getBudget())
                .orElseGet(() -> {
                    event.setBudget(new Budget());
                    return event.getBudget();
                });

        if (budget.getItem(category).isEmpty())
            budget.addItem(category, 0d);
    }
}

