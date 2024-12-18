package az.matrix.linkedinclone.dto.request;

import az.matrix.linkedinclone.enums.EducationDegree;
import az.matrix.linkedinclone.validation.ValidDateRange;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidDateRange(startField = "startTime", endField = "endTime")
public class EducationRequest {
    @NotNull(message = "School ID is required.")
    private Long schoolId;//organizationId
    private EducationDegree degree;
    private String fieldOfStudy;
    private LocalDate startTime;
    private LocalDate endTime;
}
