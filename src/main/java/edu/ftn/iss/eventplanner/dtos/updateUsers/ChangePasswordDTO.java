package edu.ftn.iss.eventplanner.dtos.updateUsers;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    private int id;
    private String oldPassword;
    private String password;
}
