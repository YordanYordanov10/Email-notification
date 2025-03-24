package bg.softuni.email.Schedule;

import bg.softuni.email.Model.EmailNotification;
import bg.softuni.email.Service.EmailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class EmailSchedule {

    private final EmailNotificationService emailNotificationService;
    private final JavaMailSenderImpl mailSender;

    @Autowired
    public EmailSchedule(EmailNotificationService emailNotificationService, JavaMailSenderImpl mailSender) {
        this.emailNotificationService = emailNotificationService;
        this.mailSender = mailSender;
    }


    @Scheduled(cron = "0 0 8 * * ?")
    public void sendEmail() {
        List<EmailNotification> emailNotificationsList = emailNotificationService.findByLocalDateTime();

        LocalDateTime now = LocalDateTime.now();

        for (EmailNotification emailNotification : emailNotificationsList) {
            LocalDateTime appointmentDateTime = emailNotification.getAppointmentDate().atStartOfDay();

            if (appointmentDateTime.minusDays(2).isBefore(now) &&
                    appointmentDateTime.isAfter(now) &&
                    !emailNotification.isReminderSent()) {

                SimpleMailMessage userMessage = new SimpleMailMessage();
                userMessage.setTo(emailNotification.getUserEmail());
                userMessage.setSubject("Reminder for Appointment");
                userMessage.setText(String.format(
                        "Hello,\n\nYour appointment for %s with %s is scheduled for %s at %s.\nPrice: %.2f BGN\n\nThank you for choosing us!",
                        emailNotification.getServiceType(),
                        emailNotification.getBarberName(),
                        emailNotification.getAppointmentDate(),
                        emailNotification.getTimeSlot(),
                        emailNotification.getPrice()
                ));

                mailSender.send(userMessage);

                emailNotification.setReminderSent(true);
                emailNotificationService.update(emailNotification);
            }
        }
    }

}
