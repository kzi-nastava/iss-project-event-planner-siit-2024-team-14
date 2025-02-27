package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.registration.RegisterSppDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterResponseDTO;
import edu.ftn.iss.eventplanner.entities.ServiceAndProductProvider;
import edu.ftn.iss.eventplanner.repositories.ServiceAndProductProviderRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ServiceAndProductProviderService {

    @Autowired
    private ServiceAndProductProviderRepository providerRepository;

    @Autowired
    private EmailService emailService;

    public ResponseEntity<RegisterResponseDTO> register(RegisterSppDTO dto, List<MultipartFile> photos) {
        try {
            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                return ResponseEntity.badRequest().body(new RegisterResponseDTO("Passwords do not match!", false));
            }

            if (providerRepository.findByEmail(dto.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body(new RegisterResponseDTO("Email already in use!", false));
            }

            try {
                int parsedPhoneNumber = Integer.parseInt(String.valueOf(dto.getPhoneNumber()));
                dto.setPhoneNumber(parsedPhoneNumber);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body(new RegisterResponseDTO("Invalid phone number format!", false));
            }

            // Initialize the photos list if it's not already set
            if (dto.getPhotos() == null) {
                dto.setPhotos(new ArrayList<>());
            }

            // If photos are provided, save them (maximum of 3 photos)
            if (photos != null && !photos.isEmpty()) {
                String uploadDir = "src/main/resources/static/photos/";
                Files.createDirectories(Paths.get(uploadDir));  // Create directories if they don't exist

                // Loop through the photos, limit to 3, and save them
                for (int i = 0; i < Math.min(photos.size(), 3); i++) {
                    MultipartFile photo = photos.get(i);
                    if (photo != null && !photo.isEmpty()) {
                        String photoFilename = dto.getEmail() + (i + 1) + ".png"; // Name photo as email1.png, email2.png, email3.png
                        Path filePath = Paths.get(uploadDir + photoFilename);

                        Files.write(filePath, photo.getBytes());  // Write photo to file

                        // Add the filename to the list of photos in the DTO
                        dto.getPhotos().add(photoFilename);
                    }
                }
            }

            String activationToken = UUID.randomUUID().toString();
            create(dto, activationToken);
            emailService.sendActivationEmail(dto.getEmail(), activationToken, "ServiceAndProductProvider");

            return ResponseEntity.ok(new RegisterResponseDTO("Registration successful! Check your email to activate your account.", true));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(new RegisterResponseDTO("Failed to upload photo(s)!", false));
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body(new RegisterResponseDTO("Failed to send activation email!", false));
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
        provider.setPhotos(dto.getPhotos());

        providerRepository.save(provider);
    }

    public ResponseEntity<RegisterResponseDTO> activate(String token) {
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

    // get
    // getPhotos
    // update
    // updatePhotos
}
