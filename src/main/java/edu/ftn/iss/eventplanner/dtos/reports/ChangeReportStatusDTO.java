package edu.ftn.iss.eventplanner.dtos.reports;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChangeReportStatusDTO {
    private Integer reportId;
    private String approved;
    private LocalDateTime timestamp;
}
