package edu.ftn.iss.eventplanner.dtos.productDetails;

import edu.ftn.iss.eventplanner.dtos.getUsers.ProviderDTO;
import edu.ftn.iss.eventplanner.enums.OfferingVisibility;

import lombok.Data;

@Data
public class ProductDTO {
    private long id;
    private ProviderDTO provider;

    private String name;
    private String description;
    private String specificities;

    private double price;
    private double discount;

    private /* URL[] */ String[] images;

    private CategoryDTO category;

    // --- Availability info (could be grouped into ProductAvailabilityProperties)
    private boolean available;
    private OfferingVisibility visibility;

    public boolean isVisible() {
        return visibility == null || visibility == OfferingVisibility.PUBLIC;
    }

    public boolean isIsVisible() {
        return visibility == null || visibility == OfferingVisibility.PUBLIC;
    }

    public Integer getProviderId() {
        return provider == null ? null : provider.getId();
    }

    public boolean isIsAvailable() {
        return available;
    }

    public String getImageUrl() {
        try {
            return this.images[0];
        } catch (NullPointerException | IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public void setProviderId(Integer id) {

    }

    // TODO: Model and DTO field names should match in order for clients to know sort parameters
}
