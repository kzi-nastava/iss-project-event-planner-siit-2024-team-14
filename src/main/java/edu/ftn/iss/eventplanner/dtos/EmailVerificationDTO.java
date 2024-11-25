package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;

@Data
public class EmailVerificationDTO {
    private String email;
    private String token;
}
