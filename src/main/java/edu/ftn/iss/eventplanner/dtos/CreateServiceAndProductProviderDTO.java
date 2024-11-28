package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CreateServiceAndProductProviderDTO {
    private String email;
    private String password;
    private String companyName;
    private String description;
    private String address;
    private String phoneNumber;
    private List<String> profilePictures;
}
