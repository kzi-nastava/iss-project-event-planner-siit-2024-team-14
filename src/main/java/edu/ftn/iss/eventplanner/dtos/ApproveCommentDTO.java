package edu.ftn.iss.eventplanner.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApproveCommentDTO {

    private Long commentId;
    private boolean approve;  // Da li se komentar odobrava (true) ili briše (false)
}
