package edu.ftn.iss.eventplanner.entities;

import lombok.*;
import jakarta.persistence.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("PROVIDER") // Maps to role = "PROVIDER"
public class ServiceAndProductProvider extends User {

    private String companyName;
    private String contactInfo;
    private String description;
    private List<String> pictures;
}

