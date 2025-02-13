package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.registration.RegisterSppDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterResponseDTO;
import edu.ftn.iss.eventplanner.entities.ServiceAndProductProvider;
import edu.ftn.iss.eventplanner.entities.User;
import edu.ftn.iss.eventplanner.repositories.ServiceAndProductProviderRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Service
public class ServiceAndProductProviderService {

    @Autowired
    private ServiceAndProductProviderRepository providerRepository;

    @Autowired
    private EmailService emailService;

    public ResponseEntity<RegisterResponseDTO> register(RegisterSppDTO dto) {
        System.out.println("ENTERED REGISTER");
        System.out.println("DTO: " + dto);
        try {
            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                return ResponseEntity.badRequest().body(new RegisterResponseDTO("Passwords do not match!", false));
            }

            if (providerRepository.findByEmail(dto.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body(new RegisterResponseDTO("Email already in use!", false));
            }

            String activationToken = UUID.randomUUID().toString();
            create(dto, activationToken);

            emailService.sendActivationEmail(dto.getEmail(), activationToken);

            return ResponseEntity.ok(new RegisterResponseDTO("Registration successful! Check your email to activate your account.", true));
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body(new RegisterResponseDTO("Failed to send activation email. Please try again later.", false));
        }
    }

    private void create(RegisterSppDTO dto, String activationToken) {
        ServiceAndProductProvider provider = new ServiceAndProductProvider();
        provider.setEmail(dto.getEmail());
        provider.setPassword(dto.getPassword());
        provider.setAddress(dto.getAddress());
        provider.setCity(dto.getCity());
        provider.setActive(false);  // for deactivation
        provider.setPhoneNumber(dto.getPhoneNumber());
        provider.setVerified(false);    // for account activation
        provider.setSuspended(false);
        provider.setActivationToken(activationToken);
        provider.setTokenCreationDate(LocalDateTime.now());
        provider.setCompanyName(dto.getCompanyName());
        provider.setDescription(dto.getCompanyDescription());
        provider.setPhotos(Collections.singletonList(dto.getPhoto()));

        providerRepository.save(provider);
    }

    public ResponseEntity<RegisterResponseDTO> activate(String token) {
        System.out.println("ENTERED ACTIVATE ---------------------------------------------------------------------------");
        ServiceAndProductProvider provider = providerRepository.findByActivationToken(token);
        if (provider == null) {
            return ResponseEntity.badRequest().body(new RegisterResponseDTO("Invalid or expired activation token!", false));
        }

        LocalDateTime tokenCreationDate = provider.getTokenCreationDate();
        if (tokenCreationDate.plusHours(24).isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(new RegisterResponseDTO("Activation token has expired. Please register again.", false));
        }

        provider.setActive(true);  // Mark as active
        provider.setVerified(true);  // Mark as verified
        provider.setActivationToken(null);
        provider.setTokenCreationDate(null);
        providerRepository.save(provider);

        return ResponseEntity.ok(new RegisterResponseDTO("Your email is verified successfully!", true));
    }
}
