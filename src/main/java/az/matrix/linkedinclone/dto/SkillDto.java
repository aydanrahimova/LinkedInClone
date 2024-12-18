package az.matrix.linkedinclone.dto;

import az.matrix.linkedinclone.enums.SkillLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SkillDto {
    @NotBlank(message = "Skill name is required.")
    @Size(max = 50, message = "Skill name must not exceed 50 characters.")
    private String name;
    private SkillLevel level;
}
