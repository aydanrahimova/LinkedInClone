package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.response.ConnectionResponse;
import az.matrix.linkedinclone.dto.response.UserResponse;
import az.matrix.linkedinclone.enums.ConnectionStatus;
import az.matrix.linkedinclone.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/connections")
@RequiredArgsConstructor
public class ConnectionController {
    private final ConnectionService connectionService;

    @GetMapping
    public Page<UserResponse> getConnectionsOfUser(@RequestParam Long userId, @PageableDefault Pageable pageable) {
        return connectionService.getConnectionsOfUser(userId, pageable);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ConnectionResponse sendConnectionRequest(@RequestParam Long receiverId) {
        return connectionService.sendConnectionRequest(receiverId);
    }

    @PatchMapping("/{id}/status")
    public ConnectionResponse changeConnectionStatus(@PathVariable Long id, @RequestParam ConnectionStatus connectionStatus) {
        return connectionService.changeConnectionStatus(id, connectionStatus);
    }

    @PatchMapping("/{id}")
    public void deleteConnection(@PathVariable Long id) {
        connectionService.deleteConnection(id);
    }
}
