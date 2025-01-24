package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.response.ConnectionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ConnectionService {
    ConnectionResponse sendConnectionRequest(Long receiverId);

    Page<ConnectionResponse> getConnectionsOfUser(Long userId,Pageable pageable);

    ConnectionResponse acceptConnectionRequest(Long id);

    ConnectionResponse rejectConnectionRequest(Long id);

    void deleteConnection(Long id);
}
