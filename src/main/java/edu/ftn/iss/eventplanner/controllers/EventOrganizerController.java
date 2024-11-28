package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
      
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<GetOrganizerDTO>> getOrganizers() {
        Collection<GetOrganizerDTO> organizers = new ArrayList<>();

        GetOrganizerDTO organizer1 = new GetOrganizerDTO();
        organizer1.setFirstName("Organizer1");
        organizer1.setLastName("Surname1");
        organizer1.setAddress("123 Event Street");
        organizer1.setPhoneNumber("123-456-7890");
        organizer1.setProfilePicture("photo");

        GetOrganizerDTO organizer2 = new GetOrganizerDTO();
        organizer2.setFirstName("Organizer2");
        organizer2.setLastName("Surname2");
        organizer2.setAddress("123 Event Street");
        organizer2.setPhoneNumber("123-456-7890");
        organizer2.setProfilePicture("photo");

        organizers.add(organizer1);
        organizers.add(organizer2);

        return new ResponseEntity<Collection<GetOrganizerDTO>>(organizers, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetOrganizerDTO> getOrganizer(@PathVariable("id") Long id) {
        GetOrganizerDTO organizer = new GetOrganizerDTO();

        if (organizer == null) {
            return new ResponseEntity<GetOrganizerDTO>(HttpStatus.NOT_FOUND);
        }

        organizer.setFirstName("Organizer");
        organizer.setLastName("Surname");
        organizer.setAddress("123 Event Street");
        organizer.setPhoneNumber("123-456-7890");
        organizer.setProfilePicture("photo");

        return new ResponseEntity<GetOrganizerDTO>(organizer, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdatedOrganizerDTO> updateOrganizer(@PathVariable Long id, @RequestBody UpdateOrganizerDTO organizer) throws Exception{
        UpdatedOrganizerDTO updatedOrganizer = new UpdatedOrganizerDTO();

        updatedOrganizer.setId(id);
        updatedOrganizer.setFirstName(organizer.getFirstName());
        updatedOrganizer.setLastName(organizer.getLastName());
        updatedOrganizer.setAddress(organizer.getAddress());
        updatedOrganizer.setPhoneNumber(organizer.getPhoneNumber());
        updatedOrganizer.setProfilePicture(organizer.getProfilePicture());

        return new ResponseEntity<UpdatedOrganizerDTO>(updatedOrganizer, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChangedPasswordDTO> changePassword(@PathVariable Long id, @RequestBody ChangePasswordDTO password) throws Exception {
        ChangedPasswordDTO changedPassword = new ChangedPasswordDTO();

        changedPassword.setId(id);
        changedPassword.setOldPassword(password.getOldPassword());
        changedPassword.setNewPassword(password.getNewPassword());
        changedPassword.setConfirmNewPassword(password.getConfirmNewPassword());

        return new ResponseEntity<ChangedPasswordDTO>(changedPassword, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deactivateOrganizer(@PathVariable Long id) {
        DeactivateAccountDTO account = new DeactivateAccountDTO();

        account.setActive(false);

        return new ResponseEntity<>("Organizer account deactivated.", HttpStatus.OK);
    }
}
