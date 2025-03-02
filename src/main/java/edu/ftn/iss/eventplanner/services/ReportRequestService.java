package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.dtos.notifications.NotificationDTO;
import edu.ftn.iss.eventplanner.dtos.reports.ChangeReportStatusDTO;
import edu.ftn.iss.eventplanner.dtos.reports.CreateReportDTO;
import edu.ftn.iss.eventplanner.dtos.reports.ReportUserDTO;
import edu.ftn.iss.eventplanner.entities.*;
import edu.ftn.iss.eventplanner.enums.Role;
import edu.ftn.iss.eventplanner.enums.Status;
import edu.ftn.iss.eventplanner.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportRequestService {

    private final ReportRequestRepository reportRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;


    public ReportUserDTO createReport(CreateReportDTO dto) {
        ReportRequest report = new ReportRequest();
        report.setReason(dto.getReason());
        report.setReportedUser(findUserById(dto.getReportedUserId()));
        report.setSender(findUserById(dto.getSenderId()));
        report.setStatus(Status.PENDING);
        ReportRequest savedReport = reportRepository.save(report);

        String sender = savedReport.getSender().getEmail();
        String reportedUser = savedReport.getReportedUser().getEmail();

        Optional<User> adminUserOpt = userRepository.findByEmail("admin@gmail.com");
        if (adminUserOpt.isPresent()) {
            User adminUser = adminUserOpt.get();
            Notification notification = new Notification();
            notification.setMessage("You have new report request to review.");

            notification.setUser(adminUser);
            notification.setDate(LocalDate.now());
            notification.setRead(false);
            notificationRepository.save(notification);
        }


        return new ReportUserDTO(savedReport.getId(), savedReport.getReason(),savedReport.getStatus(), sender, reportedUser);
    }

    public ReportUserDTO deleteReport(ChangeReportStatusDTO dto) {
        ReportRequest report = findReportById(dto.getReportId());
        report.setStatus(Status.DELETED);
        reportRepository.save(report);

        String sender = report.getSender().getEmail();
        String reportedUser = report.getReportedUser().getEmail();

        String message = "Your report: '" + report.getReason() + "' for '" + reportedUser + "' has been rejected!";
        sendNotification(report.getSender().getId(), message, report.getId());

        return new ReportUserDTO(report.getId(), report.getReason(), report.getStatus(), sender, reportedUser);
    }

    public ReportUserDTO approveReport(ChangeReportStatusDTO dto) {
        ReportRequest report = findReportById(dto.getReportId());
        report.setStatus(Status.APPROVED);
        report.setTimestamp(LocalDateTime.now().plusDays(3));
        reportRepository.save(report);

        User user = userRepository.findById(report.getReportedUser().getId()).orElse(null);
        user.setSuspended(true);
        userRepository.save(user);

        String sender = report.getSender().getEmail();
        String reportedUser = report.getReportedUser().getEmail();

        String message = "Your report: '" + report.getReason() + "' for '" + reportedUser + "' has been accepted!";
        sendNotification(report.getSender().getId(), message, report.getId());

        return new ReportUserDTO(report.getId(), report.getReason(), report.getStatus(), sender, reportedUser);
    }



    public List<ReportUserDTO> getAllReports() {
        return reportRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ReportUserDTO> getPendingReports() {
        return reportRepository.findByStatus(Status.PENDING).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // --- POMOĆNE METODE ---

    private ReportRequest findReportById(int id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }


    private User findUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private void sendNotification(Integer userId, String message, Integer commentId) {
        NotificationDTO notification = notificationService.createNotification(userId, message, commentId, null);
    }


    private ReportUserDTO mapToDTO(ReportRequest report) {
        String sender = report.getSender().getEmail();
        System.out.println(sender);
        String reportedUser = report.getReportedUser().getEmail();
        System.out.println(reportedUser);
        return new ReportUserDTO(report.getId(), report.getReason(), report.getStatus(), sender, reportedUser);
    }

}
