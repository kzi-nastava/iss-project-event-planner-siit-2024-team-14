package edu.ftn.iss.eventplanner.dtos.updateUsers;

import lombok.Data;

@Data
public class UpdateOrganizerDTO extends UpdateUserDTO {
    private Integer id;
    private String name;
    private String surname;
    private String address;
    private String city;
    private String phoneNumber;
}