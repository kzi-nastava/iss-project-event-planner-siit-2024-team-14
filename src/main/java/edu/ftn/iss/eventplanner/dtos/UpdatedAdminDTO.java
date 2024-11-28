package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;

@Data
public class UpdatedAdminDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String profilePicture;
}
