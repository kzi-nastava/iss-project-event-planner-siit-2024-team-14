package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.entities.Event;
import edu.ftn.iss.eventplanner.entities.User;
import edu.ftn.iss.eventplanner.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

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

    public void sendActivationEmail(String to, String activationToken, String role) throws MessagingException {
        String subject = "Activate Your Account";

        String frontendWebUrl = "http://localhost:4200";
        String androidAppUrl = "http://10.0.2.2:8080";

        String webLink = frontendWebUrl + "/activate?token=" + activationToken + "&role=" + role;
        String androidLink = androidAppUrl + "/activate?token=" + activationToken + "&role=" + role;

        String body = "<html>"
                + "<body>"
                + "<p>Click the button below to activate your account from Web:</p>"
                + "<a href='" + webLink + "' style='display: inline-block; padding: 10px 20px; font-size: 16px; color: white; background-color: #5d5d5d; text-decoration: none; border-radius: 5px;'>Activate on Web</a>"
                + "<br/><br/>"
                + "<p>Or activate directly via Android app:</p>"
                + "<a href='" + androidLink + "' style='display: inline-block; padding: 10px 20px; font-size: 16px; color: white; background-color: #8b5d5d; text-decoration: none; border-radius: 5px;'>Activate on Android</a>"
                + "<br/><br/>"
                + "<p>If neither button works, copy one of the links below:</p>"
                + "<p>Web: <a href='" + webLink + "'>" + webLink + "</a></p>"
                + "<p>Android: <a href='" + androidLink + "'>" + androidLink + "</a></p>"
                + "</body>"
                + "</html>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(message);
    }


    public boolean verifyActivationToken(String token) {
        User user = userRepository.findByActivationToken(token);
        if (user == null) {
            System.out.println("USERA NOT FIND------------------------------------------------------------------------------------------------------------");
            return false; // Token is invalid
        }

        LocalDateTime tokenCreationTime = user.getTokenCreationDate();
        if (tokenCreationTime == null ||tokenCreationTime.plusHours(24).isBefore(LocalDateTime.now())) {
            return false; // Token has expired
        }

        // Token is valid, activate user
        user.setActive(true);
        user.setActivationToken(null); // Remove token after activation
        user.setTokenCreationDate(null); // Remove token creation date
        userRepository.save(user);
        return true;
    }

    public boolean resendActivationEmail(String token) {
        // Find the user by the activation token
        User user = userRepository.findByActivationToken(token);

        if (user == null) {
            // If no user is found with the provided token, return false
            System.out.println("User not found with the given activation token.");
            return false;
        }

        // Check if the token is expired (24-hour validity)
        LocalDateTime tokenCreationTime = user.getTokenCreationDate();
        if (tokenCreationTime.plusHours(24).isBefore(LocalDateTime.now())) {
            // If the token is expired, return false
            System.out.println("The activation token has expired.");
            return false;
        }

        // Generate a new activation token (optional, if needed)
        user.generateActivationToken();
        user.setTokenCreationDate(LocalDateTime.now());
        userRepository.save(user);

        // Send the activation email again
        try {
            sendActivationEmail(user.getEmail(), user.getActivationToken(), user.getClass().getSimpleName());
            System.out.println("Activation email resent successfully.");
            return true;
        } catch (MessagingException e) {
            // Handle any issues with sending the email
            System.out.println("Failed to resend the activation email.");
            return false;
        }
    }

    public void sendBookingNotification(String to, String eventName, String serviceName, LocalDate date, Time startTime, Duration duration) throws MessagingException {
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
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
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
