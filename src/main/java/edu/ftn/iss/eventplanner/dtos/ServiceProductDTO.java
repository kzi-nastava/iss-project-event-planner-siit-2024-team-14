package edu.ftn.iss.eventplanner.dtos;
import lombok.Data;

@Data
public class ServiceProductDTO {
    private String name;
    private String description;
    private double price;
    private String imageUrl;

    public ServiceProductDTO(String name, String description, double price, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
