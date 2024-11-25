package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.LoginDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    // Mock user data (for demonstration purposes)
    private static final String MOCK_EMAIL = "admin@example.com";
    private static final String MOCK_PASSWORD = "admin123";
    private static final boolean MOCK_USER_ACTIVE = true;

    // Admin login endpoint
    @PostMapping("/login")
    public ResponseEntity<String> loginAdmin(@RequestBody LoginDTO loginDTO) {
        // Validate email
        if (!MOCK_EMAIL.equals(loginDTO.getEmail())) {
            return new ResponseEntity<>("Invalid email.", HttpStatus.UNAUTHORIZED);
        }

        // Validate password
        if (!MOCK_PASSWORD.equals(loginDTO.getPassword())) {
            return new ResponseEntity<>("Incorrect password.", HttpStatus.UNAUTHORIZED);
        }

        // Check if user is active
        if (!MOCK_USER_ACTIVE) {
            return new ResponseEntity<>("Your account is not active. Please verify your email.", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>("Admin login successful!", HttpStatus.OK);
    }
}
