package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.getUsers.GetOrganizerDTO;
import edu.ftn.iss.eventplanner.dtos.getUsers.OrganizerDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterEoDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterResponseDTO;
import edu.ftn.iss.eventplanner.dtos.reports.ViewOrganizerProfileDTO;
import edu.ftn.iss.eventplanner.dtos.updateUsers.UpdateOrganizerDTO;
import edu.ftn.iss.eventplanner.dtos.updateUsers.UpdateToOrganizerDTO;
import edu.ftn.iss.eventplanner.dtos.updateUsers.UpdatedOrganizerDTO;
import edu.ftn.iss.eventplanner.entities.Event;
import edu.ftn.iss.eventplanner.entities.EventOrganizer;
import edu.ftn.iss.eventplanner.entities.User;
import edu.ftn.iss.eventplanner.exceptions.NotFoundException;
import edu.ftn.iss.eventplanner.repositories.EventOrganizerRepository;
import edu.ftn.iss.eventplanner.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventOrganizerService {

    @Autowired
    private EventOrganizerRepository organizerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @PersistenceContext
    private EntityManager entityManager;

    public ResponseEntity<RegisterResponseDTO> register(RegisterEoDTO dto, MultipartFile photo) {
        try {
            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                return ResponseEntity.badRequest().body(new RegisterResponseDTO("Passwords do not match!", false));
            }

            if (organizerRepository.findByEmail(dto.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body(new RegisterResponseDTO("Email already in use!", false));
            }

            try {
                int parsedPhoneNumber = Integer.parseInt(String.valueOf(dto.getPhoneNumber()));
                dto.setPhoneNumber(parsedPhoneNumber);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body(new RegisterResponseDTO("Invalid phone number format!", false));
            }

            // If photo is provided, save it
            if (photo != null && !photo.isEmpty()) {
                String photoFilename = dto.getEmail() + ".png"; // Name photo as email.png
                String uploadDir = "src/main/resources/static/profile-photos/";
                Path filePath = Paths.get(uploadDir + photoFilename);

                Files.createDirectories(filePath.getParent());  // Create directories if they don't exist

                Files.write(filePath, photo.getBytes());  // Write photo to file

                dto.setPhoto(photoFilename);  // Set photo filename
            }

            // Generate activation token and send activation email
            String activationToken = UUID.randomUUID().toString();
            create(dto, activationToken);
            emailService.sendActivationEmail(dto.getEmail(), activationToken, "EventOrganizer");

            return ResponseEntity.ok(new RegisterResponseDTO("Registration successful! Check your email to activate your account.", true));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(new RegisterResponseDTO("Failed to upload photo!", false));
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body(new RegisterResponseDTO("Failed to send activation email!", false));
        }
    }

    private void create(RegisterEoDTO dto, String activationToken) {
        EventOrganizer organizer = new EventOrganizer();
        organizer.setEmail(dto.getEmail());
        organizer.setPassword(dto.getPassword());
        organizer.setName(dto.getName());
        organizer.setSurname(dto.getSurname());
        organizer.setAddress(dto.getAddress());
        organizer.setCity(dto.getCity());
        organizer.setPhoneNumber(dto.getPhoneNumber());
        organizer.setProfilePhoto(String.valueOf(dto.getPhoto()));
        organizer.setActive(false);         // for deactivation
        organizer.setVerified(false);       // for account activation
        organizer.setSuspended(false);
        organizer.setActivationToken(activationToken);
        organizer.setTokenCreationDate(LocalDateTime.now());

        organizerRepository.save(organizer);
    }

    public ResponseEntity<RegisterResponseDTO> activate(String token) {
        EventOrganizer organizer = organizerRepository.findByActivationToken(token);
        if (organizer == null) {
            return ResponseEntity.badRequest().body(new RegisterResponseDTO("Invalid or expired activation token!", false));
        }

        LocalDateTime tokenCreationDate = organizer.getTokenCreationDate();
        if (tokenCreationDate.plusHours(24).isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(new RegisterResponseDTO("Activation token has expired. Please register again.", false));
        }

        organizer.setActive(true);  // Mark as active
        organizer.setVerified(true);  // Mark as verified
        organizer.setActivationToken(null);
        organizer.setTokenCreationDate(null);
        organizerRepository.save(organizer);

        return ResponseEntity.ok(new RegisterResponseDTO("Your email is verified successfully!", true));
    }

    public ResponseEntity<GetOrganizerDTO> get(int id) {
        EventOrganizer organizer = organizerRepository.findById(id);

        if (organizer == null) {
            return ResponseEntity.notFound().build();  // If organizer is not found
        }

        // Map Organizer to GetOrganizerDTO
        OrganizerDTO organizerDTO = new OrganizerDTO();
        organizerDTO.setId(organizer.getId());
        organizerDTO.setEmail(organizer.getEmail());
        organizerDTO.setName(organizer.getName());
        organizerDTO.setSurname(organizer.getSurname());
        organizerDTO.setCity(organizer.getCity());
        organizerDTO.setAddress(organizer.getAddress());
        organizerDTO.setPhoneNumber(String.valueOf(organizer.getPhoneNumber()));
        organizerDTO.setRole("EventOrganizer");
        organizerDTO.setProfilePhoto("profile-photos/" + organizer.getProfilePhoto());

        GetOrganizerDTO getOrganizerDTO = new GetOrganizerDTO();
        getOrganizerDTO.setMessage("ok");
        getOrganizerDTO.setOrganizer(organizerDTO);

        return ResponseEntity.ok(getOrganizerDTO);  // Return the DTO
    }

    public ResponseEntity<Resource> getProfilePhoto(int id) throws MalformedURLException {
        EventOrganizer organizer = organizerRepository.findById(id);

        System.out.println("MAIL" + organizer.getEmail());

        Path path = Paths.get("src/main/resources/static/profile-photos/" + organizer.getEmail() + ".png");
        Resource resource = new UrlResource(path.toUri());

        // Return the image as a Resource
        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource); // Adjust type if JPEG
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public UpdatedOrganizerDTO update(Integer userId, UpdateOrganizerDTO updateDTO) {
        Optional<EventOrganizer> organizerOptional = organizerRepository.findById(userId);

        if (organizerOptional.isPresent()) {
            EventOrganizer organizer = organizerOptional.get();

            organizer.setName(updateDTO.getName());
            organizer.setSurname(updateDTO.getSurname());
            organizer.setAddress(updateDTO.getAddress());
            organizer.setCity(updateDTO.getCity());
            organizer.setPhoneNumber(Integer.parseInt(updateDTO.getPhoneNumber()));

            EventOrganizer updatedOrganizer = organizerRepository.save(organizer);

            UpdatedOrganizerDTO updatedDTO = new UpdatedOrganizerDTO();
            updatedDTO.setName(updatedOrganizer.getName());
            updatedDTO.setSurname(updatedOrganizer.getSurname());
            updatedDTO.setAddress(updatedOrganizer.getAddress());
            updatedDTO.setCity(updatedOrganizer.getCity());
            updatedDTO.setPhoneNumber(Integer.parseInt(updateDTO.getPhoneNumber()));

            return updatedDTO;
        } else {
            throw new RuntimeException("Organizer with id " + userId + " not found");
        }
    }

    public ResponseEntity<RegisterResponseDTO> updateProfilePhoto(int userId, MultipartFile photo) {
        System.out.println("ENTERED updateProfilePhoto() ---------------------------------");
        try {
            EventOrganizer organizer = organizerRepository.findById(userId);

            if (organizer == null) {
                return ResponseEntity.status(404).body(new RegisterResponseDTO("Organizer not found!", false));
            }

            // Delete the old photo if it exists (optional, to avoid unnecessary storage)
            String oldPhoto = organizer.getProfilePhoto();
            if (oldPhoto != null) {
                Path oldPhotoPath = Paths.get("src/main/resources/static/profile-photos/" + oldPhoto);
                Files.deleteIfExists(oldPhotoPath);
            }

            // Generate a new photo filename (you can use a UUID to ensure unique file names)
            String photoFilename = organizer.getEmail() + ".png";  // Or generate a unique filename
            String uploadDir = "src/main/resources/static/profile-photos/";

            // Ensure the directory exists
            Path filePath = Paths.get(uploadDir + photoFilename);
            Files.createDirectories(filePath.getParent());

            // Save the new file
            Files.write(filePath, photo.getBytes());

            // Update the organizer with the new photo filename
            organizer.setProfilePhoto(photoFilename);
            organizerRepository.save(organizer);

            return ResponseEntity.ok(new RegisterResponseDTO("Profile photo updated successfully!", true));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(new RegisterResponseDTO("Failed to update profile photo!", false));
        }
    }

    public ViewOrganizerProfileDTO getOrganizerById(Integer id) {
        return organizerRepository.findById(id)
                .map(organizer -> new ViewOrganizerProfileDTO(
                        organizer.getId(),
                        organizer.getName(),
                        organizer.getSurname(),
                        organizer.getEmail(),
                        organizer.getAddress(),
                        organizer.getCity(),
                        organizer.getPhoneNumber(),
                        "profile-photos/" + organizer.getProfilePhoto()
                  ))
                .orElse(null);
    }

    public ResponseEntity<RegisterResponseDTO> deactivate(int id) {
        EventOrganizer organizer = organizerRepository.findById(id);

        if (organizer == null) {
            return ResponseEntity.status(404).body(new RegisterResponseDTO("Organizer not found!", false));
        }

        if (organizer.getMyEvents().isEmpty()) {
            organizer.setActive(false);
        }
        organizerRepository.save(organizer);
        return ResponseEntity.ok(new RegisterResponseDTO("Organizer deactivated successfully!", true));
    }

    @Transactional
    public void upgradeUserToOrganizer(UpdateToOrganizerDTO dto, MultipartFile photo) throws IOException {
        User existingUser = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + dto.getEmail()));

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        Integer userId = existingUser.getId();

        userRepository.delete(existingUser);
        entityManager.flush();

        EventOrganizer organizer = new EventOrganizer();
        organizer.setId(userId);
        organizer.setEmail(existingUser.getEmail());
        organizer.setPassword(existingUser.getPassword());
        organizer.setAddress(dto.getAddress());
        organizer.setCity(dto.getCity());
        organizer.setPhoneNumber(Integer.parseInt(dto.getPhoneNumber()));
        organizer.setActive(existingUser.isActive());
        organizer.setVerified(existingUser.isVerified());
        organizer.setSuspended(existingUser.isSuspended());
        organizer.setMuted(existingUser.isMuted());
        organizer.setBlockedUsers(existingUser.getBlockedUsers());
        organizer.setFavoriteSolutions(existingUser.getFavoriteSolutions());
        organizer.setFavouriteEvents(existingUser.getFavouriteEvents());
        organizer.setJoinedEvents(existingUser.getJoinedEvents());
        organizer.setActivationToken(existingUser.getActivationToken());
        organizer.setTokenCreationDate(existingUser.getTokenCreationDate());

        organizer.setName(dto.getName());
        organizer.setSurname(dto.getSurname());

        // Save profile photo
        if (photo != null && !photo.isEmpty()) {
            String uploadDir = "src/main/resources/static/profile-photos/";
            Files.createDirectories(Paths.get(uploadDir));
            String photoFilename = dto.getEmail() + "_profile.png";
            Path filePath = Paths.get(uploadDir + photoFilename);
            Files.write(filePath, photo.getBytes());
            organizer.setProfilePhoto(photoFilename);
        }

        userRepository.save(organizer);
    }


    public List<Event> getFavoriteEventsByUserId(Integer userId) {
        System.out.println("ENTERED getFavoriteEventsByUserId in EventOrganizerService");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

        System.out.println("user.getFavouriteEvents(): " + user.getFavouriteEvents());

        return user.getFavouriteEvents();
    }

    public boolean toggleFavoriteEvent(Integer userId, Integer eventId) {
        System.out.println("ENTERED toggleFavoriteEvent in EventOrganizerService");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

        Event event = entityManager.find(Event.class, eventId);
        if (event == null) {
            throw new NotFoundException("Event not found with ID: " + eventId);
        }

        boolean isFavorite;

        if (user.getFavouriteEvents().contains(event)) {
            user.getFavouriteEvents().remove(event);
            isFavorite = false;
            System.out.println("Removed event " + eventId + " from favorites");
        } else {
            user.getFavouriteEvents().add(event);
            isFavorite = true;
            System.out.println("Added event " + eventId + " to favorites");
        }

        userRepository.save(user);
        return isFavorite;
    }

}
