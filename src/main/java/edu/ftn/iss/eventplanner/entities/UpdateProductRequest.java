package edu.ftn.iss.eventplanner.entities;

import edu.ftn.iss.eventplanner.enums.OfferingVisibility;
import lombok.Data;

import java.util.List;

@Data
public class UpdateProductRequest {
    private Integer id;
    private String name;
    private String description;
    private String specificities;
    private Double price;
    private Double discount;
    private String imageUrl;
    private List<Integer> applicableEventTypeIds;
    private Boolean available;
    private Boolean visible;
    private Integer categoryId;
    private String categoryName;
    private String categoryDescription;
}
