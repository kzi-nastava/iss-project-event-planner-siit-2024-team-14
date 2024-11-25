package edu.ftn.iss.eventplanner.entities;

import lombok.*;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ServiceAndProductProvider extends User {

    private String companyName;
    private String contactInfo;
    private String description;
}

