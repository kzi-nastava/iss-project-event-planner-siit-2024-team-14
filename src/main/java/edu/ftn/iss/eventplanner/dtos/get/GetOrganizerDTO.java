package edu.ftn.iss.eventplanner.dtos.get;

import lombok.Data;

@Data
public class GetOrganizerDTO {
    private String message;
    private OrganizerDTO organizer;
}
