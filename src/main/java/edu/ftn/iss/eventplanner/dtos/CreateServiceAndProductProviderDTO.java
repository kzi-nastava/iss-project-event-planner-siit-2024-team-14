package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CreateServiceAndProductProviderDTO {
    private String email;
    private String companyName;
    private String address;
    private String phoneNumber;
    private String description;
    private List<String> profilePictures;
}
