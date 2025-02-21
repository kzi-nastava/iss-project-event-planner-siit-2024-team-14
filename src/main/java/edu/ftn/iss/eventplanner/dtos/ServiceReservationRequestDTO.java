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

    private Integer serviceId;
    private Integer eventId;
    private LocalDate date;
    private String startTime;
    private String endTime;
}
