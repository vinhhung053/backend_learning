package org.example;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class EmailScheduler {

    private final EmailService emailService;

    public EmailScheduler(EmailService emailService) {
        this.emailService = emailService;
    }

    // Cron: 0 giây, 8 phút, 16 giờ, hàng ngày
    @Scheduled(cron = "0 12 21 * * *", zone = "Asia/Ho_Chi_Minh")
    public void executeDailyEmail() {
        try {
            emailService.sendScheduledEmail("vinhhung053@gmail.com");
            System.out.println("Email đã được gửi thành công!");
        } catch (Exception e) {
            System.err.println("Gửi email thất bại: " + e.getMessage());
        }
    }
//    @Async
//    @EventListener(ApplicationReadyEvent.class)
//    public void runOnStartup() {
//        System.out.println("Đang gửi mail khởi động...");
//        try {
//            emailService.sendScheduledEmail("vinhhung053@gmail.com");
//            System.out.println("Email đã được gửi thành công!");
//        } catch (Exception e) {
//            System.err.println("Gửi email thất bại: " + e.getMessage());
//        }
//    }

}