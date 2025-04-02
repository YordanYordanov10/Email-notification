package bg.softuni.email.web.DtoMapper;

import bg.softuni.email.Model.EmailNotification;
import bg.softuni.email.Web.Dto.EmailResponse;
import bg.softuni.email.Web.Mapper.DtoMapper;
import jakarta.validation.constraints.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DtoMapperUTest {

    @Test
    void givenEmailNotificationReturnEmailResponseDto(){

        EmailNotification emailNotification = EmailNotification.builder()
                .serviceType("HairStyle")
                .appointmentDate(LocalDate.now().plusDays(2))
                .timeSlot("10:00")
                .barberId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .build();

        EmailResponse responseDto = DtoMapper.fromEmailNotification(emailNotification);

        assertEquals(emailNotification.getServiceType(), responseDto.getServiceType());
        assertEquals(emailNotification.getAppointmentDate(),responseDto.getAppointment());
        assertEquals(emailNotification.getTimeSlot(),responseDto.getTimeSlot());
        assertEquals(emailNotification.getBarberId(),responseDto.getBarberId());
        assertEquals(emailNotification.getUserId(),responseDto.getUserId());
    }
}
