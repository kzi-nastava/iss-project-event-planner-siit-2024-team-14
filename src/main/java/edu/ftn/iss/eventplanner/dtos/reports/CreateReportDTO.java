package edu.ftn.iss.eventplanner.dtos.reports;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateReportDTO {
    private Integer senderId;
    private Integer reportedUserId;
    private String reason;
}
