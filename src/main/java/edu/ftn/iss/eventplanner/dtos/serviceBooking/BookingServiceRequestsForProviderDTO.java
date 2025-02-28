package edu.ftn.iss.eventplanner.dtos.serviceBooking;

import edu.ftn.iss.eventplanner.enums.Status;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class BookingServiceRequestsForProviderDTO {
    private Integer id;
    private Integer serviceId;
    private Integer eventId;
    private LocalDate bookingDate;
    private String startTime;
    private Long duration;
    private Status confirmed;
}
