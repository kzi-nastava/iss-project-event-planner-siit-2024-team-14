package edu.ftn.iss.eventplanner.dtos.updateUsers;

import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateToOrganizerDTO implements Serializable {
    private Integer id;
    private String email;
    private String password;
    private String confirmPassword;
    private String name;
    private String surname;
    private String address;
    private String city;
    private String phoneNumber;
}
