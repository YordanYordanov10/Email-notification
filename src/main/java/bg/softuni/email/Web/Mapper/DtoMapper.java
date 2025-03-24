package bg.softuni.email.Web.Mapper;

import bg.softuni.email.Model.EmailNotification;
import bg.softuni.email.Web.Dto.EmailResponse;
import lombok.experimental.UtilityClass;

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

}
