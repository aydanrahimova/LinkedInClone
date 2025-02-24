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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/connections")
@RequiredArgsConstructor
public class ConnectionController {
    private final ConnectionService connectionService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getConnectionsOfUser(@RequestParam Long userId, @PageableDefault Pageable pageable) {
        Page<UserResponse> responses = connectionService.getConnectionsOfUser(userId, pageable);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/pending")
    public ResponseEntity<Page<UserResponse>> getPendingConnections(@PageableDefault Pageable pageable, @RequestParam Boolean sentByMe) {
        Page<UserResponse> responses = connectionService.getPendingConnections(pageable, sentByMe);
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<ConnectionResponse> sendConnectionRequest(@RequestParam Long receiverId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(connectionService.sendConnectionRequest(receiverId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ConnectionResponse> changeConnectionStatus(@PathVariable Long id, @RequestParam ConnectionStatus connectionStatus) {
        ConnectionResponse response = connectionService.changeConnectionStatus(id, connectionStatus);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> deleteConnection(@PathVariable Long id) {
        connectionService.deleteConnection(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
