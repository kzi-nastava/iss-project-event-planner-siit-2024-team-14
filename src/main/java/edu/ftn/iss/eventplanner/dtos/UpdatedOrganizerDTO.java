package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;

@Data
public class UpdatedOrganizerDTO extends UpdateUserDTO {
    private Long id;
    private String address;
    private String phoneNumber;
}