package edu.ftn.iss.eventplanner.dtos.serviceBooking;

import lombok.Data;

@Data
public class ApproveRequestDTO {
    private Integer requestId;
    private String approved;
}
