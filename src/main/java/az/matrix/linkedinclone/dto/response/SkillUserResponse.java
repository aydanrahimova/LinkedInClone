package az.matrix.linkedinclone.dto.response;

import az.matrix.linkedinclone.enums.SkillLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkillUserResponse {
    private Long id;
    private SkillResponse skill;
    private SkillLevel level;
}
