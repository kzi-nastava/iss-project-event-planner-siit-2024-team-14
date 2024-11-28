package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.CreateServiceAndProductProviderDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/providers")
public class ServiceAndProductProviderController {

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
