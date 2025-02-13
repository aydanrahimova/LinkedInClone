package az.matrix.linkedinclone.dto.request;

import az.matrix.linkedinclone.enums.ReactionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReactionRequest {
    @NotNull(message = "Target ID can't be null")
    private Long targetId;
    @NotNull(message = "Reaction type can't be null")
    private ReactionType reactionType;
    private Long companyId;
}
