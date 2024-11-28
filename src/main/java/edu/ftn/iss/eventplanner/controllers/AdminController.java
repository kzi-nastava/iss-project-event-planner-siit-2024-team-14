package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<GetAdminDTO>> getAdmins() {
        Collection<GetAdminDTO> admins = new ArrayList<>();

        GetAdminDTO admin1 = new GetAdminDTO();
        admin1.setFirstName("Admin1");
        admin1.setLastName("Surname1");
        admin1.setEmail("admin1@email.com");
        admin1.setProfilePicture("admin-1-picture");

        GetAdminDTO admin2 = new GetAdminDTO();
        admin2.setFirstName("Admin2");
        admin2.setLastName("Surname2");
        admin2.setEmail("admin2@email.com");
        admin2.setProfilePicture("admin-2-picture");

        admins.add(admin1);
        admins.add(admin2);

        return new ResponseEntity<Collection<GetAdminDTO>>(admins, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetAdminDTO> getAdmin(@PathVariable("id") Long id) {
        GetAdminDTO admin = new GetAdminDTO();

        if (admin == null) {
            return new ResponseEntity<GetAdminDTO>(HttpStatus.NOT_FOUND);
        }

        admin.setFirstName("Admin");
        admin.setLastName("Surname");
        admin.setEmail("admin@email.com");
        admin.setProfilePicture("admin-picture");

        return new ResponseEntity<GetAdminDTO>(admin, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdatedAdminDTO> updateAdmin(@PathVariable Long id, @RequestBody UpdateAdminDTO admin) throws Exception{
        UpdatedAdminDTO updatedAdmin = new UpdatedAdminDTO();

        updatedAdmin.setId(id);
        updatedAdmin.setFirstName(admin.getFirstName());
        updatedAdmin.setLastName(admin.getLastName());
        updatedAdmin.setProfilePicture(admin.getProfilePicture());

        return new ResponseEntity<UpdatedAdminDTO>(updatedAdmin, HttpStatus.OK);
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
    public ResponseEntity<String> deactivateAdmin(@PathVariable Long id) {
        DeactivateAccountDTO account = new DeactivateAccountDTO();

        account.setActive(false);

        return new ResponseEntity<>("Admin account deactivated.", HttpStatus.OK);
    }
}
