package edu.ftn.iss.eventplanner.dtos.registration;

import lombok.Data;

import java.util.List;

@Data
public class RegisterSppDTO {
    private String email;
    private String password;
    private String confirmPassword;
    private String companyName;
    private String companyDescription;
    private String address;
    private String city;
    private int phoneNumber;
    private List<String> photos;
}
