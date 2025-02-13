package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.LoginDTO;
import edu.ftn.iss.eventplanner.dtos.TokenDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterSppDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterResponseDTO;
import edu.ftn.iss.eventplanner.exceptions.InvalidCredentialsException;
import edu.ftn.iss.eventplanner.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
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

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterSppDTO dto) {
        return userService.registerSpp(dto);
    }

    @GetMapping("/activate")
    public ResponseEntity<RegisterResponseDTO> activate(@RequestParam("token") String token) {
        return userService.activate(token);
    }
}
