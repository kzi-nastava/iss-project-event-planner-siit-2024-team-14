package edu.ftn.iss.eventplanner.services;
import edu.ftn.iss.eventplanner.dtos.invitations.InvitationRequestDTO;
import edu.ftn.iss.eventplanner.entities.Event;
import edu.ftn.iss.eventplanner.entities.Invitation;
import edu.ftn.iss.eventplanner.enums.Status;
import edu.ftn.iss.eventplanner.repositories.EventRepository;
import edu.ftn.iss.eventplanner.repositories.InvitationRepository;
import edu.ftn.iss.eventplanner.repositories.UserRepository;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
}
