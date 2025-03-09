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
    private String description;

    @ElementCollection
    @CollectionTable(name = "provider_photos")  // Specify table name for the collection
    @Column(name = "photo_url")  // Column name for each element in the collection
    private List<String> photos;

    @ManyToMany
    @JoinTable(
            name = "providers_solutions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "solution_id")
    )
    private List<Solution> reservedSolutions;
}
