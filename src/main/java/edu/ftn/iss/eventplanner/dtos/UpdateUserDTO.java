package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private String firstName;
    private String lastName;
    private String profilePicture;
}
