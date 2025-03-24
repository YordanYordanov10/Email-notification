package bg.softuni.email.Web;

import bg.softuni.email.Model.EmailNotification;
import bg.softuni.email.Service.EmailNotificationService;
import bg.softuni.email.Web.Dto.EmailRequest;
import bg.softuni.email.Web.Dto.EmailResponse;
import bg.softuni.email.Web.Mapper.DtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailNotificationService emailNotificationService;

    public EmailController(EmailNotificationService emailNotificationService) {
        this.emailNotificationService = emailNotificationService;
    }


    @PostMapping
    public ResponseEntity<EmailResponse> sendNotification(@RequestBody EmailRequest emailRequest) {

        EmailNotification emailNotification = emailNotificationService.sendNotification(emailRequest);

        EmailResponse response = DtoMapper.fromEmailNotification(emailNotification);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteEmailNotification(@RequestParam(name = "userId")UUID userId) {

        emailNotificationService.deleteEmailNotification(userId);

        return ResponseEntity.ok().body(null);
    }

}
