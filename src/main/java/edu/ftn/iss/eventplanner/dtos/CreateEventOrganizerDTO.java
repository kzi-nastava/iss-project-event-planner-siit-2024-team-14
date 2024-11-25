package edu.ftn.iss.eventplanner.dtos;
import lombok.Data;

@Data
public class CreateEventOrganizerDTO {
    private String email;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String address;
    private String phoneNumber;
}

