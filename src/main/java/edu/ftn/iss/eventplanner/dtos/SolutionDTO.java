package edu.ftn.iss.eventplanner.dtos;

import edu.ftn.iss.eventplanner.entities.Comment;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolutionDTO {
    private Integer id;
    private String name;
    private String description;
    private double price;
    private double discount;
    private String imageUrl;
    private boolean isAvailable;
}
