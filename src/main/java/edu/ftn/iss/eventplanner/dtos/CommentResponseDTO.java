package edu.ftn.iss.eventplanner.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CommentResponseDTO {

    private Long id;
    private String content;
    private int rating;
    private LocalDate date;
    private String status;  // Status komentara: "pending", "approved", "deleted"
    private Long productId; // ID proizvoda ili usluge na kojem je komentar
}
