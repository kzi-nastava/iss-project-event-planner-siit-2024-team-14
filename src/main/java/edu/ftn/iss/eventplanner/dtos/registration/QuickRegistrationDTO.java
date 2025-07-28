package edu.ftn.iss.eventplanner.dtos.registration;

import lombok.Data;

@Data
public class QuickRegistrationDTO {
    private String email;
    private String password;
    private String confirmPassword;
    private String fullName;
    private Integer eventId;
}
