package edu.ftn.iss.eventplanner.dtos.updateUsers;

import lombok.Data;

@Data
public class UpdatedUserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String profilePicture;
}
