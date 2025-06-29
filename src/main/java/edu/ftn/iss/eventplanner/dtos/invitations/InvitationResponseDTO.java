package edu.ftn.iss.eventplanner.dtos.invitations;
import edu.ftn.iss.eventplanner.enums.Status;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Data
public class InvitationResponseDTO {
    private String guestEmail;
    private Status status;
}
