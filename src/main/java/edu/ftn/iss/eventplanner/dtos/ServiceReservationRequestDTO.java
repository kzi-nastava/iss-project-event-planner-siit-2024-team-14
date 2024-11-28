package edu.ftn.iss.eventplanner.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceReservationRequestDTO {

    @NotNull(message = "Service ID cannot be null")
    private Long serviceId;

    @NotNull(message = "Event ID cannot be null")
    private Long eventId;

    @NotNull(message = "Reservation date cannot be null")
    @Future(message = "Reservation date must be in the future")
    private LocalDate reservationDate;

    @NotNull(message = "Start time cannot be null")
    private LocalTime startTime;

    private LocalTime endTime; // Optional, calculated on the server if null
}
