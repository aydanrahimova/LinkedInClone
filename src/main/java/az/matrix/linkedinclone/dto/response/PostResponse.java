package az.matrix.linkedinclone.dto.response;

import az.matrix.linkedinclone.dao.entity.Reaction;
import az.matrix.linkedinclone.enums.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private List<String> photosPath;
    private String content;
    private String createdBy;//username
    private LocalDateTime createdAt;
    private List<Reaction> reactions;
}

