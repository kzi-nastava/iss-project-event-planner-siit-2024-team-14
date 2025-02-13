package edu.ftn.iss.eventplanner.entities;

import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@DiscriminatorValue("Product")

public class Product extends Solution {

}