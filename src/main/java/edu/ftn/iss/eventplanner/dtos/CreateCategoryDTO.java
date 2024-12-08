package edu.ftn.iss.eventplanner.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
public class CreateCategoryDTO {
    @NotBlank
    private String name;
    private String description;
}
