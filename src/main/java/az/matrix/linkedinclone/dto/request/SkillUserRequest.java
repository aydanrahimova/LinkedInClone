package az.matrix.linkedinclone.dto.request;

import az.matrix.linkedinclone.enums.SkillLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkillUserRequest {
    private Long skillId;
    private SkillLevel level;
}
