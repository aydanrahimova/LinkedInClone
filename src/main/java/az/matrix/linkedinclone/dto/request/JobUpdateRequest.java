package az.matrix.linkedinclone.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobUpdateRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    @Future
    private LocalDate applicationDeadline;
}
