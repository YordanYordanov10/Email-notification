package bg.softuni.email.Web;

import bg.softuni.email.Model.EmailNotification;
import bg.softuni.email.Service.EmailNotificationService;
import bg.softuni.email.Web.Dto.EmailRequest;
import bg.softuni.email.Web.Dto.EmailResponse;
import bg.softuni.email.Web.Dto.EmailSentDateResponse;
import bg.softuni.email.Web.Mapper.DtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @DeleteMapping("/byUser")
    public ResponseEntity<Void> deleteEmailNotification(@RequestParam(name = "userId")UUID userId,
                                                        @RequestParam(name= "appointmentDate") LocalDate appointmentDate,
                                                        @RequestParam(name = "timeSlot") String timeSlot) {

        emailNotificationService.deleteEmailNotification(userId,appointmentDate,timeSlot);

        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("/byBarber")
    public ResponseEntity<Void> deleteEmailNotificationByBarber(@RequestParam(name = "barberId")UUID barberId,
                                                        @RequestParam(name= "appointmentDate") LocalDate appointmentDate,
                                                        @RequestParam(name = "timeSlot") String timeSlot) {

        emailNotificationService.deleteEmailNotificationByBarber(barberId,appointmentDate,timeSlot);

        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/sent-date")
    public ResponseEntity<EmailSentDateResponse> emailNotificationSentDate(@RequestParam(name = "barberId")UUID barberId,
                                                                           @RequestParam(name= "appointmentDate") LocalDate appointmentDate,
                                                                           @RequestParam(name = "timeSlot") String timeSlot) {

        EmailNotification notification = emailNotificationService.findNotificationEmailSentDate(appointmentDate,timeSlot,barberId);

        if (notification == null) {
            return ResponseEntity.notFound().build();
        }

        EmailSentDateResponse responseDto = DtoMapper.fromEmailNotificationToSentDateResponse(notification);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

}
