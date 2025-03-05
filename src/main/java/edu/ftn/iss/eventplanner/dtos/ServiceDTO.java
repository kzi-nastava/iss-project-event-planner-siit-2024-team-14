package edu.ftn.iss.eventplanner.dtos;

import edu.ftn.iss.eventplanner.enums.OfferingVisibility;
import edu.ftn.iss.eventplanner.enums.ReservationType;

import lombok.Data;
import java.net.URL;
import java.time.Duration;

@Data
public class ServiceDTO {
    private long id;
    private GetProviderDTO provider;

    private String name;
    private String description;
    private String specificities;

    private double price;
    private double discount;

    private /* URL[] */ String[] images;

    private CategoryDTO category;
    private EventTypeDTO[] applicableEventTypes;

    // --- Reservation info (could be grouped into ReservationProperties) // this comment should be in the model, not here :)
    private boolean available;
    private OfferingVisibility visibility;
    private ReservationType reservationType;

    private Duration duration;
    private Duration minDuration;
    private Duration maxDuration;
    private Duration reservationPeriod;
    private Duration cancellationPeriod;

    public Long getDurationMinutes() { return duration == null ? null : duration.toMinutes(); }
    public Long getMinDurationMinutes() { return minDuration == null ? null : minDuration.toMinutes(); }
    public Long getMaxDurationMinutes() { return maxDuration == null ? null : maxDuration.toMinutes(); }
    public Long getReservationPeriodDays() { return reservationPeriod == null ? null : reservationPeriod.toDays(); }
    public Long getCancellationPeriodDays() { return cancellationPeriod == null ? null : cancellationPeriod.toDays(); }
    public boolean isVisible() { return visibility == null || visibility == OfferingVisibility.PUBLIC; }
    public boolean isIsVisible() { return visibility == null || visibility == OfferingVisibility.PUBLIC; }
    public Integer getProviderId() { return provider == null ? null : provider.getId(); }
    public boolean isIsAvailable() { return available; }

    // TODO: Model and DTO field names should match in order for clients to know sort parameters
}
