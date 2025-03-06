package edu.ftn.iss.eventplanner.dtos;

import edu.ftn.iss.eventplanner.enums.OfferingVisibility;
import edu.ftn.iss.eventplanner.enums.ReservationType;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.time.Duration;
import java.util.List;

@Data
public class CreateServiceDTO {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    private String description;
    private String specificities;

    @PositiveOrZero(message = "Price cannot be negative")
    private double price;
    @Min(value = 0, message = "Discount cannot be lower than 0")
    @Max(value = 1, message = "Discount cannot exceed 1")
    private Double discount;

    private List<String> images;

    @NotEmpty(message = "The service must be appropriate for at least a single event type")
    private List<Integer> applicableEventTypeIds;
    @NotNull(message = "Category must be specified")
    @Valid
    private CategoryOrCategoryRequestDTO category;

    private OfferingVisibility visibility;
    private Boolean available;

    private ReservationType reservationType;

    @Positive(message = "Session duration must be positive")
    private Long durationMinutes;
    @PositiveOrZero(message = "Min duration cannot be negative")
    private Long minDurationMinutes;
    @Positive(message = "Max duration must be positive")
    private Long maxDurationMinutes;

    @Positive(message = "Reservation period must be positive")
    private Long reservationPeriodDays;
    @PositiveOrZero(message = "Cancellation period cannot be negative")
    private Long cancellationPeriodDays;



    @AssertTrue(message = "Cannot specify min and max duration if session duration is specified")
    boolean areDurationsValid() {
        return durationMinutes == null || (minDurationMinutes == null && maxDurationMinutes == null);
    }

    @Data
    public static class CategoryOrCategoryRequestDTO {
        private Integer id;
        private String name;
        private String description;

        @AssertTrue(message = "Either category id must be specified or a name for requesting a new category")
        boolean isValid() {
            return id != null || name != null && !name.isBlank();
        }
    }
}