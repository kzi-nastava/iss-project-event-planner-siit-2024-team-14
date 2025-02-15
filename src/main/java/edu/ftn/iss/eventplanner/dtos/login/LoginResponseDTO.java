package edu.ftn.iss.eventplanner.dtos.login;

import edu.ftn.iss.eventplanner.dtos.GetUserDTO;
import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private GetUserDTO user;
    private String message;
    private boolean success;

    // Add constructor that takes parameters
    public LoginResponseDTO(String token, GetUserDTO user, String message, boolean success) {
        this.token = token;
        this.user = user;
        this.message = message;
        this.success = success;
    }

    // Default constructor if needed (optional)
    public LoginResponseDTO() {}
}
