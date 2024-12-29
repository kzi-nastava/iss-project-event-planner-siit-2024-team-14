package edu.ftn.iss.eventplanner.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateServiceDTO {
    private String name;
    private String description;
    private String specificities;

    private double price;
    private double discount;

    private String[] images;


    /*private boolean available;
    private boolean visible;

    @NotNull
    private PotentiallyNewCategoryDTO category;


    public static class PotentiallyNewCategoryDTO {
        @Nullable
        private Integer id;
        @Nullable
        private String name;
        @Nullable
        private String description;
    }

    public static class ReservationProperties {

    }*/
}
