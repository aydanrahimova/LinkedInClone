package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.response.ConnectionResponse;
import az.matrix.linkedinclone.dto.response.UserResponse;
import az.matrix.linkedinclone.enums.ConnectionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ConnectionService {
    ConnectionResponse sendConnectionRequest(Long receiverId);

    Page<UserResponse> getConnectionsOfUser(Long userId, Pageable pageable);

    ConnectionResponse changeConnectionStatus(Long id, ConnectionStatus connectionStatus);

    void deleteConnection(Long id);
}
