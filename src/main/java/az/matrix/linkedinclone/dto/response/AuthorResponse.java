package az.matrix.linkedinclone.dto.response;

import az.matrix.linkedinclone.enums.AuthorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorResponse {
    private Long authorId;
    private String imageUrl;
    private String authorName;
    private AuthorType authorType;
}
