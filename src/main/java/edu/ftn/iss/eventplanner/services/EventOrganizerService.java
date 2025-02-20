package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.get.GetOrganizerDTO;
import edu.ftn.iss.eventplanner.dtos.get.OrganizerDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterEoDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterResponseDTO;
import edu.ftn.iss.eventplanner.dtos.update.UpdateOrganizerDTO;
import edu.ftn.iss.eventplanner.dtos.update.UpdatedOrganizerDTO;
import edu.ftn.iss.eventplanner.entities.EventOrganizer;
import edu.ftn.iss.eventplanner.repositories.EventOrganizerRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventOrganizerService {

    @Autowired
    private EventOrganizerRepository organizerRepository;

    @Autowired
    private EmailService emailService;

    public ResponseEntity<RegisterResponseDTO> register(RegisterEoDTO dto) {
        try {
            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                return ResponseEntity.badRequest().body(new RegisterResponseDTO("Passwords do not match!", false));
            }

            if (organizerRepository.findByEmail(dto.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body(new RegisterResponseDTO("Email already in use!", false));
            }

            String activationToken = UUID.randomUUID().toString();
            create(dto, activationToken);

            emailService.sendActivationEmail(dto.getEmail(), activationToken, "EventOrganizer");

            return ResponseEntity.ok(new RegisterResponseDTO("Registration successful! Check your email to activate your account.", true));
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body(new RegisterResponseDTO("Failed to send activation email. Please try again later.", false));
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
        organizer.setProfilePhoto(dto.getPhoto());
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

        GetOrganizerDTO getOrganizerDTO = new GetOrganizerDTO();
        getOrganizerDTO.setMessage("ok");
        getOrganizerDTO.setOrganizer(organizerDTO);

        return ResponseEntity.ok(getOrganizerDTO);  // Return the DTO
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

            return updatedDTO; // Return the updated organizer DTO
        } else {
            throw new RuntimeException("Organizer with id " + userId + " not found");
        }
    }

}
