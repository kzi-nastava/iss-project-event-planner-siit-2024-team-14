package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;

@Data
public class GetUserDTO {
    private Integer id;
    private String email;
    private String role;
    private String city;
}
