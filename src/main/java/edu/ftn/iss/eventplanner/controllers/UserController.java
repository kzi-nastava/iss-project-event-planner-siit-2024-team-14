package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.*;
import edu.ftn.iss.eventplanner.dtos.CreateUserDTO;
import edu.ftn.iss.eventplanner.entities.User;
import edu.ftn.iss.eventplanner.exceptions.InvalidCredentialsException;
import edu.ftn.iss.eventplanner.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:4200")  // Allow only requests from Angular frontend
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Login
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) {
        System.out.println("Received login data: " + loginDTO);

        TokenDTO tokenDTO = userService.login(loginDTO);
        System.out.println("provera");
        if (tokenDTO == null) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return new ResponseEntity<>(tokenDTO, HttpStatus.OK);
    }

    // User registration endpoint
    // Register
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody CreateUserDTO createUserDTO) {
        userService.registerUser(createUserDTO);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
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
      
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<GetUserDTO>> getUsers() {
        Collection<GetUserDTO> users = new ArrayList<>();

        GetUserDTO user1 = new GetUserDTO();
        user1.setFirstName("User1");
        user1.setLastName("Surname1");
        user1.setEmail("user1@email.com");
        user1.setProfilePicture("user-1-picture");

        GetUserDTO user2 = new GetUserDTO();
        user2.setFirstName("User2");
        user2.setLastName("Surname2");
        user2.setEmail("user1@email.com");
        user2.setProfilePicture("user-2-picture");

        users.add(user1);
        users.add(user2);

        return new ResponseEntity<Collection<GetUserDTO>>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetUserDTO> getUser(@PathVariable("id") Long id) {
        GetUserDTO user = new GetUserDTO();

        if (user == null) {
            return new ResponseEntity<GetUserDTO>(HttpStatus.NOT_FOUND);
        }

        user.setFirstName("User");
        user.setLastName("Surname");
        user.setEmail("user@email.com");
        user.setProfilePicture("user-picture");

        return new ResponseEntity<GetUserDTO>(user, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdatedUserDTO> updateUser(@PathVariable Long id, @RequestBody UpdateUserDTO user) throws Exception{
        UpdatedUserDTO updatedUser = new UpdatedUserDTO();

        updatedUser.setId(id);
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setProfilePicture(user.getProfilePicture());

        return new ResponseEntity<UpdatedUserDTO>(updatedUser, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/change-password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChangedPasswordDTO> changePassword(@PathVariable Long id, @RequestBody ChangePasswordDTO password) throws Exception {
        ChangedPasswordDTO changedPassword = new ChangedPasswordDTO();

        changedPassword.setId(id);
        changedPassword.setOldPassword(password.getOldPassword());
        changedPassword.setNewPassword(password.getNewPassword());
        changedPassword.setConfirmNewPassword(password.getConfirmNewPassword());

        return new ResponseEntity<ChangedPasswordDTO>(changedPassword, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deactivateUser(@PathVariable Long id) {
        DeactivateAccountDTO account = new DeactivateAccountDTO();

        account.setActive(false);

        return new ResponseEntity<>("User account deactivated.", HttpStatus.NO_CONTENT);
    }
}
