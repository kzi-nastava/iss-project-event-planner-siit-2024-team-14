package edu.ftn.iss.eventplanner.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate purchaseDate;
    private boolean isPaid;

    @OneToOne(mappedBy = "purchase")
    private Booking booking;
}

