package edu.ftn.iss.eventplanner.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApproveCommentDTO {

    private Integer commentId;
    private String approved;  // Da li se komentar odobrava (true) ili briše (false)
}
