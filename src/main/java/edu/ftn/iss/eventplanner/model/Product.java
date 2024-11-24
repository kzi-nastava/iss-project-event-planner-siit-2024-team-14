package edu.ftn.iss.eventplanner.model;

import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    private double discount;
    private String imageUrl;
    private boolean isAvailable;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Service> services;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Comment> comments;
}
