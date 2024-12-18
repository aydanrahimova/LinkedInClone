package az.matrix.linkedinclone.dto.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AuthRequest {
    private String email;
    private String password;
}