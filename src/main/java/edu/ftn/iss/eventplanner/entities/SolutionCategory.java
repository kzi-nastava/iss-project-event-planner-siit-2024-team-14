package edu.ftn.iss.eventplanner.entities;

import edu.ftn.iss.eventplanner.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Categories")
public class SolutionCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank
    @Size(min = 3, max = 100)
    @Column(name = "name", unique = true, nullable = false, length = 100)
    private String name;

    @NotNull
    @Size(max = 500)
    @Column(name = "description", nullable = false, length = 500)
    private String description = "";

    @OneToMany(mappedBy = "solutionCategory", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.EAGER)
    private List<EventType> eventTypes = new ArrayList<>();

    private Status status;
}