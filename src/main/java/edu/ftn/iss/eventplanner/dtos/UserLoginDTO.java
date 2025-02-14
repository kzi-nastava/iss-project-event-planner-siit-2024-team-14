package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String token;
    private GetUserDTO user;
}
