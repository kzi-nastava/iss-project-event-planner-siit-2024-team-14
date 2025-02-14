package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

import edu.ftn.iss.eventplanner.dtos.LoginDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admins")
public class AdminController {
  
    private static final String MOCK_EMAIL = "admin@example.com";
    private static final String MOCK_PASSWORD = "admin123";
    private static final boolean MOCK_USER_ACTIVE = true;

    // Admin login endpoint
    @PostMapping("/login")
    public ResponseEntity<String> loginAdmin(@RequestBody LoginDTO loginDTO) {
        if (!MOCK_EMAIL.equals(loginDTO.getEmail())) {
            return new ResponseEntity<>("Invalid email.", HttpStatus.UNAUTHORIZED);
        }
        if (!MOCK_PASSWORD.equals(loginDTO.getPassword())) {
            return new ResponseEntity<>("Incorrect password.", HttpStatus.UNAUTHORIZED);
        }
        if (!MOCK_USER_ACTIVE) {
            return new ResponseEntity<>("Your account is not active. Please verify your email.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("Admin login successful!", HttpStatus.OK);
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
    public ResponseEntity<String> deactivateAdmin(@PathVariable Long id) {
        DeactivateAccountDTO account = new DeactivateAccountDTO();

        account.setActive(false);

        return new ResponseEntity<>("Admin account deactivated.", HttpStatus.NO_CONTENT);
    }
}
