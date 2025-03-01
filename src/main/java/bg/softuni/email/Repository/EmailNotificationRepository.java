package bg.softuni.email.Repository;

import bg.softuni.email.Model.EmailNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailNotificationRepository extends JpaRepository<EmailNotification, UUID> {
}
