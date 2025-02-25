package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;

import java.util.List;

@Data
public class GetProviderDTO {
    private String companyName;
    private String description;
    private String address;
    private String phoneNumber;
    private List<String> photos;
    private String[] categories; // Categories of services/products
    private String[] eventTypes; // Event types they serve
}
