package az.matrix.linkedinclone.dto.response;

import az.matrix.linkedinclone.enums.OrganizationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationResponse {
    private Long id;
    private String name;
    private String logoPath;
    private String overview;
    private String website;
    private OrganizationType organizationType;
}
