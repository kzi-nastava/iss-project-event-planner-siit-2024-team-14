package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;

@Data
public class GetUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String profilePicture;
}
