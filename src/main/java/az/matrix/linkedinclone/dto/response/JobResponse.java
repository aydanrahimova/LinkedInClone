package az.matrix.linkedinclone.dto.response;

import az.matrix.linkedinclone.enums.EmploymentType;
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
//    private List<Skill> skills;
}
