package edu.ftn.iss.eventplanner.dtos.budget;

import edu.ftn.iss.eventplanner.dtos.homepage.EventDTO;
import edu.ftn.iss.eventplanner.entities.Event;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EventBudgetDTO {
    private Event event; // TODO
    private double amount, spent;
    private List<EventBudgetItemDTO> items = List.of();
}
