package edu.ftn.iss.eventplanner.dtos;

import edu.ftn.iss.eventplanner.enums.OfferingVisibility;
import edu.ftn.iss.eventplanner.enums.ReservationType;

import lombok.Data;

import java.time.Duration;

// TODO: Add validation

@Data
public class UpdateServiceDTO {
    // TODO: Check if the provider may be changed
    private String name;
    private String description;
    private String specificities;

    private Double price;
    private Double discount;

    private Boolean available;
    private ReservationType reservationPolicy;
    private Duration sessionDuration;
    private Duration reservationPeriod;
    private Duration cancellationPeriod;

    private OfferingVisibility visibility;
}
