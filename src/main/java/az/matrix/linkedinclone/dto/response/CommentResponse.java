package az.matrix.linkedinclone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private Long id;
    private AuthorResponse author;
    private String content;
    private List<CommentResponse> replies;
}
