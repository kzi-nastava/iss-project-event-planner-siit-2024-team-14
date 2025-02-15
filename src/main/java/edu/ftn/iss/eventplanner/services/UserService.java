package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.*;
import edu.ftn.iss.eventplanner.entities.User;
import edu.ftn.iss.eventplanner.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import edu.ftn.iss.eventplanner.security.JWTUtil;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<UserLoginDTO> login(LoginDTO loginDTO) {
        Optional<User> userOpt = userRepository.findByEmail(loginDTO.getEmail());

        if (userOpt.isEmpty()) {
            System.out.println("No user found with email: " + loginDTO.getEmail());
            return ResponseEntity.status(404).body(null);
        }

        User user = userOpt.get();
        System.out.println("User found with email: " + user.getEmail());

        if (!user.getPassword().equals(loginDTO.getPassword())) {
            System.out.println("Password mismatch for user: " + user.getEmail());
            return ResponseEntity.status(401).body(null);
        }

        // Generate token
        String token = JWTUtil.generateToken(user.getEmail());

        // Prepare User DTO
        GetUserDTO userDTO = new GetUserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getClass().getSimpleName());
        userDTO.setCity(user.getCity());

        // Prepare UserLoginDTO
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setToken(token);
        userLoginDTO.setUser(userDTO);

        return ResponseEntity.ok(userLoginDTO);
    }
}
