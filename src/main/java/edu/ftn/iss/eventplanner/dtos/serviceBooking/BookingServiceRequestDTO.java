package edu.ftn.iss.eventplanner.dtos.serviceBooking;

import lombok.Data;
import java.time.LocalDate;


@Data
public class BookingServiceRequestDTO {
    private Integer serviceId;
    private Integer eventId;
    private LocalDate bookingDate;
    private String startTime;
    private Integer duration;
}
