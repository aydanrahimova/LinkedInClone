package az.matrix.linkedinclone.dto.request;

import az.matrix.linkedinclone.enums.EmploymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobFilterDto {
    private String title;
    private EmploymentType employmentType;
    private String organizationName;
}
