package edu.ftn.iss.eventplanner.entities;

import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("PRODUCT") // Oznaka za Product u bazi podataka
public class Product extends Solution {

    private boolean isAvailable;

    public Product(Long id, String name, String description, double price, double discount, String imageUrl, boolean isAvailable, boolean isVisible, List<Comment> comments) {
        super(id, name, description, price, discount, imageUrl, isAvailable, isVisible, false, comments);
        this.isAvailable = isAvailable;
    }
}
