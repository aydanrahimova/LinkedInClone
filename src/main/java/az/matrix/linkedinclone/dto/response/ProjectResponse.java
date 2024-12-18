package az.matrix.linkedinclone.dto.response;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDate startTime;
    private LocalDate endTime;
    private String projectUrl;
}
