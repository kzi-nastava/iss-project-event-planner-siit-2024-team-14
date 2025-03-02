package edu.ftn.iss.eventplanner.dtos.serviceDetails;

import edu.ftn.iss.eventplanner.enums.OfferingVisibility;
import edu.ftn.iss.eventplanner.enums.ReservationType;
import lombok.Data;

import java.time.Duration;

@Data
public class ServiceDTO {
    private long id;

    private String name;
    private String description;
    private String specificities;  // Specifičnosti servisa

    private double price;
    private double discount;

    private String[] images;  // Polje slika servisa kao niz URL-ova (stringova)

    // --- Reservation info (grouped into ReservationProperties)
    private boolean isAvailable;  // Da li je servis dostupan
    private ReservationType reservationType;  // Rezervaciona politika (manualna ili automatska)
    private Duration duration;  // Trajanje sesije servisa
    private Duration reservationPeriod;  // Period za rezervaciju
    private Duration cancellationPeriod;  // Period za otkazivanje rezervacije
    private Duration minDuration;
    private Duration maxDuration;
    // ---

    private CategoryDTO category;  // Kategorija servisa kao DTO sa svim potrebnim informacijama
    private EventTypeDTO[] applicableEventTypes;  // Tipovi događaja koji se mogu koristiti za servis

    private OfferingVisibility visibility;  // Vidljivost servisa (public, private, pending)

    private ProviderDTO provider;
    private Integer providerId;
}
