package az.matrix.linkedinclone.dto.request;

import az.matrix.linkedinclone.validation.ValidDateRange;
import jakarta.validation.constraints.NotBlank;
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
@ValidDateRange(startField = "startTime", endField = "endTime")
public class ProjectRequest {
    @NotBlank(message = "Project name is required.")
    @Size(max = 255, message = "The length of the name must not exceed 255 characters")
    private String name;
    @Size(max = 2000,message = "The length of the description must not exceed 2000 characters")
    private String description;
    @PastOrPresent(message = "Start time must be in past or present")
    private LocalDate startTime;
    @PastOrPresent(message = "End time must be in past or present")
    private LocalDate endTime;
    @URL(message = "Invalid URL input")
    private String projectUrl;
}
