package edu.ftn.iss.eventplanner.dtos.update;

import edu.ftn.iss.eventplanner.dtos.UpdateUserDTO;
import lombok.Data;

import java.util.List;

@Data
public class UpdateProviderDTO extends UpdateUserDTO {
    private Integer id;
    private String name;
    private String description;
    private String address;
    private String city;
    private String phoneNumber;
}
