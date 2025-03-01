package bg.softuni.email.Service;

import bg.softuni.email.Model.EmailNotification;
import bg.softuni.email.Repository.EmailNotificationRepository;
import bg.softuni.email.Web.Dto.EmailRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
                .appointmentDateTime(emailRequest.getAppointmentDateTime())
                .barberId(emailRequest.getBarberId())
                .barberName(emailRequest.getBarberName())
                .serviceType(emailRequest.getServiceType())
                .userEmail(emailRequest.getUserEmail())
                .userId(emailRequest.getUserId())
                .price(emailRequest.getPrice())
                .createdAt(LocalDateTime.now())
                .build();

        emailNotificationRepository.save(emailNotification);


        // 📩 Изпращане на имейл до клиента
        SimpleMailMessage userMessage = new SimpleMailMessage();
        userMessage.setTo(emailRequest.getUserEmail());
        userMessage.setSubject("Your Appointment Confirmation");
        userMessage.setText("Hello,\n\nYour appointment for " + emailRequest.getServiceType() +
                " with " + emailRequest.getBarberName() +
                " is scheduled for " + emailRequest.getAppointmentDateTime() +
                ".\nPrice: " + emailRequest.getPrice() +
                "\n\nThank you for choosing us!");

        mailSender.send(userMessage);

        // 📩 Изпращане на имейл до барбъра
        SimpleMailMessage barberMessage = new SimpleMailMessage();
        barberMessage.setTo(emailRequest.getBarberEmail());
        barberMessage.setSubject("New Appointment Scheduled");
        barberMessage.setText("Hello " + emailRequest.getBarberName() + ",\n\nYou have a new appointment scheduled.\n" +
                "Client: " + emailRequest.getUserEmail() + "\n" +
                "Service: " + emailRequest.getServiceType() + "\n" +
                "Date & Time: " + emailRequest.getAppointmentDateTime() + "\n" +
                "Price: " + emailRequest.getPrice() + "\n\n" +
                "Please be prepared!");

        mailSender.send(barberMessage);

        return emailNotification;
    }
}
