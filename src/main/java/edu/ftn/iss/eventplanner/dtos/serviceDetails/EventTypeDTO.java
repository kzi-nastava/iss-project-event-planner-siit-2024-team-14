package edu.ftn.iss.eventplanner.dtos.serviceDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventTypeDTO {
    private String name;
    private String description;
}
