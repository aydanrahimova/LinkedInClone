package az.matrix.linkedinclone.dto.request;


import az.matrix.linkedinclone.enums.EmploymentType;
import az.matrix.linkedinclone.validation.ValidDateRange;
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
@ValidDateRange(startField = "startTime", endField = "endTime")
public class ExperienceRequest {
    @NotNull(message = "Company ID is required.")
    private Long companyId;
    @NotBlank(message = "Title is required.")
    private String title;
    @Size(max = 2000,message = "The length of the description must not exceed 255 characters")
    private String description;
    private EmploymentType employmentType;
    private LocalDate startTime;
    private LocalDate endTime;
}
