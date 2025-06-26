package edu.ftn.iss.eventplanner.dtos.invitations;

import edu.ftn.iss.eventplanner.enums.Status;

import lombok.Data;
@Data
public class InvitationResponseDTO {
    private Integer id;
    private String guestEmail;
    private Status status;
    private String eventTitle;
    private Integer eventId;
}
