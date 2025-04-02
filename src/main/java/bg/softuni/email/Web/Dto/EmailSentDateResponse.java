package bg.softuni.email.Web.Dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EmailSentDateResponse {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime sentDate;
}
