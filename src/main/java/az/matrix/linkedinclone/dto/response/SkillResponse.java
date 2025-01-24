package az.matrix.linkedinclone.dto.response;

import az.matrix.linkedinclone.enums.SkillCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkillResponse {
    private Long id;
    private String name;
    private SkillCategory category;
}
