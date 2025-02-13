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
    private String status;
    private String commenterFirstName;
    private String commenterLastName;
    private String commenterProfilePicture;
    private String solution;  // Naziv rešenja (ako je primenljivo)
    private String solutionProvider;  // Dobavljač rešenja (ako je primenljivo)
}
