package edu.ftn.iss.eventplanner.controllers;
import edu.ftn.iss.eventplanner.dtos.invitations.EventWithInvitationsDTO;
import edu.ftn.iss.eventplanner.dtos.invitations.InvitationRequestDTO;
import edu.ftn.iss.eventplanner.dtos.registration.QuickRegistrationDTO;
import edu.ftn.iss.eventplanner.dtos.registration.RegisterResponseDTO;
import edu.ftn.iss.eventplanner.entities.Invitation;
import edu.ftn.iss.eventplanner.enums.Status;
import edu.ftn.iss.eventplanner.services.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invitations")
public class InvitationController {

    @Autowired
    private InvitationService invitationService;

    @PostMapping("/bulk")
    public ResponseEntity<?> sendBulkInvitations(@RequestBody InvitationRequestDTO dto) {
        System.out.println(dto);
        List<Invitation> created = invitationService.createInvitation(dto);

        return ResponseEntity.ok(Map.of("message", created.size() + " invitations sent."));
    }

    @PutMapping("/{invitationId}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Integer invitationId,
            @RequestParam Status status
    ) {
        invitationService.updateInvitationStatus(invitationId, status);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registerAndFollowEvent(@RequestBody QuickRegistrationDTO dto) {
        return invitationService.registerUserViaInvitation(dto);
    }

    @GetMapping("/activate")
    public ResponseEntity<RegisterResponseDTO> activate(@RequestParam("token") String token) {
        return invitationService.activate(token);
    }

    @GetMapping("/by-organizer/{organizerId}")
    public ResponseEntity<List<EventWithInvitationsDTO>> getInvitationsByOrganizer(@PathVariable Integer organizerId) {
        List<EventWithInvitationsDTO> result = invitationService.getInvitationsByOrganizer(organizerId);
        return ResponseEntity.ok(result);
    }
}
