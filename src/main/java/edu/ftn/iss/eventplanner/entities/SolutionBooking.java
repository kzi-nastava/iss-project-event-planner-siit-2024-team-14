package edu.ftn.iss.eventplanner.entities;

import edu.ftn.iss.eventplanner.enums.Status;
import lombok.*;
import jakarta.persistence.*;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolutionBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "solution_id")
    private Solution solution;

    private LocalDate bookingDate;
    private Status confirmed;
    private Time startTime;
    private Duration duration;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
