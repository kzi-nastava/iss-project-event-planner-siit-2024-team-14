package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.entities.Event;
import edu.ftn.iss.eventplanner.entities.User;
import edu.ftn.iss.eventplanner.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    // In dev (emulator), your backend is reachable at 10.0.2.2
    // In prod, switch to your real HTTPS domain.
    private static final String BACKEND_BASE_URL = "http://10.0.2.2:8080";

    private String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            return s;
        }
    }

    public void sendActivationEmail(String to, String activationToken, String role) throws MessagingException {
        String subject = "Activate Your Account";

        // Build ONE verify URL that hits your backend GET /api/email-verification/verify
        String verifyUrl = BACKEND_BASE_URL
                + "/api/email-verification/verify?token=" + urlEncode(activationToken)
                + "&role=" + urlEncode(role);

        String body = """
                <html>
                  <body style="font-family: Arial, Helvetica, sans-serif;">
                    <p>Click the button below to verify your email and activate your account:</p>
                    <p>
                      <a href="%s"
                         style="display:inline-block;padding:12px 20px;font-size:16px;color:#ffffff;
                                background-color:#5d5d5d;text-decoration:none;border-radius:6px;">
                        Verify Email
                      </a>
                    </p>
                    <p>If the button doesn’t work, copy and paste this link into your browser:</p>
                    <p><a href="%s">%s</a></p>
                  </body>
                </html>
                """.formatted(verifyUrl, verifyUrl, verifyUrl);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(message);
    }

    public boolean verifyActivationToken(String token) {
        User user = userRepository.findByActivationToken(token);
        if (user == null) {
            System.out.println("USER NOT FOUND for token");
            return false;
        }

        LocalDateTime tokenCreationTime = user.getTokenCreationDate();
        System.out.println("Created=" + tokenCreationTime + " Now=" + LocalDateTime.now());
        if (tokenCreationTime == null || tokenCreationTime.plusHours(24).isBefore(LocalDateTime.now())) {
            System.out.println("TOKEN INVALID or EXPIRED");
            return false;
        }

        user.setActive(true);
        user.setVerified(true);
        user.setActivationToken(null);
        user.setTokenCreationDate(null);
        userRepository.save(user);
        System.out.println("USER ACTIVATED");
        return true;
    }

    public boolean resendActivationEmail(String token) {
        User user = userRepository.findByActivationToken(token);
        if (user == null) {
            System.out.println("User not found with the given activation token.");
            return false;
        }

        LocalDateTime tokenCreationTime = user.getTokenCreationDate();
        if (tokenCreationTime.plusHours(24).isBefore(LocalDateTime.now())) {
            System.out.println("The activation token has expired.");
            return false;
        }

        user.generateActivationToken();
        user.setTokenCreationDate(LocalDateTime.now());
        userRepository.save(user);

        try {
            sendActivationEmail(user.getEmail(), user.getActivationToken(), user.getClass().getSimpleName());
            System.out.println("Activation email resent successfully.");
            return true;
        } catch (MessagingException e) {
            System.out.println("Failed to resend the activation email.");
            return false;
        }
    }

    public void sendBookingNotification(String to, String eventName, String serviceName,
                                        LocalDate date, Time startTime, Duration duration) throws MessagingException {
        String subject = "New Booking Notification";

        String body = "<html>"
                + "<body>"
                + "<h3>A new booking has been made</h3>"
                + "<p><b>Event:</b> " + eventName + "</p>"
                + "<p><b>Service:</b> " + serviceName + "</p>"
                + "<p><b>Date:</b> " + date + "</p>"
                + "<p><b>Start Time:</b> " + startTime + "</p>"
                + "<p><b>Duration:</b> " + duration.toMinutes() + " minutes</p>"
                + "</body>"
                + "</html>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(message);
    }

    public void sendInvitationEmail(String recipientEmail, Event event, String webLink, String androidLink) {
        String subject = "You're invited to the event: " + event.getName();

        String content = """
                Hello,

                You have been invited to the event:

                📝 Name: %s
                🗒️ Description: %s
                📍 Location: %s
                📅 Start: %s
                📅 End: %s
                👤 Organizer: %s %s

                ➡️ To follow the event on web, click the following link:
                %s

                ➡️ To open the invitation on your Android app, use this link:
                %s

                If you don't have an account, you'll be able to register quickly using these links.
                If you already have an account, you'll be redirected to login and the event will be added to your calendar.

                Best regards,
                EventPlanner Team
                """.formatted(
                event.getName(),
                event.getDescription(),
                event.getLocation(),
                event.getStartDate().toString(),
                event.getEndDate().toString(),
                event.getOrganizer().getName(),
                event.getOrganizer().getSurname(),
                webLink,
                androidLink
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }
}
