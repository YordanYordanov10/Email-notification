package bg.softuni.email.Web.Mapper;

import bg.softuni.email.Model.EmailNotification;
import bg.softuni.email.Web.Dto.EmailResponse;
import bg.softuni.email.Web.Dto.EmailSentDateResponse;
import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class DtoMapper {


    public static EmailResponse fromEmailNotification(EmailNotification notification) {


      return EmailResponse.builder()
              .serviceType(notification.getServiceType())
              .appointment(notification.getAppointmentDate())
              .timeSlot(notification.getTimeSlot())
              .barberId(notification.getBarberId())
              .userId(notification.getUserId())
              .build();
    }

    public static EmailSentDateResponse fromEmailNotificationToSentDateResponse(EmailNotification notification) {


        return EmailSentDateResponse.builder()
                .sentDate(notification.getCreatedAt())
                .build();
    }

}
