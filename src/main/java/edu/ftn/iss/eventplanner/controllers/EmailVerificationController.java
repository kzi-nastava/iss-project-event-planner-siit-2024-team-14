package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.EmailVerificationDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email-verification")
public class EmailVerificationController {

    @PostMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestBody EmailVerificationDTO verificationDTO) {
        System.out.println("Verifying email for token: " + verificationDTO.getToken());

        if (verificationDTO.getToken() == null || verificationDTO.getToken().isEmpty()) {
            return new ResponseEntity<>("Invalid or expired token.", HttpStatus.BAD_REQUEST);
        }

        boolean isValid = true; // Replace with actual validation logic

        if (isValid) {
            System.out.println("Email successfully verified for token: " + verificationDTO.getToken());
            return new ResponseEntity<>("Email verified successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid or expired token.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<String> resendVerificationEmail(@RequestBody EmailVerificationDTO verificationDTO) {
        System.out.println("Resending verification email to token: " + verificationDTO.getToken());

        String verificationLink = "http://localhost:8080/api/verification/verify?token=" + verificationDTO.getToken();
        System.out.println("Verification link: " + verificationLink);

        return new ResponseEntity<>("Verification email resent successfully!", HttpStatus.OK);
    }
}
