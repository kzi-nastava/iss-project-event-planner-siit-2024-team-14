package edu.ftn.iss.eventplanner.entities;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    private double discount;
    private String imageUrl;
    private int duration;
    private String reservationType;
    private LocalDate reservationDate;
    private LocalDate cancellationDate;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
