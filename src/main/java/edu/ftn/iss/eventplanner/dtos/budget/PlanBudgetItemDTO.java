package edu.ftn.iss.eventplanner.dtos.budget;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Value;

@Value
public class PlanBudgetItemDTO {

    int categoryId;

    @PositiveOrZero
    double amount;

}
