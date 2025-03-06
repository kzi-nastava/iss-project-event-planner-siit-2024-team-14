package edu.ftn.iss.eventplanner.dtos.reports;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ViewOrganizerProfileDTO {
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String address;
    private String city;
    private Integer phoneNumber;
    private String profilePhoto;
}
