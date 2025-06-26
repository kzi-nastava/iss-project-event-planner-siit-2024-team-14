package edu.ftn.iss.eventplanner.controllers;
import edu.ftn.iss.eventplanner.dtos.invitations.InvitationRequestDTO;
import edu.ftn.iss.eventplanner.dtos.invitations.InvitationResponseDTO;
import edu.ftn.iss.eventplanner.entities.Invitation;
import edu.ftn.iss.eventplanner.enums.Status;
import edu.ftn.iss.eventplanner.services.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invitations")
public class InvitationController {

    @Autowired
    private InvitationService invitationService;

    @PostMapping("/bulk")
    public ResponseEntity<?> sendBulkInvitations(@RequestBody InvitationRequestDTO dto) {
        System.out.println("DTO received: " + dto);
        System.out.println("Event ID: " + dto.getEventId());
        System.out.println("Guest Emails: " + dto.getGuestEmails());
        if (dto.getEventId() == null || dto.getGuestEmails() == null || dto.getGuestEmails().isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid invitation request.");
        }

        List<Invitation> created = invitationService.createInvitation(dto);
        return ResponseEntity.ok(created.size() + " invitations sent.");
    }


    @PutMapping("/{invitationId}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Integer invitationId,
            @RequestParam Status status
    ) {
        invitationService.updateInvitationStatus(invitationId, status);
        return ResponseEntity.ok().build();
    }
}
