package bg.softuni.email.Web.Dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class EmailRequest {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID barberId;

    @NotBlank
    private String barberName;

    @NotBlank
    private String barberEmail;

    @NotBlank
    private String userEmail;

    @NotNull
    private LocalDateTime appointmentDateTime;

    @NotBlank
    private String serviceType;

    @NotNull
    private double price;
}
