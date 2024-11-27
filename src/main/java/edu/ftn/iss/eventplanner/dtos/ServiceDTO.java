package edu.ftn.iss.eventplanner.dtos;

import edu.ftn.iss.eventplanner.enums.OfferingVisibility;
import edu.ftn.iss.eventplanner.enums.ReservationType;

import lombok.Data;
import java.net.URL;
import java.time.Duration;

@Data
public class ServiceDTO {
    private long id;
    private /* URL */ String provider;

    private String name;
    private String description;
    private String specificities;

    private double price;
    private double discount;

    private /* URL[] */ String[] images;

    // --- Reservation info (could be grouped into ReservationProperties) // this comment should be in the model, not here :)
    private boolean available;
    private ReservationType reservationPolicy;
    private Duration sessionDuration;
    private Duration reservationPeriod;
    private Duration cancellationPeriod;
    // ---

    private /* URL */ String category;
    private /* URL[] */ String[] applicableEventTypes;

    private OfferingVisibility visibility;
}
