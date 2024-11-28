package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

import edu.ftn.iss.eventplanner.dtos.LoginDTO;
import edu.ftn.iss.eventplanner.dtos.CreateUserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
  
    private static final String MOCK_EMAIL = "user@example.com";
    private static final String MOCK_PASSWORD = "user123";
    private static final boolean MOCK_USER_ACTIVE = true;

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDTO loginDTO) {
        if (!MOCK_EMAIL.equals(loginDTO.getEmail())) {
            return new ResponseEntity<>("Invalid email.", HttpStatus.UNAUTHORIZED);
        }
        if (!MOCK_PASSWORD.equals(loginDTO.getPassword())) {
            return new ResponseEntity<>("Incorrect password.", HttpStatus.UNAUTHORIZED);
        }
        if (!MOCK_USER_ACTIVE) {
            return new ResponseEntity<>("Your account is not active. Please verify your email.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("Login successful!", HttpStatus.OK);
    }

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
