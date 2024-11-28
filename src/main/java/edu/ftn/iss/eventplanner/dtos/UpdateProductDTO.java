package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;

import java.util.List;

@Data
public class UpdateProductDTO {
    private String name;
    private String description;
    private double price;
    private double discount;
    private String imageUrl;
    private List<String> eventTypes;
    private boolean available;
    private boolean visible;
}
