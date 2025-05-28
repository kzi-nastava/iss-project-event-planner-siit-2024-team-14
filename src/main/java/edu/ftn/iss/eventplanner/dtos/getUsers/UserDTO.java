package edu.ftn.iss.eventplanner.dtos.getUsers;

import lombok.Data;

@Data
public class UserDTO {
    private Integer id;
    private String email;
    private String role;
    private String city;
}
