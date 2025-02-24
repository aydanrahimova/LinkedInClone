package az.matrix.linkedinclone.dto.request;

import az.matrix.linkedinclone.enums.EmploymentType;
import az.matrix.linkedinclone.validation.ValidationGroups;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobRequest {
    @NotNull(message = "Organization ID is required.",groups = ValidationGroups.onCreate.class)
    private Long organizationId;
    @NotBlank(message = "Title is required.")
    private String title;
    @NotNull(message = "Employment type is required.")
    private EmploymentType employmentType;
    @Size(min = 5, max = 2000, message = "Description of job should be between 5 and 2000 characters.")
    private String description;
    @NotNull
    @Future
    private LocalDate applicationDeadline;
    private List<Long> skillId;
}
