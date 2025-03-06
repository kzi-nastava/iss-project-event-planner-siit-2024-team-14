package edu.ftn.iss.eventplanner.entities;

import edu.ftn.iss.eventplanner.enums.OfferingVisibility;
import edu.ftn.iss.eventplanner.enums.ReservationType;
import lombok.*;

import java.time.Duration;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateServiceRequest {
    private int id;

    private String name;
    private String description;
    private String specificities;

    private Double price;
    private Double discount;

    private List<String> images;
    private List<Integer> applicableEventTypeIds;

    private OfferingVisibility visibility; // probably shouldn't allow to change from PENDING
    private Boolean available;

    private ReservationType reservationType;

    private Duration duration;
    private Duration minDuration;
    private Duration maxDuration;

    private Duration reservationPeriod;
    private Duration cancellationPeriod;


    public static UpdateServiceRequestBuilder builder(int id) {
        return new UpdateServiceRequestBuilder().id(id);
    }
}