package bg.softuni.email.Service;

import bg.softuni.email.Model.EmailNotification;
import bg.softuni.email.Repository.EmailNotificationRepository;
import bg.softuni.email.Web.Dto.EmailRequest;
import jakarta.transaction.Transactional;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EmailNotificationService {

    private final EmailNotificationRepository emailNotificationRepository;
    private final JavaMailSenderImpl mailSender;

    public EmailNotificationService(EmailNotificationRepository emailNotificationRepository, JavaMailSenderImpl mailSender) {
        this.emailNotificationRepository = emailNotificationRepository;
        this.mailSender = mailSender;
    }


    public EmailNotification sendNotification(EmailRequest emailRequest) {



        EmailNotification emailNotification = EmailNotification.builder()
                .barberEmail(emailRequest.getBarberEmail())
                .appointmentDate(emailRequest.getAppointmentDate())
                .timeSlot(emailRequest.getTimeSlot())
                .barberId(emailRequest.getBarberId())
                .barberName(emailRequest.getBarberName())
                .serviceType(emailRequest.getServiceType())
                .userEmail(emailRequest.getUserEmail())
                .userId(emailRequest.getUserId())
                .price(emailRequest.getPrice())
                .createdAt(LocalDateTime.now())
                .reminderSent(false)
                .build();

        emailNotificationRepository.save(emailNotification);



        SimpleMailMessage userMessage = new SimpleMailMessage();
        userMessage.setTo(emailRequest.getUserEmail());
        userMessage.setSubject("Your Appointment Confirmation");
        userMessage.setText("Hello,\n\nYour appointment for " + emailRequest.getServiceType() +
                " with " + emailRequest.getBarberName() +
                " is scheduled for " + emailRequest.getAppointmentDate() + emailRequest.getTimeSlot() +
                ".\nPrice: " + emailRequest.getPrice() +
                "\n\nThank you for choosing us!");

        mailSender.send(userMessage);


        SimpleMailMessage barberMessage = new SimpleMailMessage();
        barberMessage.setTo(emailRequest.getBarberEmail());
        barberMessage.setSubject("New Appointment Scheduled");
        barberMessage.setText("Hello " + emailRequest.getBarberName() + ",\n\nYou have a new appointment scheduled.\n" +
                "Client: " + emailRequest.getUserEmail() + "\n" +
                "Service: " + emailRequest.getServiceType() + "\n" +
                "Date & Time: " + emailRequest.getAppointmentDate() + emailRequest.getTimeSlot() + "\n" +
                "Price: " + emailRequest.getPrice() + "\n\n" +
                "Please be prepared!");

        mailSender.send(barberMessage);

        return emailNotification;
    }

    public List<EmailNotification> findByLocalDateTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next24Hours = now.plusDays(1);

        return emailNotificationRepository.findByAppointmentDateBetweenAndReminderSentFalse(
                now.toLocalDate(), next24Hours.toLocalDate()
        );
    }


    public void update(EmailNotification emailNotification) {
        emailNotificationRepository.save(emailNotification);
    }

    @Transactional
    public void deleteEmailNotification(UUID userId) {
        emailNotificationRepository.deleteEmailNotificationByUserId(userId);
    }
}
