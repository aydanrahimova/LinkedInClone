package az.matrix.linkedinclone.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordDto {
    @Size(min = 3, max = 30)
    @NotNull(message = "current password can not be null")
    @Pattern(regexp = "[A-Za-z0-9]+")
    private String currentPassword;

    @NotNull(message = "new password can not be null")
    @Size(min = 3, max = 30)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$"
            , message = "Invalid Password: Must be at least 8 characters long, with at least one uppercase letter, one lowercase letter, one digit, and one special character."
    )
    private String newPassword;

    @NotNull(message = "retry password can not be null")
    @Size(min = 3, max = 30)
    @Pattern(regexp = "[A-Za-z0-9]+")
    private String retryPassword;
}
