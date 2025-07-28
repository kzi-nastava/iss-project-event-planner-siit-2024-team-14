package edu.ftn.iss.eventplanner.dtos.comments;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApproveCommentDTO {

    private Integer commentId;
    private String approved;
}
