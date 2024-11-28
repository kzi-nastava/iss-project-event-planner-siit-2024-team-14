package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CreateUserDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}

