package edu.ftn.iss.eventplanner.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ReportResponseDTO {
    private Long reportId;
    private Long reportedUserId;
    private Long reportingUserId;
    private String reason;
    private LocalDate reportDate;
    private Boolean resolved;
    private LocalDate resolutionDate;

    public ReportResponseDTO(Long reportId, Long reportedUserId, Long reportingUserId, String reason, LocalDate reportDate, Boolean resolved, LocalDate resolutionDate) {
        this.reportId = reportId;
        this.reportedUserId = reportedUserId;
        this.reportingUserId = reportingUserId;
        this.reason = reason;
        this.reportDate = reportDate;
        this.resolved = resolved;
        this.resolutionDate = resolutionDate;
    }
}
