package az.matrix.linkedinclone.dto.request;

import az.matrix.linkedinclone.enums.EmploymentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobRequest {
    @NotNull(message = "Organization ID is required.")
    private Long organizationId;
    @NotBlank(message = "Title is required.")
    private String title;
    @NotNull(message = "Employment type is required.")
    private EmploymentType employmentType;
    @Size(min = 5,max = 200,message = "Description of job should be between 5 and 200 characters.")
    private String description;
}
