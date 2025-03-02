package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.dtos.reports.ChangeReportStatusDTO;
import edu.ftn.iss.eventplanner.dtos.reports.CreateReportDTO;
import edu.ftn.iss.eventplanner.dtos.reports.ReportUserDTO;
import edu.ftn.iss.eventplanner.services.ReportRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportUserController {

    private final ReportRequestService reportService;

    @PostMapping
    public ResponseEntity<ReportUserDTO> createReport(@Valid @RequestBody CreateReportDTO createReportDTO) {
        ReportUserDTO response = reportService.createReport(createReportDTO);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/approve")
    public ResponseEntity<ReportUserDTO> approveReport(@Valid @RequestBody ChangeReportStatusDTO approveReportDTO) {
        ReportUserDTO response = reportService.approveReport(approveReportDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/delete")
    public ResponseEntity<ReportUserDTO> deleteReport(@Valid @RequestBody ChangeReportStatusDTO deleteReportDTO) {
        ReportUserDTO response = reportService.deleteReport(deleteReportDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ReportUserDTO>> getPendingComments() {
        List<ReportUserDTO> comments = reportService.getPendingReports();
        return ResponseEntity.ok(comments);
    }
}
