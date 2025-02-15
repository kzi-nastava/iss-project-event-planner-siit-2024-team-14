package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.EmailVerificationDTO;
import edu.ftn.iss.eventplanner.services.EmailService;
import edu.ftn.iss.eventplanner.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email-verification")
public class EmailVerificationController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestBody EmailVerificationDTO verificationDTO) {
        System.out.println("Verifying email for token: " + verificationDTO.getToken());

        if (verificationDTO.getToken() == null || verificationDTO.getToken().isEmpty()) {
            return new ResponseEntity<>("Invalid or expired token.", HttpStatus.BAD_REQUEST);
        }

        // Call userService to validate the token and activate user
        boolean isValid = emailService.verifyActivationToken(verificationDTO.getToken());

        if (isValid) {
            return new ResponseEntity<>("Email verified successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid or expired token.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<String> resendVerificationEmail(@RequestBody EmailVerificationDTO verificationDTO) {
        System.out.println("Resending verification email to token: " + verificationDTO.getToken());

        // Call userService to resend email
        boolean isResent = emailService.resendActivationEmail(verificationDTO.getToken());

        if (isResent) {
            return new ResponseEntity<>("Verification email resent successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to resend email. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
