package edu.ftn.iss.eventplanner.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CreateCommentDTO {

    @NotNull
    private Long productId;

    @NotBlank
    private String content;

    private int rating;

    private LocalDate date;
}

