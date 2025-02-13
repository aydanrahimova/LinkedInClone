package az.matrix.linkedinclone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long id;
    private List<String> photosPath;
    private String text;
    private AuthorResponse author;
    private LocalDateTime addTime;
    private LocalDateTime editTime;
    private Boolean isEdited;
}

