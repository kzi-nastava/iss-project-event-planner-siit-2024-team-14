package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.CreateUserDTO;
import edu.ftn.iss.eventplanner.dtos.LoginDTO;
import edu.ftn.iss.eventplanner.dtos.TokenDTO;
import edu.ftn.iss.eventplanner.entities.User;
import edu.ftn.iss.eventplanner.repositories.UserRepository;
import edu.ftn.iss.eventplanner.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public TokenDTO login(LoginDTO loginDTO) {
        Optional<User> userOpt = userRepository.findByEmail(loginDTO.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Log the user and email check
            System.out.println("User found with email: " + user.getEmail());

            if (user.getPassword().equals(loginDTO.getPassword())) {
                String token = JWTUtil.generateToken(user.getEmail());
                TokenDTO tokenDTO = new TokenDTO();
                tokenDTO.setToken(token);
                return tokenDTO;
            } else {
                System.out.println("Password mismatch for user: " + user.getEmail());
            }
        } else {
            System.out.println("No user found with email: " + loginDTO.getEmail());
        }
        return null;  // Or throw an exception for better error handling
    }



    // POPRAVITI
    public User registerUser(CreateUserDTO createUserDTO) {
        User user = new User();
        user.setEmail(createUserDTO.getEmail());
        user.setPassword(createUserDTO.getPassword());  // Za sigurnost koristi hash lozinku!
        user.setVerified(false);  // Novi korisnici nisu verificirani
        return userRepository.save(user);
    }
}