package edu.ftn.iss.eventplanner.dtos;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ReportDTO {
    private Long reportedUserId;
    private Long reportingUserId;
    private String reason; // Razlog prijave
    private LocalDate reportDate;


    public ReportDTO(Long reportedUserId, Long reportingUserId, String reason, LocalDate reportDate) {
        this.reportedUserId = reportedUserId;
        this.reportingUserId = reportingUserId;
        this.reason = reason;
        this.reportDate = reportDate;
    }
}
