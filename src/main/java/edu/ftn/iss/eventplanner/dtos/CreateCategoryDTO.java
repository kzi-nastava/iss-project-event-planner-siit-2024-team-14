package edu.ftn.iss.eventplanner.dtos;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCategoryDTO {
    @NotBlank(message = "{category.name.blank}")
    @Size(min = 3, max = 100, message = "{category.name.size}")
    private String name;

    @Size(max = 500, message = "{category.description.size}")
    private String description;
}
