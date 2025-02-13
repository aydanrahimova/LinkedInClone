package az.matrix.linkedinclone.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailRequest {
    @Email
    private String receiver;
    private String subject;
    private String body;
}
