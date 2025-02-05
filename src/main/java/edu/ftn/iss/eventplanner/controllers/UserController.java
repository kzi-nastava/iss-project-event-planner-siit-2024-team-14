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
        System.out.println("TokenDTO value: " + tokenDTO);
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
}