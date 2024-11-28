package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.LoginDTO;
import edu.ftn.iss.eventplanner.dtos.CreateEventOrganizerDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/organizers")
public class EventOrganizerController {

    private static final String MOCK_EMAIL = "organizer@example.com";
    private static final String MOCK_PASSWORD = "organizer123";
    private static final boolean MOCK_USER_ACTIVE = true;

    // Event Organizer login endpoint
    @PostMapping("/login")
    public ResponseEntity<String> loginOrganizer(@RequestBody LoginDTO loginDTO) {
        if (!MOCK_EMAIL.equals(loginDTO.getEmail())) {
            return new ResponseEntity<>("Invalid email.", HttpStatus.UNAUTHORIZED);
        }
        if (!MOCK_PASSWORD.equals(loginDTO.getPassword())) {
            return new ResponseEntity<>("Incorrect password.", HttpStatus.UNAUTHORIZED);
        }
        if (!MOCK_USER_ACTIVE) {
            return new ResponseEntity<>("Your account is not active. Please verify your email.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("Event Organizer login successful!", HttpStatus.OK);
      
    // Event Organizer registration
    @PostMapping("/register")
    public ResponseEntity<String> registerEventOrganizer(@RequestBody CreateEventOrganizerDTO registerDTO) {
        // mock verification token
        String verificationToken = UUID.randomUUID().toString();

        System.out.println("Saving organizer with email: " + registerDTO.getEmail() + " and token: " + verificationToken);

        String verificationLink = "http://localhost:8080/api/email-verification/verify?token=" + verificationToken;

        System.out.println("Verification email sent to " + registerDTO.getEmail());
        System.out.println("Verification link: " + verificationLink);

        return new ResponseEntity<>("Event organizer registered successfully! Please verify your account via email.", HttpStatus.CREATED);
    }
}
