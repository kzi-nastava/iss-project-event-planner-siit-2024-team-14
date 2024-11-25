package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CreateUserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String address;
    private String phoneNumber;
    private boolean isActive;
    private String companyName;
    private String description;
    private List<String> profilePictures;
}

