package edu.ftn.iss.eventplanner.dtos.getUsers;

import lombok.Data;

@Data
public class OrganizerDTO extends UserDTO {
    private String address;
    private String phoneNumber;
    private String name;
    private String surname;
    private String profilePhoto;
}
