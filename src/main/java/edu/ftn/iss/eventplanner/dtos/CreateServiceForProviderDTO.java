package edu.ftn.iss.eventplanner.dtos;

import edu.ftn.iss.eventplanner.enums.OfferingVisibility;
import edu.ftn.iss.eventplanner.enums.ReservationType;
import lombok.Data;

import java.net.URL;
import java.time.Duration;

// TODO: Add validation

@Data
public class CreateServiceForProviderDTO {
    private long providerId;

    private String name;
    private String description;
    private String specificities;

    private double price;
    private double discount;

    private /* URL[] */ String[] images;  // maby via separate upload

    private Boolean available;
    private ReservationType reservationPolicy;
    private Duration sessionDuration;
    private Duration reservationPeriod;
    private Duration cancellationPeriod;

    private long category;
    private long[] applicableEventTypes;

    private OfferingVisibility visibility;
}
