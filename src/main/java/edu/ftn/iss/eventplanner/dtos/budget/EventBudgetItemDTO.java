package edu.ftn.iss.eventplanner.dtos.budget;

import edu.ftn.iss.eventplanner.dtos.serviceDetails.CategoryDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EventBudgetItemDTO {
    private CategoryDTO category;
    private double amount, spent;
    private List<SolutionItemDTO> items = List.of();
}
