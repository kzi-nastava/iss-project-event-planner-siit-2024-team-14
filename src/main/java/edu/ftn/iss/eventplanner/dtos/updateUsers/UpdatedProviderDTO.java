package edu.ftn.iss.eventplanner.dtos.updateUsers;

import lombok.Data;

@Data
public class UpdatedProviderDTO {
    private String description;
    private String address;
    private String city;
    private int phoneNumber;
}
