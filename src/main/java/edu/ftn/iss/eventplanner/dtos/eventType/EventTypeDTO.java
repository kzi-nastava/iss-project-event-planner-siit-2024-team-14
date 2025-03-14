package edu.ftn.iss.eventplanner.dtos.eventType;

import edu.ftn.iss.eventplanner.entities.SolutionCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventTypeDTO {
    private int id;
    private String name;
    private String description;
    private boolean isActive;
    private List<SolutionCategory> categories;
}
