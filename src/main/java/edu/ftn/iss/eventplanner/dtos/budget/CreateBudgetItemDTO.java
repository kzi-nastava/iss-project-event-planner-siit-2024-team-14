package edu.ftn.iss.eventplanner.dtos.budget;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Value;

@Value
public class CreateBudgetItemDTO {

    @PositiveOrZero
    double amount;

}
