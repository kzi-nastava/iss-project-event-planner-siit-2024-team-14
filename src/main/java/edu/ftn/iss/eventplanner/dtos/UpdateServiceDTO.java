package edu.ftn.iss.eventplanner.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.ftn.iss.eventplanner.enums.OfferingVisibility;
import edu.ftn.iss.eventplanner.enums.ReservationType;

import lombok.Data;

import java.util.List;


@Data
public class UpdateServiceDTO {
    @JsonIgnore
    private int id;

    private String name;
    private String description;
    private String specificities;

    private Double price;
    private Double discount;

    private List<String> images;
    private List<Integer> applicableEventTypeIds;

    private OfferingVisibility visibility;
    private Boolean available;

    private ReservationType reservationType;

    private Long durationMinutes;
    private Long minDurationMinutes;
    private Long maxDurationMinutes;

    private Long reservationPeriodDays;
    private Long cancellationPeriodDays;
}
