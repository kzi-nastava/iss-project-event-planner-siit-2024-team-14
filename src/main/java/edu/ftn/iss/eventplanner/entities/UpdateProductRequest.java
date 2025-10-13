package edu.ftn.iss.eventplanner.entities;

import edu.ftn.iss.eventplanner.enums.OfferingVisibility;
import lombok.Data;

@Data
public class UpdateProductRequest {
    private Integer id;
    private String name;
    private String description;
    private String specificities;
    private Double price;
    private Double discount;
    private String imageUrl;        // ← add this
    private Boolean available;
    private Boolean visible;        // ← add this
    private Integer categoryId;     // ← add this
    private String categoryName;
    private String categoryDescription;
}
