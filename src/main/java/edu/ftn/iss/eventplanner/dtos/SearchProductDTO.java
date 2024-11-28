package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;

@Data
public class SearchProductDTO {
    private String name;
    private String category;
    private String eventType;
    private Double minPrice;
    private Double maxPrice;
    private Boolean available;
    private Boolean visible;
    private String description;
}
