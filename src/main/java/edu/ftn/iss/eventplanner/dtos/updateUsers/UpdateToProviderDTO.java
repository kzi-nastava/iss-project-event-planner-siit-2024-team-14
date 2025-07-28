package edu.ftn.iss.eventplanner.dtos.updateUsers;
import lombok.Data;
import java.util.List;

@Data
public class UpdateToProviderDTO {
    private Integer id;
    private String email;
    private String password;
    private String confirmPassword;
    private String companyName;
    private String companyDescription;
    private String address;
    private String city;
    private String phoneNumber;
    private List<String> photos;
}
