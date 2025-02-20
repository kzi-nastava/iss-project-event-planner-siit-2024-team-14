package edu.ftn.iss.eventplanner.dtos.update;

import lombok.Data;

@Data
public class UpdatedOrganizerDTO {
    private String name;
    private String surname;
    private String address;
    private String city;
    private int phoneNumber;
}
