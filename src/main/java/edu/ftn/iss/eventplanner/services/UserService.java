package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.get.UserDTO;
import edu.ftn.iss.eventplanner.dtos.login.LoginDTO;
import edu.ftn.iss.eventplanner.dtos.login.LoginResponseDTO;
import edu.ftn.iss.eventplanner.dtos.update.ChangePasswordDTO;
import edu.ftn.iss.eventplanner.dtos.update.ChangedPasswordDTO;
import edu.ftn.iss.eventplanner.entities.ReportRequest;
import edu.ftn.iss.eventplanner.entities.User;
import edu.ftn.iss.eventplanner.enums.Status;
import edu.ftn.iss.eventplanner.repositories.ReportRequestRepository;
import edu.ftn.iss.eventplanner.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import edu.ftn.iss.eventplanner.security.JWTUtil;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private  final ReportRequestRepository reportRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;  // Fix here!

    public UserService(ReportRequestRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public ResponseEntity<LoginResponseDTO> login(LoginDTO loginDTO) {
        Optional<User> userOpt = userRepository.findByEmail(loginDTO.getEmail());

        if (userOpt.isEmpty()) {
            System.out.println("No user found with email: " + loginDTO.getEmail());
            return ResponseEntity.status(404).body(new LoginResponseDTO(null, null, "User not found!", false));
        }

        User user = userOpt.get();
        System.out.println("User found with email: " + user.getEmail());

        if (!user.getPassword().equals(loginDTO.getPassword())) {
            System.out.println("Password mismatch for user: " + user.getEmail());
            return ResponseEntity.status(401).body(new LoginResponseDTO(null, null, "Password mismatch!", false));
        }

        if (!user.isVerified()) {
            System.out.println("User is not verified");
            return ResponseEntity.status(401).body(new LoginResponseDTO(null, null, "User account is not verified!", false));
        }

        if (!user.isActive()) {
            System.out.println("User is not active");
            return ResponseEntity.status(401).body(new LoginResponseDTO(null, null, "User is not active!", false));
        }

        if (user.isSuspended()) {
            System.out.println("User is suspended");
            ReportRequest report = reportRepository.findByReportedUserAndStatus(user, Status.APPROVED);
            if (report != null && report.getTimestamp().isBefore(LocalDateTime.now())) {
                report.setStatus(Status.DELETED);
                reportRepository.save(report);
                System.out.println("Removed expired report for suspended user");
            }

            report = reportRepository.findByReportedUserAndStatus(user, Status.APPROVED);
            if (report != null) {
                return ResponseEntity.status(401).body(new LoginResponseDTO(null, null, "User is suspended!", false));
            }
        }

        // Generate token
        String token = JWTUtil.generateToken(user.getEmail());

        // Prepare User DTO
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getClass().getSimpleName());
        userDTO.setCity(user.getCity());

        // Prepare UserLoginDTO
        LoginResponseDTO userLoginDTO = new LoginResponseDTO();
        userLoginDTO.setToken(token);
        userLoginDTO.setUser(userDTO);
        userLoginDTO.setMessage("Login successful");
        userLoginDTO.setSuccess(true);

        return ResponseEntity.ok(userLoginDTO);
    }

    public ResponseEntity<ChangedPasswordDTO> changePassword(ChangePasswordDTO dto) {
        Optional<User> userOptional = userRepository.findById(dto.getId());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("User found with email: " + user.getEmail());
            System.out.println("Old password: " + user.getPassword() + ", given password: " + dto.getOldPassword());

            if (!dto.getOldPassword().equals(user.getPassword())) {
                ChangedPasswordDTO response = new ChangedPasswordDTO();
                response.setMessage("Old password is incorrect");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            user.setPassword(dto.getPassword());

            userRepository.save(user);

            ChangedPasswordDTO response = new ChangedPasswordDTO();
            response.setMessage("Password successfully changed");
            return ResponseEntity.ok(response);
        } else {
            ChangedPasswordDTO response = new ChangedPasswordDTO();
            response.setMessage("User not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
