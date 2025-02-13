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
    private String content;
    @NotNull
    private int rating;
    private LocalDate date;
    private Long productId;
    private Long serviceId;
    private Integer commenterId;
}

