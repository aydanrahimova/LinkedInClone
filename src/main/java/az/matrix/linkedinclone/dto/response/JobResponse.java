package az.matrix.linkedinclone.dto.response;

import az.matrix.linkedinclone.enums.EmploymentType;
import az.matrix.linkedinclone.enums.EntityStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobResponse {
    private String title;
    private EmploymentType employmentType;
    private String description;
    private String organizationName;
    private EntityStatus status;
//    private List<Skill> skills;
}
