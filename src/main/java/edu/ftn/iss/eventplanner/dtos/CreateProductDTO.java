package edu.ftn.iss.eventplanner.dtos;

import edu.ftn.iss.eventplanner.enums.OfferingVisibility;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class CreateProductDTO {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    private String description;
    private String specificities;

    @PositiveOrZero(message = "Price cannot be negative")
    private Double price;

    @Min(value = 0, message = "Discount cannot be lower than 0")
    @Max(value = 1, message = "Discount cannot exceed 1")
    private Double discount;

    private List<String> images;

    @NotNull(message = "Category must be specified")
    @Valid
    private CategoryOrCategoryRequestDTO category;

    private OfferingVisibility visibility;

    private Boolean visible; // frontend toggle for public/private
    private Boolean available; // frontend toggle for availability

    @NotNull(message = "Provider ID must be provided")
    private Integer providerId;

    // ✅ Helper for services that need category ID directly
    public Integer getCategoryId() {
        return category != null ? category.getId() : null;
    }

    // ✅ Inner class to support existing or new category
    @Data
    public static class CategoryOrCategoryRequestDTO {
        private Integer id; // existing category ID
        private String name; // name for new category
        private String description; // description for new category

        @AssertTrue(message = "Either category id must be specified or a name for requesting a new category")
        boolean isValid() {
            return id != null || (name != null && !name.isBlank());
        }
    }
}
