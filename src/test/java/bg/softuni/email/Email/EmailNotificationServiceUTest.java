package bg.softuni.email.Email;

import bg.softuni.email.Model.EmailNotification;
import bg.softuni.email.Repository.EmailNotificationRepository;
import bg.softuni.email.Service.EmailNotificationService;
import bg.softuni.email.Web.Dto.EmailRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailNotificationServiceUTest {

    @InjectMocks
    private EmailNotificationService emailNotificationService;

    @Mock
    private EmailNotificationRepository emailNotificationRepository;

    @Mock
    private JavaMailSenderImpl mailSender;

    private EmailRequest emailRequest;
    private EmailNotification emailNotification;
    private EmailNotification emailNotification1;

    @BeforeEach
    void setUp() {
        emailRequest = EmailRequest.builder()
                .userId(UUID.randomUUID())
                .barberId(UUID.randomUUID())
                .barberName("barber123")
                .barberEmail("barber@gmail.com")
                .userEmail("user@gmail.com")
                .appointmentDate(LocalDate.now().plusDays(2))
                .timeSlot("10:00")
                .serviceType("Haircut")
                .price(20)
                .build();

        emailNotification = EmailNotification.builder()
                .userId(emailRequest.getUserId())
                .barberId(emailRequest.getBarberId())
                .barberName(emailRequest.getBarberName())
                .barberEmail(emailRequest.getBarberEmail())
                .userEmail(emailRequest.getUserEmail())
                .appointmentDate(emailRequest.getAppointmentDate())
                .timeSlot(emailRequest.getTimeSlot())
                .serviceType(emailRequest.getServiceType())
                .price(emailRequest.getPrice())
                .createdAt(LocalDateTime.now())
                .reminderSent(false)
                .build();

        emailNotification1 = EmailNotification.builder()
                .userId(UUID.randomUUID())
                .barberId(UUID.randomUUID())
                .barberName("barber456")
                .barberEmail("barber2@gmail.com")
                .userEmail("user2@gmail.com")
                .appointmentDate(LocalDate.now().plusDays(1))
                .timeSlot("12:00")
                .serviceType("Beard Trim")
                .price(15)
                .createdAt(LocalDateTime.now())
                .reminderSent(false)
                .build();
    }

    @Test
    void givenEmailRequestShouldCreateAndSaveEmailNotificationAndSendEmailToUserAndBarber() {

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        when(emailNotificationRepository.save(any(EmailNotification.class))).thenReturn(emailNotification);

        EmailNotification result = emailNotificationService.sendNotification(emailRequest);

        verify(mailSender, times(2)).send(any(SimpleMailMessage.class));
        verify(emailNotificationRepository, times(1)).save(any(EmailNotification.class));

        assertNotNull(result);
        assertEquals(emailRequest.getUserEmail(), result.getUserEmail());
        assertEquals(emailRequest.getBarberEmail(), result.getBarberEmail());
        assertEquals(emailRequest.getAppointmentDate(), result.getAppointmentDate());
        assertEquals(emailRequest.getTimeSlot(), result.getTimeSlot());
        assertEquals(emailRequest.getPrice(), result.getPrice());
        assertEquals(emailRequest.getServiceType(), result.getServiceType());
        assertEquals(emailRequest.getBarberName(), result.getBarberName());
    }

    @Test
    void givenTimeRangeShouldReturnPendingEmailNotifications() {

        List<EmailNotification> expectedNotifications = Arrays.asList(emailNotification, emailNotification1);

        when(emailNotificationRepository.findByAppointmentDateBetweenAndReminderSentFalse(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(expectedNotifications);

        List<EmailNotification> result = emailNotificationService.findByLocalDateTime();

        verify(emailNotificationRepository, times(1))
                .findByAppointmentDateBetweenAndReminderSentFalse(any(LocalDate.class), any(LocalDate.class));


        assertEquals(2, result.size());
        assertEquals(emailNotification.getUserEmail(), result.get(0).getUserEmail());
        assertEquals(emailNotification1.getUserEmail(), result.get(1).getUserEmail());
    }




}
