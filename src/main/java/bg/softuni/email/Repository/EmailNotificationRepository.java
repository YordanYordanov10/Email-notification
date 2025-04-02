package bg.softuni.email.Repository;

import bg.softuni.email.EmailApplication;
import bg.softuni.email.Model.EmailNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EmailNotificationRepository extends JpaRepository<EmailNotification, UUID> {


    List<EmailNotification> findByAppointmentDateBetweenAndReminderSentFalse(LocalDate localDate, LocalDate localDate1);

    void deleteEmailNotificationByUserId(UUID userId);

    EmailNotification findByAppointmentDateAndTimeSlotAndUserId(LocalDate appointmentDate, String timeSlot, UUID userId);

    EmailNotification findByAppointmentDateAndTimeSlotAndBarberId(LocalDate appointmentDate, String timeSlot, UUID barberId);

}
