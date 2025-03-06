package edu.ftn.iss.eventplanner.dtos.reports;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ViewProviderProfileDTO {
    private Integer id;
    private String companyName;
    private String description;
    private String email;
    private String address;
    private String city;
    private Integer phoneNumber;
    private String profilePhoto;
}
