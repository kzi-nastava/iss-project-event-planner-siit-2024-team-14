package edu.ftn.iss.eventplanner.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceReservationResponse {

    private Long reservationId;
    private Long serviceId;
    private Long eventId;
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status; // Example: "Confirmed"
}
