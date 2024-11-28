package edu.ftn.iss.eventplanner.dtos;
import lombok.Data;

@Data
public class CreateEventOrganizerDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String profilePicture;
}

