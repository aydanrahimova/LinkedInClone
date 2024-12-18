package az.matrix.linkedinclone.dto.request;

import az.matrix.linkedinclone.enums.OrganizationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationRequest {
    @NotBlank(message = "Organization name is required.")
    private String name;
    private MultipartFile logoPath;
    private String overview;
    @URL
    private String website;
    @NotNull(message = "Organization type is required.")
    private OrganizationType organizationType;
}
