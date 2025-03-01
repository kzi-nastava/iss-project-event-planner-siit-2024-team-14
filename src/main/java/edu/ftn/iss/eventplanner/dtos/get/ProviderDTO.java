package edu.ftn.iss.eventplanner.dtos.get;

import lombok.Data;

import java.util.List;

@Data
public class ProviderDTO extends UserDTO {
    private String address;
    private String phoneNumber;
    private String name;
    private String description;
    private List<String> photos;
}
