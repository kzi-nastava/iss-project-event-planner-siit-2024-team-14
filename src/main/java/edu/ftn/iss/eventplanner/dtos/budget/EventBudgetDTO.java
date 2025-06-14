package edu.ftn.iss.eventplanner.dtos.budget;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EventBudgetDTO {
    private double amount, spent;
    private List<EventBudgetItemDTO> items = List.of();
}
