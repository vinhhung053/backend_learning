package org.example;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendScheduledEmail(String to) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("Thông Báo Hệ Thống - Test Daily Report");

        String htmlContent = """
                <!DOCTYPE html>
                <html xmlns:th="http://www.thymeleaf.org">
                <body style="margin: 0; padding: 20px; font-family: Arial, sans-serif; background-color: #f4f4f4;">
                    <div style="max-width: 600px; margin: auto; background: #ffffff; border-radius: 20px; overflow: hidden;">
                        <!-- Header -->
                        <div style="background: #2563eb; color: #ffffff; padding: 40px; text-align: center;">
                            <h1 style="margin: 0;">Great Job, Player!</h1>
                            <p>Your latest score update is here.</p>
                        </div>
                
                        <!-- Score -->
                        <div style="padding: 30px; text-align: center;">
                            <p style="color: #64748b;">Current Score</p>
                            <div style="font-size: 50px; font-weight: bold;" th:text="${score}">{{user_score}}</div>
                        </div>
                
                        <!-- Button -->
                        <div style="text-align: center; padding-bottom: 40px;">
                            <a href="#" style="background: #7c3aed; color: #fff; padding: 15px 30px; border-radius: 99px; text-decoration: none;">View Full Stats</a>
                        </div>
                    </div>
                </body>
                </html>
            """;

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}