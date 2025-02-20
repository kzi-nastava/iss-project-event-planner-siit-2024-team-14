package edu.ftn.iss.eventplanner.dtos.comments;

import lombok.AllArgsConstructor;
import lombok.Data;
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
    private Integer productId;
    private Integer serviceId;
    private Integer commenterId;
}

