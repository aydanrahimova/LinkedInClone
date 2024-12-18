package az.matrix.linkedinclone.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsRequest {
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

    private MultipartFile photo;

    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters.")
    private String title;

    @Size(max = 500, message = "About section cannot exceed 500 characters.")
    private String about;

    @Pattern(regexp = "^\\+?[1-9][0-9]{1,14}$", message = "Phone number must be in a valid format, e.g., +1234567890.")
    private String phone;

    @Past(message = "Birthdate must be in past.")
    private LocalDate birthDate;
}
