package edu.ftn.iss.eventplanner.dtos;

import edu.ftn.iss.eventplanner.enums.OfferingVisibility;
import edu.ftn.iss.eventplanner.enums.ReservationType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;


@Data
public class UpdateServiceDTO {
    @NotNull
    private Integer id;

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
