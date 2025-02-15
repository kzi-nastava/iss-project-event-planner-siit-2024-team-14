package edu.ftn.iss.eventplanner.dtos.registration;

import edu.ftn.iss.eventplanner.dtos.GetUserDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class RegisterResponseDTO {
    // Getters and Setters
    private String message;
    private boolean success;

    // Constructor
    public RegisterResponseDTO(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

}

