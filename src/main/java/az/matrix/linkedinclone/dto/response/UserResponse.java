package az.matrix.linkedinclone.dto.response;

import az.matrix.linkedinclone.enums.EntityStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String title;
    private EntityStatus status;
}
