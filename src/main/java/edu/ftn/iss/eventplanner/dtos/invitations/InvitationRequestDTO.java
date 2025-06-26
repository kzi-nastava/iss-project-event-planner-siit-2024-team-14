package edu.ftn.iss.eventplanner.dtos.invitations;
import lombok.Data;

import java.util.List;

@Data
public class InvitationRequestDTO {
    private Integer eventId;
    private List<String> guestEmails;
}

