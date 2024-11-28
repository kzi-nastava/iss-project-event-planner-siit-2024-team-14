package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;

import java.util.List;

@Data
public class GetProductDTO {
    private String name;
    private String description;
    private double price;
    private double discount;
    private String imageUrl;
    private int categoryId;
    private boolean available;
}
