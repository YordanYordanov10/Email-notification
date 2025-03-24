package bg.softuni.email.Model;

import jakarta.persistence.*;
import lombok.*;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private UUID barberId;

    @Column(nullable = false)
    private String barberName;

    @Column(nullable = false)
    private String barberEmail;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Column(nullable = false)
    private String timeSlot;

    @Column(nullable = false)
    private String serviceType;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean reminderSent;

}
