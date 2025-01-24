package az.matrix.linkedinclone.dto.response;

import az.matrix.linkedinclone.enums.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReactionResponse {
    private UserResponse user;
    private ReactionType reactionType;
    private LocalDateTime addTime;
    private LocalDateTime editTime;
    private Boolean isEdited;
}
