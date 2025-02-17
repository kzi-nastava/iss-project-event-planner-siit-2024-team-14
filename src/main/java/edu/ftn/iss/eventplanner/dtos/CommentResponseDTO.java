package edu.ftn.iss.eventplanner.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CommentResponseDTO {

    private Integer id;
    private String content;
    private int rating;
    private LocalDate date;
    private String status;
    private Integer commentId;
    private String commenterFirstName;
    private String commenterLastName;
    private String commenterProfilePicture;
    private String solution;
    private String solutionProvider;
}
