package edu.ftn.iss.eventplanner.controllers;
import edu.ftn.iss.eventplanner.dtos.invitations.InvitationRequestDTO;
import edu.ftn.iss.eventplanner.dtos.registration.QuickRegistrationDTO;
import edu.ftn.iss.eventplanner.entities.Event;
import edu.ftn.iss.eventplanner.entities.Invitation;
import edu.ftn.iss.eventplanner.enums.Status;
import edu.ftn.iss.eventplanner.exceptions.ConflictException;
import edu.ftn.iss.eventplanner.services.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public ResponseEntity<?> registerAndFollowEvent(@RequestBody QuickRegistrationDTO dto) {
        try {
            invitationService.registerUserViaInvitation(dto);
            return ResponseEntity.ok("User registered and event followed.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed.");
        }
    }
}
