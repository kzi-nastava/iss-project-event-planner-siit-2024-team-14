package edu.ftn.iss.eventplanner.services;
import edu.ftn.iss.eventplanner.dtos.invitations.InvitationRequestDTO;
import edu.ftn.iss.eventplanner.dtos.registration.QuickRegistrationDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterResponseDTO;
import edu.ftn.iss.eventplanner.entities.Event;
import edu.ftn.iss.eventplanner.entities.EventOrganizer;
import edu.ftn.iss.eventplanner.entities.Invitation;
import edu.ftn.iss.eventplanner.entities.User;
import edu.ftn.iss.eventplanner.enums.Status;
import edu.ftn.iss.eventplanner.exceptions.ConflictException;
import edu.ftn.iss.eventplanner.repositories.EventRepository;
import edu.ftn.iss.eventplanner.repositories.InvitationRepository;
import edu.ftn.iss.eventplanner.repositories.UserRepository;
import jakarta.mail.MessagingException;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public InvitationService(InvitationRepository invitationRepository, EventRepository eventRepository, UserRepository userRepository, EmailService emailService) {
        this.invitationRepository = invitationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public List<Invitation> createInvitation(InvitationRequestDTO dto) {
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        List<Invitation> created = new ArrayList<>();

        for (String email : dto.getGuestEmails()) {
            if (invitationRepository.existsByGuestEmailAndEventId(email, event.getId())) continue;

            Invitation inv = new Invitation();
            inv.setGuestEmail(email.trim());
            inv.setEvent(event);
            inv.setStatus(Status.PENDING);

            Invitation saved = invitationRepository.save(inv);
            created.add(inv);

            boolean alreadyRegistered = userRepository.existsByEmail(email.trim());
            String frontendBaseUrl = "http://localhost:4200";

            String invitationLink;
            if (alreadyRegistered) {
                invitationLink = frontendBaseUrl + "/invitation/login?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8) + "&eventId=" + event.getId();
            } else {
                invitationLink = frontendBaseUrl + "/invitation/register?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8) + "&eventId=" + event.getId();
            }

            emailService.sendInvitationEmail(email, event.getName(), invitationLink);
        }
        return created;
    }

    public void updateInvitationStatus(Integer invitationId, Status status) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));
        invitation.setStatus(status);
        invitationRepository.save(invitation);
    }

    public ResponseEntity<RegisterResponseDTO> registerUserViaInvitation(QuickRegistrationDTO dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("User with this email already exists.");
        }

        String activationToken = UUID.randomUUID().toString();

        User newUser = new User();
        newUser.setEmail(dto.getEmail());
        newUser.setPassword(dto.getPassword()); // 🔐 hashing je preporučen ovde
        newUser.setActive(false);
        newUser.setVerified(false);
        newUser.setSuspended(false);
        newUser.setActivationToken(activationToken);
        newUser.setTokenCreationDate(LocalDateTime.now());

        userRepository.save(newUser); // ✅ token i datum se čuvaju

        // poveži pozivnicu
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found."));

        Optional<Invitation> invitationOpt = invitationRepository.findByGuestEmailAndEventId(dto.getEmail(), event.getId());
        invitationOpt.ifPresent(invitation -> {
            invitation.setStatus(Status.APPROVED);
            invitationRepository.save(invitation);
        });

        try {
            emailService.sendActivationEmail(dto.getEmail(), activationToken, "User");
        } catch (MessagingException e) {
            System.err.println("⚠️ Email could not be sent, but registration succeeded: " + e.getMessage());
        }

        return ResponseEntity.ok(new RegisterResponseDTO("Registration successful! Check your email to activate your account.", true));
    }

    public ResponseEntity<RegisterResponseDTO> activate(String token) {
        User user = userRepository.findByActivationToken(token);
        if (user == null) {
            return ResponseEntity.badRequest().body(new RegisterResponseDTO("Invalid or expired activation token!", false));
        }

        LocalDateTime tokenCreationDate = user.getTokenCreationDate();
        if (tokenCreationDate.plusHours(24).isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(new RegisterResponseDTO("Activation token has expired. Please register again.", false));
        }

        user.setActive(true);  // Mark as active
        user.setVerified(true);  // Mark as verified
        user.setActivationToken(null);
        user.setTokenCreationDate(null);
        userRepository.save(user);

        return ResponseEntity.ok(new RegisterResponseDTO("Your email is verified successfully!", true));
    }
}
