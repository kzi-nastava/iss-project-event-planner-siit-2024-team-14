package edu.ftn.iss.eventplanner.dtos.updateUsers;

import lombok.Data;

@Data
public class UpdateProviderDTO extends UpdateUserDTO {
    private Integer id;
    private String name;
    private String description;
    private String address;
    private String city;
    private String phoneNumber;
}
