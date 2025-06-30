package edu.ftn.iss.eventplanner.services;
import edu.ftn.iss.eventplanner.dtos.homepage.EventDTO;
import edu.ftn.iss.eventplanner.dtos.invitations.EventWithInvitationsDTO;
import edu.ftn.iss.eventplanner.dtos.invitations.InvitationRequestDTO;
import edu.ftn.iss.eventplanner.dtos.invitations.InvitationResponseDTO;
import edu.ftn.iss.eventplanner.dtos.registration.QuickRegistrationDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterResponseDTO;
import edu.ftn.iss.eventplanner.entities.Event;
import edu.ftn.iss.eventplanner.entities.Invitation;
import edu.ftn.iss.eventplanner.entities.User;
import edu.ftn.iss.eventplanner.enums.PrivacyType;
import edu.ftn.iss.eventplanner.enums.Status;
import edu.ftn.iss.eventplanner.exceptions.ConflictException;
import edu.ftn.iss.eventplanner.repositories.EventRepository;
import edu.ftn.iss.eventplanner.repositories.InvitationRepository;
import edu.ftn.iss.eventplanner.repositories.UserRepository;
import jakarta.mail.MessagingException;
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
    private final EventService eventService;


    public InvitationService(InvitationRepository invitationRepository,
                             EventRepository eventRepository,
                             UserRepository userRepository,
                             EmailService emailService,
                             EventService eventService) {
        this.invitationRepository = invitationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.eventService = eventService;
    }

    /**
     * Creates invitations for a list of guest emails, sends an email with a proper link
     * (either for login or registration), and saves the invitation with PENDING status.
     */
    public List<Invitation> createInvitation(InvitationRequestDTO dto) {
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        List<Invitation> created = new ArrayList<>();

        for (String email : dto.getGuestEmails()) {
            // Skip if invitation already exists for this email and event
            if (invitationRepository.existsByGuestEmailAndEventId(email, event.getId())) continue;

            // Create and save invitation
            Invitation inv = new Invitation();
            inv.setGuestEmail(email.trim());
            inv.setEvent(event);
            inv.setStatus(Status.PENDING);
            Invitation saved = invitationRepository.save(inv);
            created.add(inv);

            // Check if user already exists
            boolean alreadyRegistered = userRepository.existsByEmail(email.trim());
            String frontendBaseUrl = "http://localhost:4200";

            // Build appropriate link depending on whether the user is registered
            String invitationLink;
            if (alreadyRegistered) {
                userRepository.findByEmail(email.trim()).ifPresent(user -> {
                    if (user.getJoinedEvents() == null) {
                        user.setJoinedEvents(new ArrayList<>());
                    }

                    if (!user.getJoinedEvents().contains(event)) {
                        user.getJoinedEvents().add(event);
                        userRepository.save(user);
                    }
                });

                invitationLink = frontendBaseUrl + "/invitation/login?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8) + "&eventId=" + event.getId();
                inv.setStatus(Status.APPROVED);
                invitationRepository.save(inv);
            } else {
                invitationLink = frontendBaseUrl + "/invitation/register?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8) + "&eventId=" + event.getId();
            }

            // Send invitation email
            emailService.sendInvitationEmail(email, event, invitationLink);
        }

        return created;
    }

    /**
     * Updates the status of a specific invitation (e.g., to APPROVED or REJECTED).
     */
    public void updateInvitationStatus(Integer invitationId, Status status) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));
        invitation.setStatus(status);
        invitationRepository.save(invitation);
    }

    /**
     * Registers a user using an invitation. If successful, links the invitation to the newly created user,
     * sets status to APPROVED, and sends an activation email.
     */
    public ResponseEntity<RegisterResponseDTO> registerUserViaInvitation(QuickRegistrationDTO dto) {
        // Validate password match
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        // Check if user already exists
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("User with this email already exists.");
        }

        // Generate activation token
        String activationToken = UUID.randomUUID().toString();

        // Create new user with inactive and unverified status
        User newUser = new User();
        newUser.setEmail(dto.getEmail());
        newUser.setPassword(dto.getPassword());
        newUser.setActive(false);
        newUser.setVerified(false);
        newUser.setSuspended(false);
        newUser.setActivationToken(activationToken);
        newUser.setTokenCreationDate(LocalDateTime.now());

        userRepository.save(newUser);

        // Update invitation status if invitation exists
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found."));

        Optional<Invitation> invitationOpt = invitationRepository.findByGuestEmailAndEventId(dto.getEmail(), event.getId());
        invitationOpt.ifPresent(invitation -> {
            invitation.setStatus(Status.APPROVED);
            invitationRepository.save(invitation);
        });

        if (newUser.getJoinedEvents() == null) {
            newUser.setJoinedEvents(new ArrayList<>());
        }

        if (!newUser.getJoinedEvents().contains(event)) {
            newUser.getJoinedEvents().add(event);
            userRepository.save(newUser);
        }

        // Send activation email
        try {
            emailService.sendActivationEmail(dto.getEmail(), activationToken, "User");
        } catch (MessagingException e) {
            System.err.println("⚠️ Email could not be sent, but registration succeeded: " + e.getMessage());
        }

        return ResponseEntity.ok(new RegisterResponseDTO("Registration successful! Check your email to activate your account.", true));
    }

    /**
     * Activates the user account using the provided token. Validates token expiration (24h).
     */
    public ResponseEntity<RegisterResponseDTO> activate(String token) {
        User user = userRepository.findByActivationToken(token);
        if (user == null) {
            return ResponseEntity.badRequest().body(new RegisterResponseDTO("Invalid or expired activation token!", false));
        }

        // Check if token has expired (valid for 24h)
        LocalDateTime tokenCreationDate = user.getTokenCreationDate();
        if (tokenCreationDate.plusHours(24).isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(new RegisterResponseDTO("Activation token has expired. Please register again.", false));
        }

        // Activate and verify user
        user.setActive(true);
        user.setVerified(true);
        user.setActivationToken(null);
        user.setTokenCreationDate(null);
        userRepository.save(user);

        return ResponseEntity.ok(new RegisterResponseDTO("Your email is verified successfully!", true));
    }

    public List<EventWithInvitationsDTO> getInvitationsByOrganizer(Integer organizerId) {
        List<Event> events = eventRepository.findByOrganizerId(organizerId).stream()
                .filter(event -> event.getPrivacyType() == PrivacyType.CLOSED)
                .toList();

        return events.stream().map(event -> {
            List<Invitation> invitations = invitationRepository.findByEvent(event);
            List<InvitationResponseDTO> invitationDTOs = invitations.stream()
                    .map(inv -> new InvitationResponseDTO(inv.getGuestEmail(), inv.getStatus()))
                    .toList();

            EventDTO eventDTO = eventService.getEventById(event.getId());

            return new EventWithInvitationsDTO(eventDTO, invitationDTOs);
        }).toList();
    }
}
