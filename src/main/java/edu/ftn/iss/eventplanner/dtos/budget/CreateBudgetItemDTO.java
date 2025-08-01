package edu.ftn.iss.eventplanner.dtos.budget;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class CreateBudgetItemDTO {

    @PositiveOrZero
    double amount;

}
