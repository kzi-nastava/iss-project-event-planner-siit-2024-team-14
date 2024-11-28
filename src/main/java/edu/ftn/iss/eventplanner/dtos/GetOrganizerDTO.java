package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;

import java.util.List;

@Data
public class GetOrganizerDTO extends GetUserDTO {
    private String address;
    private String phoneNumber;
}
