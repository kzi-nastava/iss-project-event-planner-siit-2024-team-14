package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;

@Data
public class UpdateOrganizerDTO extends UpdateUserDTO {
    private String address;
    private String phoneNumber;
}