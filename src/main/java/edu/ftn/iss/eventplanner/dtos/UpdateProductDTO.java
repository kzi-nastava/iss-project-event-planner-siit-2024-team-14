package edu.ftn.iss.eventplanner.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.ftn.iss.eventplanner.enums.OfferingVisibility;
import lombok.Data;

import java.util.List;

@Data
public class UpdateProductDTO {
    @JsonIgnore
    private int id;

    private String name;
    private String description;
    private String specificities;

    private Double price;
    private Double discount;

    private List<String> images;

    private OfferingVisibility visibility;
    private Boolean available;

    private List<Integer> categoryIds; // optional if products can belong to multiple categories

    // TODO: add any product-specific fields, e.g., stock quantity, min/max purchase limits
}
