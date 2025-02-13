package az.matrix.linkedinclone.dto.response;

import az.matrix.linkedinclone.enums.EmploymentType;
import az.matrix.linkedinclone.enums.EntityStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobResponse {
    private Long id;
    private String title;
    private EmploymentType employmentType;
    private String description;
    private String organizationName;
    private EntityStatus status;
    private LocalDate applicationDeadline;
    private List<SkillResponse> skills;
}
