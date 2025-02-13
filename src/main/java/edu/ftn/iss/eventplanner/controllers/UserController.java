package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.LoginDTO;
import edu.ftn.iss.eventplanner.dtos.TokenDTO;
import edu.ftn.iss.eventplanner.dtos.UserLoginDTO;
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

    @PostMapping("/login")
    public ResponseEntity<UserLoginDTO> login(@RequestBody LoginDTO loginDTO) {
        System.out.println("Received login data: " + loginDTO);

        UserLoginDTO userLoginDTO = userService.login(loginDTO);
        System.out.println("UserLoginDTO value: " + userLoginDTO);

        if (userLoginDTO == null) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        return new ResponseEntity<>(userLoginDTO, HttpStatus.OK);
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
