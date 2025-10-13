package edu.ftn.iss.eventplanner.entities;

import edu.ftn.iss.eventplanner.enums.OfferingVisibility;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {
    private int providerId;

    private String name;
    private String description = "";
    private String specificities = "";

    private double price;
    private double discount = 0;

    private List<String> images = new ArrayList<>();

    private List<Integer> applicableEventTypeIds = new ArrayList<>();

    private Integer categoryId;
    private String categoryName;
    private String categoryDescription;

    private OfferingVisibility visibility = OfferingVisibility.PUBLIC;

    private boolean available = true;

    public boolean requestsNewCategory() {
        return categoryId == null;
    }
}
