package edu.ftn.iss.eventplanner.entities;

import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

@DiscriminatorValue("PRODUCT")

public class Product extends Solution {

    private boolean isAvailable;
}
