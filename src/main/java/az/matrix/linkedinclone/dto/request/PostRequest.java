package az.matrix.linkedinclone.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private List<MultipartFile> photos;
    @NotNull(message = "Content is required")
    @Size(min = 10, message = "Content must be at least 10 characters long")
    private String content;
}
