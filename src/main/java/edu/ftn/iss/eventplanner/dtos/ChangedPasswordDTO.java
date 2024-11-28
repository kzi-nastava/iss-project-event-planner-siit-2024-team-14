package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;

@Data
public class ChangedPasswordDTO {
    private Long id;
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}
