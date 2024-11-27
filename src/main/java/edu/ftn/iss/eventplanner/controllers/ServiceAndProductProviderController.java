package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.LoginDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/providers")
public class ServiceAndProductProviderController {

    private static final String MOCK_EMAIL = "provider@example.com";
    private static final String MOCK_PASSWORD = "provider123";
    private static final boolean MOCK_USER_ACTIVE = true;

    // Service and Product Provider login endpoint
    @PostMapping("/login")
    public ResponseEntity<String> loginProvider(@RequestBody LoginDTO loginDTO) {
        if (!MOCK_EMAIL.equals(loginDTO.getEmail())) {
            return new ResponseEntity<>("Invalid email.", HttpStatus.UNAUTHORIZED);
        }
        if (!MOCK_PASSWORD.equals(loginDTO.getPassword())) {
            return new ResponseEntity<>("Incorrect password.", HttpStatus.UNAUTHORIZED);
        }
        if (!MOCK_USER_ACTIVE) {
            return new ResponseEntity<>("Your account is not active. Please verify your email.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("Service and Product Provider login successful!", HttpStatus.OK);
    }
}
