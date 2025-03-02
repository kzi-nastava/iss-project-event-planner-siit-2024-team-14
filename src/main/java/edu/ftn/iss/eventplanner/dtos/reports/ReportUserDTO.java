package edu.ftn.iss.eventplanner.dtos.reports;

import edu.ftn.iss.eventplanner.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportUserDTO {
    Integer reportId;
    String reason;
    Status status;
    String sender;
    String reportedUser;
}
