package az.matrix.linkedinclone.dto.response;

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
public class ExperienceResponse {
    private Long id;
    private Long companyId;
    private String companyName;
    private String title;
    private String description;
    private EmploymentType employmentType;
    private LocalDate startTime;
    private LocalDate endTime;
}
