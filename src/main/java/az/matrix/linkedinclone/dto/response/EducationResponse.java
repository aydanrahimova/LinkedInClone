package az.matrix.linkedinclone.dto.response;

import az.matrix.linkedinclone.enums.EducationDegree;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationResponse {
    private Long id;
    private Long schoolId;
    private String schoolName;
    private EducationDegree degree;
    private String fieldOfStudy;
    private LocalDate startTime;
    private LocalDate endTime;
}
