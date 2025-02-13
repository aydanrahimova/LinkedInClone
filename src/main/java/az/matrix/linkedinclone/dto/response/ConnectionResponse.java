package az.matrix.linkedinclone.dto.response;

import az.matrix.linkedinclone.enums.ConnectionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionResponse {
    private Long id;
    private UserResponse connectedUser;
    private ConnectionStatus status;
    private LocalDate sendTime;
    private LocalDate responseTime;
}
