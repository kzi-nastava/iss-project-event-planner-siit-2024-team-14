package edu.ftn.iss.eventplanner.entities;

import edu.ftn.iss.eventplanner.enums.OfferingVisibility;
import edu.ftn.iss.eventplanner.enums.ReservationType;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateServiceRequest {
    private int providerId;

    private String name;
    private String description = "";
    private String specificities = "";

    private double price;
    private double discount = 0;

    private List<String> images = new ArrayList<>();

    private List<Integer> applicableEventTypeIds = new ArrayList<>();

    private Integer categoryId;
    private String categoryName;
    private String categoryDescription;

    private OfferingVisibility visibility = OfferingVisibility.PUBLIC;

    // Reservation properties
    private boolean available = true;
    private ReservationType reservationType = ReservationType.MANUAL;

    private Duration sessionDuration;
    private Duration minDuration;
    private Duration maxDuration;

    private Duration reservationPeriod;
    private Duration cancellationPeriod;



    public boolean requestsNewCategory() {
        return categoryId == null;
    }
}
