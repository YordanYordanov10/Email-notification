package bg.softuni.email.Web.Dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class EmailResponse {

    private UUID userId;

    private UUID barberId;

    private LocalDateTime appointmentTime;

    private String serviceType;
}
