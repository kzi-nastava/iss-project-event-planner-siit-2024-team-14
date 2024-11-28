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

@RestController
@RequestMapping("/api/organizers")
public class EventOrganizerController {

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
