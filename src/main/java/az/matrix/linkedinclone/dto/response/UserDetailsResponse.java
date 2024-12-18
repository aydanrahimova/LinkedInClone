package az.matrix.linkedinclone.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsResponse {
    private String firstName;
    private String lastName;
    private String title;
    private String email;
    private String photoUrl;
    private String about;
    private String phone;
    private LocalDate birthDate;
}
