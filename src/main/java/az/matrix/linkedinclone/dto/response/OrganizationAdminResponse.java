package az.matrix.linkedinclone.dto.response;

import az.matrix.linkedinclone.enums.OrganizationRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationAdminResponse {
    private UserResponse userResponse;
    private OrganizationRole role;
}
