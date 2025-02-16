package edu.ftn.iss.eventplanner.services;

import edu.ftn.iss.eventplanner.entities.User;
import edu.ftn.iss.eventplanner.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDateTime;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    public void sendActivationEmail(String to, String activationToken) throws MessagingException {
        String subject = "Activate Your Account";
        String activationLink = "http://localhost:4200/activate?token=" + activationToken;

        String body = "<html>"
                + "<body>"
                + "<p>Click the button below to activate your account:</p>"
                + "<a href='" + activationLink + "' style='display: inline-block; padding: 10px 20px; font-size: 16px; color: white; background-color: #5d5d5d; text-decoration: none; border-radius: 5px;'>Activate Account</a>"
                + "<p>If the button doesn't work, copy and paste the link below:</p>"
                + "<p><a href='" + activationLink + "'>" + activationLink + "</a></p>"
                + "</body>"
                + "</html>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);  // true enables HTML content

        mailSender.send(message);
    }

    public boolean verifyActivationToken(String token) {
        User user = userRepository.findByActivationToken(token);
        if (user == null) {
            System.out.println("NEMA USERA------------------------------------------------------------------------------------------------------------");
            return false; // Token is invalid
        }

        LocalDateTime tokenCreationTime = user.getTokenCreationDate();
        if (tokenCreationTime.plusHours(24).isBefore(LocalDateTime.now())) {
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
            sendActivationEmail(user.getEmail(), user.getActivationToken());
            System.out.println("Activation email resent successfully.");
            return true;
        } catch (MessagingException e) {
            // Handle any issues with sending the email
            System.out.println("Failed to resend the activation email.");
            return false;
        }
    }
}
