package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;

import java.util.List;

@Data
public class UpdateProviderDTO {
    private String description;
    private String address;
    private String phoneNumber;
    private List<String> photos;
}
