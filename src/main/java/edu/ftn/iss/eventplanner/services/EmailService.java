package edu.ftn.iss.eventplanner.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

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
}
