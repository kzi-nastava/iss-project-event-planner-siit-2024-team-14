package edu.ftn.iss.eventplanner.entities;

import lombok.*;
import jakarta.persistence.*;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BookingService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate bookingDate;
    private boolean confirmed;
    private Time startTime;
    private Duration duration;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}

