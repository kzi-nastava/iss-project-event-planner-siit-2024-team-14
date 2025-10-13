package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;

@Data
public class CreateProductDTO {
    private String name;
    private String description;
    private String specificities;
    private Double price;
    private Double discount;
    private String imageUrl;
    private Boolean available;
    private Boolean visible;

    private CategoryWrapper category; // <-- object wrapper

    private Integer providerId;

    @Data
    public static class CategoryWrapper {
        private Integer id;
    }

    public Integer getCategoryId() {
        return category != null ? category.getId() : null;
    }
}
