package edu.ftn.iss.eventplanner.controllers;
import edu.ftn.iss.eventplanner.dtos.getUsers.UserDTO;
import edu.ftn.iss.eventplanner.dtos.login.LoginDTO;
import edu.ftn.iss.eventplanner.dtos.login.LoginResponseDTO;
import edu.ftn.iss.eventplanner.dtos.updateUsers.ChangePasswordDTO;
import edu.ftn.iss.eventplanner.dtos.updateUsers.ChangedPasswordDTO;
import edu.ftn.iss.eventplanner.entities.User;
import jakarta.validation.constraints.Email;
import edu.ftn.iss.eventplanner.services.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequestMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        System.out.println("Received login data: " + loginDTO);
        return userService.login(loginDTO); // Directly returning RegisterResponseDTO
    }

    @PutMapping("/changePassword")
    public ResponseEntity<ChangedPasswordDTO> changePassword(@RequestBody ChangePasswordDTO dto) {
        return userService.changePassword(dto);
    }

    @GetMapping(path = "/~{email}")
    @ResponseStatus(HttpStatus.OK)
    UserDTO getByEmail(@PathVariable @Email String email) {
        var user = userService.getUserByEmail(email);
        return modelMapper.map(user, UserDTO.class);
    }

    @GetMapping(path = {"whoami"})
    @PreAuthorize("isAuthenticated()")
    UserDTO whoAmI(
            @AuthenticationPrincipal @NotNull User principal
    ) {
        return modelMapper.map(principal, UserDTO.class);
    }

}
