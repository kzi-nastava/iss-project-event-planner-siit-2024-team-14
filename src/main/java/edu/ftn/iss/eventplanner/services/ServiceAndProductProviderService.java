package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.GetProviderDTO;
import edu.ftn.iss.eventplanner.dtos.get.ProviderDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterSppDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterResponseDTO;
import edu.ftn.iss.eventplanner.dtos.update.UpdateProviderDTO;
import edu.ftn.iss.eventplanner.dtos.update.UpdatedProviderDTO;
import edu.ftn.iss.eventplanner.entities.ServiceAndProductProvider;
import edu.ftn.iss.eventplanner.repositories.ServiceAndProductProviderRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public ResponseEntity<List<Resource>> getPhotos(int id) throws MalformedURLException {
        ServiceAndProductProvider provider = providerRepository.findById(id);
        List<String> photos = provider.getPhotos();
        List<Resource> photoResources = new ArrayList<>();

        try {
            for (String photo : photos) {
                Path path = Paths.get("src/main/resources/static/photos/" + photo);
                Resource resource = new UrlResource(path.toUri());

                if (resource.exists() || resource.isReadable()) {
                    photoResources.add(resource); // Add all readable resources to the list
                    System.out.println(resource);
                }
            }

            if (photoResources.isEmpty()) {
                return ResponseEntity.notFound().build(); // Return 404 if no photos are found
            }

            return ResponseEntity.ok().body(photoResources);

        } catch (MalformedURLException e) {
            return ResponseEntity.status(500).body(null); // Return internal server error in case of malformed URL
        }
    }



    public UpdatedProviderDTO update(Integer userId, UpdateProviderDTO updateDTO) {
        Optional<ServiceAndProductProvider> providerOptional = providerRepository.findById(userId);

        if (providerOptional.isPresent()) {
            ServiceAndProductProvider provider = providerOptional.get();

            provider.setCompanyName(updateDTO.getName());
            provider.setDescription(updateDTO.getDescription());
            provider.setAddress(updateDTO.getAddress());
            provider.setCity(updateDTO.getCity());
            provider.setPhoneNumber(Integer.parseInt(updateDTO.getPhoneNumber()));

            ServiceAndProductProvider updatedProvider = providerRepository.save(provider);

            UpdatedProviderDTO updatedDTO = new UpdatedProviderDTO();
            updatedDTO.setName(updatedProvider.getCompanyName());
            updatedDTO.setDescription(updatedProvider.getDescription());
            updatedDTO.setAddress(updatedProvider.getAddress());
            updatedDTO.setCity(updatedProvider.getCity());
            updatedDTO.setPhoneNumber(Integer.parseInt(updateDTO.getPhoneNumber()));

            return updatedDTO;
        } else {
            throw new RuntimeException("Provider with id " + userId + " not found");
        }
    }

    // updatePhotos
}

