package az.matrix.linkedinclone.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    @NotBlank(message = "Content required")
    private String content;
    @NotNull(message = "Post ID required")
    private Long postId;
    private Long parentCommentId;
    private Long companyId;
}
