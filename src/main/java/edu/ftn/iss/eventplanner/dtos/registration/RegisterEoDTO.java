package edu.ftn.iss.eventplanner.dtos.registration;

import lombok.Data;

@Data
public class RegisterEoDTO {
    private String email;
    private String password;
    private String confirmPassword;
    private String name;
    private String surname;
    private String address;
    private String city;
    private int phoneNumber;
    private String photo;
}
