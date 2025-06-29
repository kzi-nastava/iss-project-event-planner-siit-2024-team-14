package edu.ftn.iss.eventplanner.dtos.invitations;

import edu.ftn.iss.eventplanner.dtos.homepage.EventDTO;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Data
public class EventWithInvitationsDTO {
    private EventDTO event;
    private List<InvitationResponseDTO> invitations;
}
