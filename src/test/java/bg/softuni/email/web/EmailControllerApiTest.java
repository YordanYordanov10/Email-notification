package bg.softuni.email.web;

import bg.softuni.email.Model.EmailNotification;
import bg.softuni.email.Service.EmailNotificationService;
import bg.softuni.email.Web.Dto.EmailRequest;
import bg.softuni.email.Web.Dto.EmailSentDateResponse;
import bg.softuni.email.Web.EmailController;
import bg.softuni.email.Web.Mapper.DtoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;


import javax.management.Notification;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EmailController.class)
public class EmailControllerApiTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private EmailNotificationService emailNotificationService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());


    @Test
    void postWithBodySendNotification_returns201AndCorrectStructure() throws Exception {

        EmailRequest requestDto = EmailRequest.builder()
                .userEmail("daka@gmail.com")
                .userId(UUID.randomUUID())
                .barberId(UUID.randomUUID())
                .price(12)
                .serviceType("HairCut")
                .barberEmail("barber@gmail.com")
                .appointmentDate(LocalDate.now().plusDays(3))
                .timeSlot("10:00")
                .build();

        EmailNotification emailNotification = EmailNotification.builder()
                .userId(requestDto.getUserId())
                .barberId(requestDto.getBarberId())
                .userEmail(requestDto.getUserEmail())
                .barberEmail(requestDto.getBarberEmail())
                .appointmentDate(requestDto.getAppointmentDate())
                .timeSlot(requestDto.getTimeSlot())
                .serviceType(requestDto.getServiceType())
                .reminderSent(false)
                .price(requestDto.getPrice())
                .createdAt(LocalDateTime.now())
                .build();

        when(emailNotificationService.sendNotification(any())).thenReturn(emailNotification);


        MockHttpServletRequestBuilder request = post("/api/v1/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto));


        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("userId").isNotEmpty())
                .andExpect(jsonPath("barberId").isNotEmpty())
                .andExpect(jsonPath("serviceType").isNotEmpty())
                .andExpect(jsonPath("appointment").isNotEmpty())
                .andExpect(jsonPath("timeSlot").isNotEmpty());

    }

    @Test
    void givenUserIdAndAppointmentDateAndTimeSlotShouldDeleteEmailNotificationByUser() throws Exception {

        UUID userId = UUID.randomUUID();
        LocalDate appointmentDate = LocalDate.now().plusDays(3);
        String timeSlot = "10:00";


                MockHttpServletRequestBuilder request = delete("/api/v1/email/byUser")
                        .param("userId",userId.toString())
                        .param("appointmentDate",appointmentDate.toString())
                        .param("timeSlot",timeSlot);

                mockMvc.perform(request)
                        .andExpect(status().isOk());
    }

    @Test
    void givenUserIdAndAppointmentDateAndTimeSlotShouldDeleteEmailNotificationByBarber() throws Exception {

        UUID barberId = UUID.randomUUID();
        LocalDate appointmentDate = LocalDate.now().plusDays(3);
        String timeSlot = "10:00";


        MockHttpServletRequestBuilder request = delete("/api/v1/email/byBarber")
                .param("barberId",barberId.toString())
                .param("appointmentDate",appointmentDate.toString())
                .param("timeSlot",timeSlot);

        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    void givenBarberIdAppointmentDateAndTimeSlotShouldReturnWhenIsSentTheEmail() throws Exception {

        UUID barberId = UUID.randomUUID();
        LocalDate appointmentDate = LocalDate.now().plusDays(3);
        String timeSlot = "10:00";

        EmailNotification notification = EmailNotification.builder()
                .createdAt(LocalDateTime.now().plusDays(3))
                .build();

        when(emailNotificationService.findNotificationEmailSentDate(appointmentDate, timeSlot, barberId))
                .thenReturn(notification);

        EmailSentDateResponse responseDto = DtoMapper.fromEmailNotificationToSentDateResponse(notification);


        mockMvc.perform(get("/api/v1/email/sent-date")
                        .param("barberId", barberId.toString())
                        .param("appointmentDate", appointmentDate.toString())
                        .param("timeSlot", timeSlot)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sentDate").value(
                        notification.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                ));
    }




}




