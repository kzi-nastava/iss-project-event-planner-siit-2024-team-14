package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.CreateUserDTO;
import edu.ftn.iss.eventplanner.dtos.ReportDTO;
import edu.ftn.iss.eventplanner.dtos.ReportResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    // User registration endpoint
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody CreateUserDTO registerDTO) {
        // mock verification token
        String verificationToken = UUID.randomUUID().toString();

        System.out.println("Saving user with email: " + registerDTO.getEmail() + " and token: " + verificationToken);

        String verificationLink = "http://localhost:8080/api/email-verification/verify?token=" + verificationToken;

        System.out.println("Verification email sent to " + registerDTO.getEmail());
        System.out.println("Verification link: " + verificationLink);

        return new ResponseEntity<>("User registered successfully! Please verify your account via email.", HttpStatus.CREATED);
    }

    // Endpoint za prijavu korisnika
    @PostMapping("/reportUser")
    public ResponseEntity<Void> reportUser(@RequestBody ReportDTO reportDTO) {
        // Simulacija prijave korisnika
        System.out.println("User " + reportDTO.getReportedUserId() + " reported for: " + reportDTO.getReason());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Endpoint za prikaz svih prijava
    @GetMapping("/reports")
    public ResponseEntity<List<ReportResponseDTO>> getReports() {
        // Simulacija podataka iz baze
        List<ReportResponseDTO> reports = new ArrayList<>();
        reports.add(new ReportResponseDTO(1L, 1L, 2L, "Spam", LocalDate.now().minusDays(1), false, null));
        reports.add(new ReportResponseDTO(2L, 3L, 4L, "Harassment", LocalDate.now(), false, null));

        return ResponseEntity.ok(reports);
    }

    // Endpoint za rešavanje prijave korisnika
    @PatchMapping("/resolveReport/{reportId}")
    public ResponseEntity<Void> resolveReport(@PathVariable Long reportId) {
        // Simulacija rešavanja prijave
        System.out.println("Report with ID " + reportId + " resolved.");

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
