package edu.ftn.iss.eventplanner.model;

import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("SERVICE")

public class Service extends Solution {

    private int duration; // Trajanje usluge (u minutima)
    private String reservationType; // Tip rezervacije (npr. online, telefonski)
    private LocalDate reservationDate; // Datum kada je usluga dostupna
    private LocalDate cancellationDate; // Datum kada usluga više nije dostupna

    public Service(Long id, String name, String description, double price, double discount, String imageUrl, boolean isAvailable, int duration, String reservationType, LocalDate reservationDate, LocalDate cancellationDate, List<Comment> comments) {
        super(id, name, description, price, discount, imageUrl, isAvailable, true, false, comments);
        this.duration = duration;
        this.reservationType = reservationType;
        this.reservationDate = reservationDate;
        this.cancellationDate = cancellationDate;
    }
}
