package edu.ftn.iss.eventplanner.entities;
import edu.ftn.iss.eventplanner.enums.Status;
import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "solution_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;
    private double price;
    private double discount;
    private String imageUrl;
    private boolean isAvailable;
    private boolean isVisible;
    private boolean isDeleted;
    private Status status;      // when new category is requested

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private SolutionCategory category;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private ServiceAndProductProvider provider;
}
