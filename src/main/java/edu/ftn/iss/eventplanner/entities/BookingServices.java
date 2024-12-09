package edu.ftn.iss.eventplanner.entities;

import lombok.*;
import jakarta.persistence.*;

import java.sql.Time;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BookingServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate bookingDate;
    private boolean isConfirmed;
    private Time startTime;
    private Time endTime;

    /*
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
     */
}

