package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.*;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterSppDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterResponseDTO;
import edu.ftn.iss.eventplanner.entities.User;
import edu.ftn.iss.eventplanner.entities.ServiceAndProductProvider;
import edu.ftn.iss.eventplanner.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.UUID;

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

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<RegisterResponseDTO> registerSpp(RegisterSppDTO dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new RegisterResponseDTO("Passwords do not match!", false));
        }

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(new RegisterResponseDTO("Email already in use!", false));
        }

        // Creating user with non-active status and token
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setActive(false);
        user.setTokenCreationDate(LocalDateTime.now());

        // Generating token
        String activationToken = UUID.randomUUID().toString();
        user.setActivationToken(activationToken);

        // Saving user with token, without activating
        userRepository.save(user);

        // Sending email for account activation
        try {
            emailService.sendActivationEmail(user.getEmail(), activationToken);
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body(new RegisterResponseDTO("Failed to send activation email. Please try again later.", false));
        }

        return ResponseEntity.ok(new RegisterResponseDTO("Registration successful! Check your email to activate your account.", true));
    }

    // FOR SPP
    private User create(RegisterSppDTO dto) {
        ServiceAndProductProvider user = new ServiceAndProductProvider();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setAddress(dto.getAddress());
        user.setCity(dto.getCity());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setActive(false);

        // Generate activation token
        user.generateActivationToken();
        user.setTokenCreationDate(LocalDateTime.now());

        return userRepository.save(user);
    }

    // Method to activate a user
    public ResponseEntity<RegisterResponseDTO> activate(String token) {
        User user = userRepository.findByActivationToken(token);
        if (user == null) {
            return ResponseEntity.badRequest().body(new RegisterResponseDTO("Invalid or expired activation token.", false));
        }

        // Check if token is expired (24-hour validity)
        LocalDateTime tokenCreationDate = user.getTokenCreationDate();
        if (tokenCreationDate.plusHours(24).isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(new RegisterResponseDTO("Activation token has expired. Please register again.", false));
        }

        // Activate the user
        user.setActive(true);
        user.setActivationToken(null);
        user.setTokenCreationDate(null);
        userRepository.save(user);

        return ResponseEntity.ok(new RegisterResponseDTO("Account activated successfully! You can now log in.", true));
    }

    public boolean verifyActivationToken(String token) {
        User user = userRepository.findByActivationToken(token);
        if (user == null) {
            return false; // Token is invalid
        }

        LocalDateTime tokenCreationTime = user.getTokenCreationDate();
        if (tokenCreationTime.plusHours(24).isBefore(LocalDateTime.now())) {
            return false; // Token has expired
        }

        // Token is valid, activate user
        user.setActive(true);
        user.setActivationToken(null); // Remove token after activation
        user.setTokenCreationDate(null); // Remove token creation date
        userRepository.save(user);
        return true;
    }

    public boolean resendActivationEmail(String token) {
        // Find the user by the activation token
        User user = userRepository.findByActivationToken(token);

        if (user == null) {
            // If no user is found with the provided token, return false
            System.out.println("User not found with the given activation token.");
            return false;
        }

        // Check if the token is expired (24-hour validity)
        LocalDateTime tokenCreationTime = user.getTokenCreationDate();
        if (tokenCreationTime.plusHours(24).isBefore(LocalDateTime.now())) {
            // If the token is expired, return false
            System.out.println("The activation token has expired.");
            return false;
        }

        // Generate a new activation token (optional, if needed)
        user.generateActivationToken();
        user.setTokenCreationDate(LocalDateTime.now());
        userRepository.save(user);

        // Send the activation email again
        try {
            emailService.sendActivationEmail(user.getEmail(), user.getActivationToken());
            System.out.println("Activation email resent successfully.");
            return true;
        } catch (MessagingException e) {
            // Handle any issues with sending the email
            System.out.println("Failed to resend the activation email.");
            return false;
        }
    }

    public UserLoginDTO login(LoginDTO loginDTO) {
        Optional<User> userOpt = userRepository.findByEmail(loginDTO.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Log the user and email check
            System.out.println("User found with email: " + user.getEmail());

            if (user.getPassword().equals(loginDTO.getPassword())) {
                String token = JWTUtil.generateToken(user.getEmail());

                // Kreiramo UserDTO sa podacima korisnika
                GetUserDTO userDTO = new GetUserDTO();
                userDTO.setId(user.getId());
                userDTO.setEmail(user.getEmail());
                userDTO.setRole(user.getClass().getSimpleName());
                userDTO.setCity(user.getCity());

                // Kreiramo UserLoginDTO koji sadrži i token i korisničke podatke
                UserLoginDTO userLoginDTO = new UserLoginDTO();
                userLoginDTO.setToken(token);
                userLoginDTO.setUser(userDTO);

                return userLoginDTO;
            } else {
                System.out.println("Password mismatch for user: " + user.getEmail());
            }
        } else {
            System.out.println("No user found with email: " + loginDTO.getEmail());
        }
        return null;  // Or throw an exception for better error handling
    }

}
