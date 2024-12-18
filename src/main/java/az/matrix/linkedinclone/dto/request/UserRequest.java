package az.matrix.linkedinclone.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank(message = "First name is required.")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
    @Pattern(regexp = "^[A-Za-z]+$", message = "First name can only contain letters.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name can only contain letters.")
    private String lastName;

    @NotBlank(message = "Email is required.")
    @Size(max = 100, message = "Email cannot be longer than 100 characters.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$"
            , message = "Invalid Password: Must be at least 8 characters long, with at least one uppercase letter, one lowercase letter, one digit, and one special character."
    )
    private String password;

    @NotBlank(message = "Password confirmation is required.")
    private String passwordConfirm;
}
