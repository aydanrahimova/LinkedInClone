package az.matrix.linkedinclone.dto.response;

import az.matrix.linkedinclone.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationResponse {
    private UserResponse applicant;
    private String resumeUrl;
    private ApplicationStatus status;
}
