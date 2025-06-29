package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.getUsers.GetProviderDTO;
import edu.ftn.iss.eventplanner.dtos.getUsers.ProviderDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterSppDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterResponseDTO;
import edu.ftn.iss.eventplanner.dtos.reports.ViewProviderProfileDTO;
import edu.ftn.iss.eventplanner.dtos.updateUsers.UpdateProviderDTO;
import edu.ftn.iss.eventplanner.dtos.updateUsers.UpdateToProviderDTO;
import edu.ftn.iss.eventplanner.dtos.updateUsers.UpdatedProviderDTO;
import edu.ftn.iss.eventplanner.entities.ServiceAndProductProvider;
import edu.ftn.iss.eventplanner.entities.User;
import edu.ftn.iss.eventplanner.exceptions.NotFoundException;
import edu.ftn.iss.eventplanner.repositories.ServiceAndProductProviderRepository;
import edu.ftn.iss.eventplanner.repositories.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Service
public class ServiceAndProductProviderService {

    @Autowired
    private ServiceAndProductProviderRepository providerRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

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

            if (dto.getPhotos() == null) {
                dto.setPhotos(new ArrayList<>());
            }

            if (photos != null && !photos.isEmpty()) {
                String uploadDir = "src/main/resources/static/photos/";
                Files.createDirectories(Paths.get(uploadDir));

                // Loop through the photos, limit to 3, and save them
                for (int i = 0; i < Math.min(photos.size(), 3); i++) {
                    MultipartFile photo = photos.get(i);
                    if (photo != null && !photo.isEmpty()) {
                        String photoFilename = dto.getEmail() + (i + 1) + ".png";
                        Path filePath = Paths.get(uploadDir + photoFilename);

                        Files.write(filePath, photo.getBytes());  // Write photo to file

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
    public ViewProviderProfileDTO getProviderById(Integer id) {
        return providerRepository.findById(id)
                .map(provider -> new ViewProviderProfileDTO(
                        provider.getId(),
                        provider.getCompanyName(),
                        provider.getDescription(),
                        provider.getEmail(),
                        provider.getAddress(),
                        provider.getCity(),
                        provider.getPhoneNumber(),
                        provider.getPhotos().get(0)
                ))
                .orElse(null);
    }

    public ServiceAndProductProvider getById(int id) {
        return providerRepository.findByIdAsOptional(id)
                .orElseThrow(() -> new NotFoundException("Provider not found"));
    }


    public ResponseEntity<GetProviderDTO> get(int id) {
        ServiceAndProductProvider provider = providerRepository.findById(id);

        if (provider == null) {
            return ResponseEntity.notFound().build();
        }

        ProviderDTO providerDTO = new ProviderDTO();
        providerDTO.setId(provider.getId());
        providerDTO.setEmail(provider.getEmail());
        providerDTO.setName(provider.getCompanyName());
        providerDTO.setDescription(provider.getDescription());
        providerDTO.setCity(provider.getCity());
        providerDTO.setAddress(provider.getAddress());
        providerDTO.setPhoneNumber(String.valueOf(provider.getPhoneNumber()));
        providerDTO.setRole("ServiceAndProductProvider");
        providerDTO.setPhotos(providerDTO.getPhotos());

        GetProviderDTO getProviderDTO = new GetProviderDTO();
        getProviderDTO.setMessage("ok");
        getProviderDTO.setProvider(providerDTO);

        return ResponseEntity.ok(getProviderDTO);
    }

    public List<String> getPhotos(int id) {
        ServiceAndProductProvider provider = providerRepository.findById(id);

        if (provider == null || provider.getPhotos().isEmpty()) {
            return Collections.emptyList(); // Return empty list if no photos found
        }

        // Convert filenames into accessible URLs
        return provider.getPhotos().stream()
                .map(photo -> "http://localhost:8080/photos/" + photo) // Change based on deployment
                .collect(Collectors.toList());
    }


    public UpdatedProviderDTO update(Integer userId, UpdateProviderDTO updateDTO) {
        Optional<ServiceAndProductProvider> providerOptional = providerRepository.findById(userId);

        if (providerOptional.isPresent()) {
            ServiceAndProductProvider provider = providerOptional.get();

            provider.setDescription(updateDTO.getDescription());
            provider.setAddress(updateDTO.getAddress());
            provider.setCity(updateDTO.getCity());
            provider.setPhoneNumber(Integer.parseInt(updateDTO.getPhoneNumber()));

            ServiceAndProductProvider updatedProvider = providerRepository.save(provider);

            UpdatedProviderDTO updatedDTO = new UpdatedProviderDTO();
            updatedDTO.setDescription(updatedProvider.getDescription());
            updatedDTO.setAddress(updatedProvider.getAddress());
            updatedDTO.setCity(updatedProvider.getCity());
            updatedDTO.setPhoneNumber(Integer.parseInt(updateDTO.getPhoneNumber()));

            return updatedDTO;
        } else {
            throw new RuntimeException("Provider with id " + userId + " not found");
        }
    }

    public ResponseEntity<RegisterResponseDTO> updatePhoto(int userId, MultipartFile photo, int photoIndex) {
        try {
            ServiceAndProductProvider provider = providerRepository.findById(userId);

            if (provider == null) {
                return ResponseEntity.badRequest().body(new RegisterResponseDTO("Provider not found!", false));
            }

            // Get the list of photos
            List<String> photos = provider.getPhotos();

            // Check if the photoIndex is valid
            if (photoIndex < 0 || photoIndex >= photos.size()) {
                return ResponseEntity.badRequest().body(new RegisterResponseDTO("Invalid photo index!", false));
            }

            // Get the current photo to delete
            String oldPhoto = photos.get(photoIndex);  // Use the correct method to get the photo from the list
            if (oldPhoto != null) {
                Path oldPhotoPath = Paths.get("src/main/resources/static/photos/" + oldPhoto);
                Files.deleteIfExists(oldPhotoPath);  // Delete the old photo if it exists
            }

            int index = photoIndex + 1;
            // Generate a new photo filename, using the email + photoIndex to make it unique
            String photoFilename = provider.getEmail()  + index + ".png";  // Unique filename per index
            String uploadDir = "src/main/resources/static/photos/";

            // Ensure the directory exists
            Path filePath = Paths.get(uploadDir + photoFilename);
            Files.createDirectories(filePath.getParent());

            // Write the new photo to the file system
            Files.write(filePath, photo.getBytes());

            // Update the photos list with the new photo URL (add the new photo filename to the list)
            photos.set(photoIndex, photoFilename);  // Update the photo at the given index

            // Save the provider entity with the updated photos list
            provider.setPhotos(photos);  // Update the list of photos in the provider
            providerRepository.save(provider);

            return ResponseEntity.ok(new RegisterResponseDTO("Photo updated successfully!", true));
        } catch (IOException ex) {
            return ResponseEntity.status(500).body(new RegisterResponseDTO("Failed to update photo!", false));
        }
    }

    public ResponseEntity<RegisterResponseDTO> deactivate(int id) {
        ServiceAndProductProvider provider = providerRepository.findById(id);

        if (provider == null) {
            return ResponseEntity.badRequest().body(new RegisterResponseDTO("Provider not found!", false));
        }

        if (provider.getReservedSolutions().isEmpty()) {
            provider.setActive(false);
        }
        providerRepository.save(provider);
        return ResponseEntity.ok(new RegisterResponseDTO("Provider deactivated successfully!", true));
    }

    @Transactional
    public void upgradeUserToProvider(UpdateToProviderDTO dto) {
        User existingUser = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + dto.getEmail()));

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        ServiceAndProductProvider provider = new ServiceAndProductProvider();

        provider.setId(existingUser.getId());
        provider.setEmail(existingUser.getEmail());
        provider.setPassword(existingUser.getPassword());
        provider.setAddress(dto.getAddress());
        provider.setCity(dto.getCity());
        provider.setPhoneNumber(Integer.parseInt(dto.getPhoneNumber()));
        provider.setActive(existingUser.isActive());
        provider.setVerified(existingUser.isVerified());
        provider.setSuspended(existingUser.isSuspended());
        provider.setMuted(existingUser.isMuted());
        provider.setBlockedUsers(existingUser.getBlockedUsers());
        provider.setFavoriteSolutions(existingUser.getFavoriteSolutions());
        provider.setFavouriteEvents(existingUser.getFavouriteEvents());
        provider.setJoinedEvents(existingUser.getJoinedEvents());
        provider.setActivationToken(existingUser.getActivationToken());
        provider.setTokenCreationDate(existingUser.getTokenCreationDate());

        provider.setCompanyName(dto.getCompanyName());
        provider.setDescription(dto.getCompanyDescription());
        provider.setPhotos(dto.getPhotos());

        userRepository.delete(existingUser);
        userRepository.save(provider);
    }
}

