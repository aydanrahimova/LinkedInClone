package az.matrix.linkedinclone.dto.request;

import az.matrix.linkedinclone.enums.SkillCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkillRequest {
    private String name;
    private SkillCategory category;
}
