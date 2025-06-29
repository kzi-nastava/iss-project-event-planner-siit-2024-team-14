package edu.ftn.iss.eventplanner.dtos.homepage;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class EventDTO {

    private Integer id;
    private String name;
    private String description;
    private String location;
    private String privacyType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String imageUrl;
    private String organizerFirstName;
    private String organizerLastName;
    private Integer organizerId;
    private String organizerProfilePicture;
    private Integer maxParticipants;
}
