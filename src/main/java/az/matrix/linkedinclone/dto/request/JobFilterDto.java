package az.matrix.linkedinclone.dto.request;

import az.matrix.linkedinclone.enums.EmploymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobFilterDto {
    private String title;
    private EmploymentType employmentType;
    private String organizationName;
    private List<String> skills;
}
