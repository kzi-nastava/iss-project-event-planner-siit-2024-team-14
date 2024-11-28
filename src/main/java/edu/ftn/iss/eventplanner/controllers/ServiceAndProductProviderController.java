package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.LoginDTO;
import edu.ftn.iss.eventplanner.dtos.CreateServiceAndProductProviderDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @PostMapping("/register")
    public ResponseEntity<String> registerServiceAndProductProvider(@RequestBody CreateServiceAndProductProviderDTO registerDTO) {
        // mock verification token
        String verificationToken = UUID.randomUUID().toString();

        System.out.println("Saving provider with email: " + registerDTO.getEmail() + " and token: " + verificationToken);

        String verificationLink = "http://localhost:8080/api/email-verification/verify?token=" + verificationToken;

        System.out.println("Verification email sent to " + registerDTO.getEmail());
        System.out.println("Verification link: " + verificationLink);

        return new ResponseEntity<>("Service and product provider registered successfully! Please verify your account via email.", HttpStatus.CREATED);
    }
}
