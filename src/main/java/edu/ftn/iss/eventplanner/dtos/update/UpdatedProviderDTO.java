package edu.ftn.iss.eventplanner.dtos.update;

import lombok.Data;

@Data
public class UpdatedProviderDTO {
    private String name;
    private String description;
    private String address;
    private String city;
    private int phoneNumber;
}
