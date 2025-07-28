package edu.ftn.iss.eventplanner.dtos.budget;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class UpdateBudgetItemDTO {

    int categoryId;

    @PositiveOrZero
    double amount;

}
